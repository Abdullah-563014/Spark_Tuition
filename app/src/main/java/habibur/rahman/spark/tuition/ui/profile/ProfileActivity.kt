package habibur.rahman.spark.tuition.ui.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import habibur.rahman.spark.tuition.R
import habibur.rahman.spark.tuition.databinding.ActivityProfileBinding
import habibur.rahman.spark.tuition.model.UserInfoModel
import habibur.rahman.spark.tuition.ui.MyApplication
import habibur.rahman.spark.tuition.ui.login.LoginActivity
import habibur.rahman.spark.tuition.ui.login.LoginActivityViewModel
import habibur.rahman.spark.tuition.ui.login.LoginActivityViewModelFactory
import habibur.rahman.spark.tuition.utils.Constants
import habibur.rahman.spark.tuition.utils.Coroutines
import habibur.rahman.spark.tuition.utils.SharedPreUtils

class ProfileActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var viewModel: ProfileActivityViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityProfileBinding.inflate(layoutInflater)
        val factory: ProfileActivityViewModelFactory = ProfileActivityViewModelFactory((application as MyApplication).appDatabase.userInfoDao())
        viewModel= ViewModelProvider(this, factory).get(ProfileActivityViewModel::class.java)
        setContentView(binding.root)


        initAll()

        getUserInfo()


    }

    private fun initAll() {
        binding.signOutButton.setOnClickListener(this)
    }

    private fun getUserInfo() {
        viewModel.userInfoLiveData.observe(this,object : Observer<UserInfoModel> {
            override fun onChanged(t: UserInfoModel?) {
                t?.let {
                    binding.nameTextView.text="${resources.getString(R.string.name)}:- ${it.name}"
                    binding.emailTextView.text="${resources.getString(R.string.email)}:- ${it.email}"
                    binding.phoneTextView.text="${resources.getString(R.string.phone)}:- ${it.phone}"
                    binding.classNameTextView.text="${resources.getString(R.string.class_name)}:- ${it.className}"
                    binding.instituteNameTextView.text="${resources.getString(R.string.institute)}:- ${it.instituteName}"
                }
            }

        })
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id) {
                R.id.signOutButton -> {
                    Coroutines.io {
                        SharedPreUtils.setBooleanToStorage(this,Constants.loginStatusKey,false)
                        viewModel.deleteUserInfo()
                        startActivity(Intent(this,LoginActivity::class.java))
                        finishAffinity()
                    }
                }
                else -> {

                }
            }
        }
    }
}