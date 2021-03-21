package habibur.rahman.spark.tuition.ui.class_list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.lifecycle.asLiveData
import habibur.rahman.spark.tuition.R
import habibur.rahman.spark.tuition.databinding.ActivityClassListBinding
import habibur.rahman.spark.tuition.ui.MyApplication
import habibur.rahman.spark.tuition.ui.class_details.ClassDetailsActivity
import habibur.rahman.spark.tuition.utils.Constants
import habibur.rahman.spark.tuition.utils.MyExtension.longMessage
import habibur.rahman.spark.tuition.utils.MyExtension.shortMessage
import habibur.rahman.spark.tuition.utils.MyExtension.showDialog

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

    private fun checkClassInfo(className: String) {
        if (Constants.userInfoModel!=null) {
            if (className.equals(Constants.userInfoModel!!.className,true)) {
                startActivity(Intent(this,ClassDetailsActivity::class.java))
            } else {
                showDialog(resources.getString(R.string.class_selection),resources.getString(R.string.not_eligible_to_access_this_class))
            }
        } else{
            shortMessage(resources.getString(R.string.login_and_try_again))
            finishAffinity()
        }
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when(it.id) {
                R.id.yearThreeButton,
                    R.id.yearFourButton,
                    R.id.yearFiveButton,
                    R.id.yearSixButton -> checkClassInfo(findViewById<Button>(it.id).text.toString())
            }
        }
    }


}