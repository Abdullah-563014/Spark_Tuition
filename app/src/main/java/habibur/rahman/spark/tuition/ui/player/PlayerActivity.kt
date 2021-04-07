package habibur.rahman.spark.tuition.ui.player

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.FadingCircle
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.gson.Gson
import com.google.gson.JsonElement
import habibur.rahman.spark.tuition.R
import habibur.rahman.spark.tuition.databinding.ActivityPlayerBinding
import habibur.rahman.spark.tuition.model.SavedMediaLinkModel
import habibur.rahman.spark.tuition.model.TutorInfoModel
import habibur.rahman.spark.tuition.model.UserInfoModel
import habibur.rahman.spark.tuition.model.VideoModel
import habibur.rahman.spark.tuition.network_database.MyApi
import habibur.rahman.spark.tuition.ui.MyApplication
import habibur.rahman.spark.tuition.ui.live_support.LiveSupportActivity
import habibur.rahman.spark.tuition.ui.previous_class_list.PreviousClassListViewModel
import habibur.rahman.spark.tuition.ui.previous_class_list.PreviousClassListViewModelFactory
import habibur.rahman.spark.tuition.utils.CommonMethod
import habibur.rahman.spark.tuition.utils.Constants
import habibur.rahman.spark.tuition.utils.Coroutines
import habibur.rahman.spark.tuition.utils.MyExtension.shortMessage
import habibur.rahman.spark.tuition.utils.SharedPreUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PlayerActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var viewModel: PlayerActivityViewModel
    private var simpleExoPlayer: SimpleExoPlayer?=null
    private var isLiveVideo: Boolean=false
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private var mediaLink: String?=null
    private var currentClassPosition: Int=0
    private var savedMediaLinkModel: SavedMediaLinkModel?=null
    private var userInfoModel: UserInfoModel?=null
    private var refreshJob: Job?=null
