package habibur.rahman.spark.tuition.ui.class_details

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import habibur.rahman.spark.tuition.R
import habibur.rahman.spark.tuition.databinding.ActivityClassDetailsBinding
import habibur.rahman.spark.tuition.ui.player.PlayerActivity
import habibur.rahman.spark.tuition.ui.previous_class_list.PreviousClassListActivity
import habibur.rahman.spark.tuition.utils.Constants

class ClassDetailsActivity : AppCompatActivity(), View.OnClickListener {


    private lateinit var binding: ActivityClassDetailsBinding
    private lateinit var playIntent: Intent


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
                R.id.previousClassButton -> {
                    playIntent= Intent(this,PreviousClassListActivity::class.java)
                    startActivity(playIntent)
                }
                R.id.liveClassButton -> {
                    playIntent= Intent(this,PlayerActivity::class.java)
                    playIntent.putExtra(Constants.videoPlayModeIsLive,true)
                    startActivity(playIntent)
                }
            }
        }
    }


}