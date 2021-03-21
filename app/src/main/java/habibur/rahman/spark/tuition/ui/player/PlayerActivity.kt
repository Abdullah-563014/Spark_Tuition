package habibur.rahman.spark.tuition.ui.player

import android.annotation.SuppressLint
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
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import habibur.rahman.spark.tuition.R
import habibur.rahman.spark.tuition.databinding.ActivityPlayerBinding
import habibur.rahman.spark.tuition.utils.CommonMethod
import habibur.rahman.spark.tuition.utils.Constants
import habibur.rahman.spark.tuition.utils.MyExtension.shortMessage


class PlayerActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityPlayerBinding
    private var simpleExoPlayer: SimpleExoPlayer?=null
    private var isLiveVideo: Boolean=false
    private var liveChatNumber: String="+8801750179685"
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private val mediaLink: String="http://192.168.100.100/spark_tuition/uploads/my_video.mp4"
//    private val mediaLink: String="https://media.geeksforgeeks.org/wp-content/uploads/20201217163353/Screenrecorder-2020-12-17-16-32-03-350.mp4"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)



        initAll()

    }


    private fun initAll() {
        binding.liveChat.setOnClickListener(this)
    }

    private fun initPlayer() {
        try {
            resizePlayerHeight(resources.configuration.orientation)
            if (isLiveVideo) {
                binding.exoPlayerId.setShowShuffleButton(false)
                binding.exoPlayerId.setShowSubtitleButton(false)
                binding.exoPlayerId.setShowVrButton(false)
                binding.exoPlayerId.setRepeatToggleModes(0)
                binding.exoPlayerId.useController=false
            } else {
                binding.exoPlayerId.setShowShuffleButton(true)
                binding.exoPlayerId.setShowSubtitleButton(true)
                binding.exoPlayerId.setShowVrButton(true)
                binding.exoPlayerId.setRepeatToggleModes(0)
                binding.exoPlayerId.useController=true
            }
            simpleExoPlayer=SimpleExoPlayer.Builder(this).build()
            binding.exoPlayerId.player=simpleExoPlayer
            val mediaItem: MediaItem= MediaItem.fromUri(Uri.parse(mediaLink))
            simpleExoPlayer?.setMediaItem(mediaItem)
            simpleExoPlayer?.prepare()
            simpleExoPlayer?.playWhenReady=true
            simpleExoPlayer?.seekTo(currentWindow,playbackPosition)
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
        if (orientationState==Configuration.ORIENTATION_LANDSCAPE) {
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

    override fun onClick(p0: View?) {
        p0?.let {
            when(it.id) {
                R.id.liveChat -> CommonMethod.openWhatsApp(this,liveChatNumber)
            }
        }
    }


}