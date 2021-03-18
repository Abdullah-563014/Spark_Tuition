package habibur.rahman.spark.tuition.ui.class_list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import habibur.rahman.spark.tuition.R
import habibur.rahman.spark.tuition.databinding.ActivityClassListBinding
import habibur.rahman.spark.tuition.ui.class_details.ClassDetailsActivity

class ClassListActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityClassListBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityClassListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initAll()
        
    }


    private fun initAll() {
        binding.yearThreeButton.setOnClickListener(this)
        binding.yearFourButton.setOnClickListener(this)
        binding.yearFiveButton.setOnClickListener(this)
        binding.yearSixButton.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when(it.id) {
                R.id.yearThreeButton -> startActivity(Intent(this,ClassDetailsActivity::class.java))
            }
        }
    }
}