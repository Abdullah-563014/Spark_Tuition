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
                startActivity(Intent(this@SplashActivity,LoginActivity::class.java))
                finish()
            }

            override fun onAnimationRepeat(p0: Animation?) {

            }

        })
    }


}