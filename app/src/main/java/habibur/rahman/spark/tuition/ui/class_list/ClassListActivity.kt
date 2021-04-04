package habibur.rahman.spark.tuition.ui.class_list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import habibur.rahman.spark.tuition.R
import habibur.rahman.spark.tuition.databinding.ActivityClassListBinding
import habibur.rahman.spark.tuition.model.UserInfoModel
import habibur.rahman.spark.tuition.ui.MyApplication
import habibur.rahman.spark.tuition.ui.class_details.ClassDetailsActivity
import habibur.rahman.spark.tuition.ui.profile.ProfileActivityViewModel
import habibur.rahman.spark.tuition.ui.profile.ProfileActivityViewModelFactory
import habibur.rahman.spark.tuition.utils.Constants
import habibur.rahman.spark.tuition.utils.MyExtension.longMessage
import habibur.rahman.spark.tuition.utils.MyExtension.shortMessage
import habibur.rahman.spark.tuition.utils.MyExtension.showDialog

class ClassListActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityClassListBinding
    private lateinit var viewModel: ClassListViewModel
    private var userInfoModel: UserInfoModel?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityClassListBinding.inflate(layoutInflater)
        val factory: ClassListViewModelFactory = ClassListViewModelFactory((application as MyApplication).appDatabase.userInfoDao())
        viewModel= ViewModelProvider(this, factory).get(ClassListViewModel::class.java)
        setContentView(binding.root)


        initAll()

        loadUserInfo()
        
    }


    private fun initAll() {
        binding.yearThreeButton.setOnClickListener(this)
        binding.yearFourButton.setOnClickListener(this)
        binding.yearFiveButton.setOnClickListener(this)
        binding.yearSixButton.setOnClickListener(this)
    }

    private fun loadUserInfo() {
        viewModel.userInfoLiveData.observe(this,object : Observer<UserInfoModel> {
            override fun onChanged(t: UserInfoModel?) {
                t?.let {
                    userInfoModel=it
                }
            }

        })
    }

    private fun checkClassInfo(className: String) {
        if (userInfoModel!=null) {
            if (className.equals(userInfoModel!!.className,true)) {
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