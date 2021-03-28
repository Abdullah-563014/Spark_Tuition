package habibur.rahman.spark.tuition.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import habibur.rahman.spark.tuition.R
import habibur.rahman.spark.tuition.databinding.ActivitySplashBinding
import habibur.rahman.spark.tuition.ui.login.LoginActivity
import habibur.rahman.spark.tuition.utils.CommonMethod
import habibur.rahman.spark.tuition.utils.MyExtension.showDialog

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)


        loadAnimation()


    }


    private fun loadAnimation() {
        val slideBottomToTop: Animation= AnimationUtils.loadAnimation(this,R.anim.slidebottomtotop)
        slideBottomToTop.interpolator= FastOutSlowInInterpolator()
        binding.splashTitleTextView.startAnimation(slideBottomToTop)
        slideBottomToTop.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                if (CommonMethod.isNetworkAvailable(this@SplashActivity)) {
                    startActivity(Intent(this@SplashActivity,LoginActivity::class.java))
                    finish()
                } else {
                    showDialog(resources.getString(R.string.network_connection_status),resources.getString(R.string.no_internet_message))
                }
            }

            override fun onAnimationRepeat(p0: Animation?) {

            }

        })
    }


}