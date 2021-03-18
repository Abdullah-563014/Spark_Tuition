package habibur.rahman.spark.tuition.ui.player

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import habibur.rahman.spark.tuition.databinding.ActivityPlayerBinding
import habibur.rahman.spark.tuition.utils.Constants


class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private var simpleExoPlayer: SimpleExoPlayer?=null
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private val mediaLink: String="http://192.168.100.101/spark_tuition/uploads/my_video.mp4"
//    private val mediaLink: String="https://media.geeksforgeeks.org/wp-content/uploads/20201217163353/Screenrecorder-2020-12-17-16-32-03-350.mp4"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    private fun initPlayer() {
        try {
            simpleExoPlayer=SimpleExoPlayer.Builder(this).build()
            binding.exoPlayerId.player=simpleExoPlayer
            val mediaItem: MediaItem= MediaItem.fromUri(Uri.parse(mediaLink))
            simpleExoPlayer?.setMediaItem(mediaItem)
            simpleExoPlayer?.prepare()
            simpleExoPlayer?.playWhenReady=true
//        simpleExoPlayer?.play()
        } catch (e: Exception) {
            Log.d(Constants.TAG,"player error is:- ${e.message}")
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




}