//    private val mediaLink: String="https://media.geeksforgeeks.org/wp-content/uploads/20201217163353/Screenrecorder-2020-12-17-16-32-03-350.mp4"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPlayerBinding.inflate(layoutInflater)
        val factory: PlayerActivityViewModelFactory = PlayerActivityViewModelFactory((application as MyApplication).appDatabase.userInfoDao())
        viewModel = ViewModelProvider(this, factory).get(PlayerActivityViewModel::class.java)
        setContentView(binding.root)



        initAll()


        if (intent.extras!=null) {
            isLiveVideo=intent.extras!!.getBoolean(Constants.videoPlayModeIsLive)
            if (!isLiveVideo) {
                currentClassPosition= intent.extras!!.getInt(Constants.classVideoPosition)
                loadMediaLinkFromStorage()
            } else {
                loadUserInfo()
            }
        }




    }


    private fun initAll() {
        binding.liveChat.setOnClickListener(this)
        val fadingCircle: Sprite = FadingCircle()
        binding.playerSpinKit.setIndeterminateDrawable(fadingCircle)
        binding.playerSpinKit.visibility= View.GONE
    }

    private fun loadUserInfo() {
        viewModel.userInfoLiveData.observe(this,object : Observer<UserInfoModel> {
            override fun onChanged(t: UserInfoModel?) {
                t?.let {
                    userInfoModel=it
                    loadLiveVideoListFromNetwork()
                }
            }

        })
    }

    private fun loadMediaLinkFromStorage() {
        binding.playerSpinKit.visibility=View.VISIBLE
        val gson: Gson = Gson()
        var dataString: String?=null
        Coroutines.main {
            binding.playerSpinKit.visibility=View.GONE
            dataString= SharedPreUtils.getStringFromStorage(
                applicationContext,
                Constants.savedMediaLink,
                null
            )
            dataString?.let {
                savedMediaLinkModel =gson.fromJson(it, SavedMediaLinkModel::class.java)
                currentWindow=currentClassPosition
                initPlayer()
            }
        }
    }

    private fun loadLiveVideoListFromNetwork() {
        binding.playerSpinKit.visibility=View.VISIBLE
        userInfoModel?.let {
            val call: Call<JsonElement> = MyApi.invoke().getLiveVideoList(it.className)
            call.enqueue(object : Callback<JsonElement> {
                override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                    binding.playerSpinKit.visibility=View.GONE
                    if (response.isSuccessful && response.code() == 200) {
                        val rootArray: JSONArray = JSONArray(response.body().toString())
                        if (rootArray.getJSONObject(0).getString("status").equals("success", false)) {
                            val userObject = rootArray.getJSONObject(1)
                            val innerObject: JSONObject = userObject.getJSONObject("message")
                            mediaLink=innerObject.getString("VideoUrl")
                            initPlayer()
                            refreshJob?.cancel()
                            refreshJob=null
                        } else {
                            shortMessage(rootArray.getJSONObject(1).getString("message"))
                            mediaLink=null
                            if (refreshJob?.isActive != true) {
                                refreshJob?.cancel()
                                refreshJob=null
                                refreshForLiveVideo()
                            } else {
                                refreshForLiveVideo()
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    binding.playerSpinKit.visibility=View.GONE
                    shortMessage("${resources.getString(R.string.failed_for)} ${t.message}")
                }

            })
        }
    }

    private fun refreshForLiveVideo() {
        refreshJob= CoroutineScope(Main).launch {
            delay(20000)
            loadLiveVideoListFromNetwork()
        }
    }

    private fun initPlayer() {
        try {
//            resizePlayerHeight(resources.configuration.orientation)
            simpleExoPlayer=SimpleExoPlayer.Builder(this).build()
            binding.exoPlayerId.player=simpleExoPlayer
            if (isLiveVideo) {
                binding.exoPlayerId.setShowShuffleButton(false)
                binding.exoPlayerId.setShowSubtitleButton(false)
                binding.exoPlayerId.setShowVrButton(false)
                binding.exoPlayerId.setRepeatToggleModes(0)
                binding.exoPlayerId.useController=false
                resizePlayerHeight(Configuration.ORIENTATION_PORTRAIT)
                mediaLink?.let {
                    val mediaItem: MediaItem= MediaItem.fromUri(Uri.parse(it))
                    simpleExoPlayer?.setMediaItem(mediaItem)
                    simpleExoPlayer?.prepare()
                    simpleExoPlayer?.playWhenReady=true
                    simpleExoPlayer?.seekTo(currentWindow,playbackPosition)
                }
            } else {
                binding.exoPlayerId.setShowShuffleButton(true)
                binding.exoPlayerId.setShowSubtitleButton(false)
                binding.exoPlayerId.setShowVrButton(true)
                binding.exoPlayerId.setRepeatToggleModes(0)
                binding.exoPlayerId.useController=true
                resizePlayerHeight(Configuration.ORIENTATION_LANDSCAPE)
                savedMediaLinkModel?.let {
                    for (i in it.allVideoInfo.indices) {
                        val mediaItem: MediaItem= MediaItem.fromUri(Uri.parse(it.allVideoInfo[i].VideoUrl))
                        simpleExoPlayer?.addMediaItem(mediaItem)
                    }
                    simpleExoPlayer?.prepare()
                    simpleExoPlayer?.playWhenReady=true
                    simpleExoPlayer?.seekTo(currentWindow,playbackPosition)
                }
            }

//        simpleExoPlayer?.play()
        } catch (e: Exception) {

        }

    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        binding.exoPlayerId.setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LOW_PROFILE
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        )
    }

    private fun releasePlayer() {
        if (simpleExoPlayer != null) {
            playbackPosition = simpleExoPlayer!!.currentPosition
            currentWindow = simpleExoPlayer!!.currentWindowIndex
            simpleExoPlayer!!.release()
            simpleExoPlayer = null
        }
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        resizePlayerHeight(newConfig.orientation)
    }

    private fun resizePlayerHeight(orientationState: Int) {
        val layoutParams: ViewGroup.LayoutParams? =binding.exoPlayerId.layoutParams
        if (orientationState==Configuration.ORIENTATION_LANDSCAPE || !isLiveVideo) {
            layoutParams?.height=ViewGroup.LayoutParams.MATCH_PARENT
            binding.liveChat.visibility=View.GONE
        } else {
            val splittedScreenHeight: Int=(CommonMethod.getScreenHeight(this))/3
            layoutParams?.height=splittedScreenHeight*2
            binding.liveChat.visibility=View.VISIBLE
        }
        binding.exoPlayerId.layoutParams=layoutParams
    }

    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT>=24) {
            initPlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()
        if (Build.VERSION.SDK_INT<24 && simpleExoPlayer==null) {
            initPlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT<24) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Build.VERSION.SDK_INT>=24) {
            releasePlayer()
        }
    }

    override fun onDestroy() {
        refreshJob?.cancel()
        refreshJob=null
        super.onDestroy()
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when(it.id) {
                R.id.liveChat -> startActivity(Intent(this,LiveSupportActivity::class.java))
            }
        }
    }


}