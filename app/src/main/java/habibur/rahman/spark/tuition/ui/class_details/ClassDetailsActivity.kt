package habibur.rahman.spark.tuition.ui.class_details

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import habibur.rahman.spark.tuition.R
import habibur.rahman.spark.tuition.databinding.ActivityClassDetailsBinding
import habibur.rahman.spark.tuition.ui.player.PlayerActivity

class ClassDetailsActivity : AppCompatActivity(), View.OnClickListener {


    private lateinit var binding: ActivityClassDetailsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityClassDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initAll()

    }


    private fun initAll() {
        binding.liveClassButton.setOnClickListener(this)
        binding.previousClassButton.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when(it.id) {
                R.id.previousClassButton -> startActivity(Intent(this, PlayerActivity::class.java))
                R.id.liveClassButton -> startActivity(Intent(this, PlayerActivity::class.java))
            }
        }
    }


}