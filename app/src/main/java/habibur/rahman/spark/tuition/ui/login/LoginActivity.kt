package habibur.rahman.spark.tuition.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import co.nedim.maildroidx.MaildroidX
import co.nedim.maildroidx.MaildroidXType
import co.nedim.maildroidx.callback
import co.nedim.maildroidx.sendEmail
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.FadingCircle
import com.google.gson.JsonElement
import habibur.rahman.spark.tuition.R
import habibur.rahman.spark.tuition.databinding.ActivityLoginBinding
import habibur.rahman.spark.tuition.model.UserInfoModel
import habibur.rahman.spark.tuition.ui.MyApplication
import habibur.rahman.spark.tuition.ui.forgot_password.ForgotPasswordActivity
import habibur.rahman.spark.tuition.ui.main.MainActivity
import habibur.rahman.spark.tuition.ui.registration.RegistrationActivity
import habibur.rahman.spark.tuition.utils.Constants
import habibur.rahman.spark.tuition.utils.MyExtension.shortMessage
import habibur.rahman.spark.tuition.utils.MyExtension.showDialog
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        val factory: LoginActivityViewModelFactory= LoginActivityViewModelFactory((application as MyApplication).appDatabase.userInfoDao())
        viewModel=ViewModelProvider(this, factory).get(LoginActivityViewModel::class.java)
        setContentView(binding.root)


        initAll()




    }


    private fun initAll() {
        binding.signInButton.setOnClickListener(this)
        binding.doNotHaveAccountTextView.setOnClickListener(this)
        binding.forgotPasswordTextView.setOnClickListener(this)
        val fadingCircle: Sprite=FadingCircle()
        binding.loginSpinKit.setIndeterminateDrawable(fadingCircle)
        binding.loginSpinKit.visibility=View.GONE
    }

    private fun startLoginOperation() {
       val inputtedEmail: String=binding.emailEditText.text.toString().trim()
       val inputtedPassword: String=binding.passwordEditText.text.toString().trim()
        if (inputtedEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(inputtedEmail).matches()) {
            binding.emailEditText.error=resources.getString(R.string.input_valid_email)
            binding.emailEditText.isFocusable = true
            return
        }
        if (inputtedPassword.isEmpty()) {
            binding.passwordEditText.error=resources.getString(R.string.input_correct_password)
            binding.passwordEditText.isFocusable = true
            return
        }
        binding.loginSpinKit.visibility=View.VISIBLE
        val response: Call<JsonElement> =viewModel.getUserInfoFromNetwork(inputtedEmail)
        response.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                binding.loginSpinKit.visibility = View.GONE
                if (response.isSuccessful && response.code() == 200) {
                    val rootArray: JSONArray = JSONArray(response.body().toString())
                    if (rootArray.getJSONObject(0).getString("status").equals("success", false)) {
                        val userObject = rootArray.getJSONObject(1)

                        val name: String = userObject.getJSONObject("message").getString("Name")
                        val phone: String = userObject.getJSONObject("message").getString("Phone")
                        val email: String = userObject.getJSONObject("message").getString("Email")
                        val password: String =
                            userObject.getJSONObject("message").getString("Password")
                        val instituteName: String =
                            userObject.getJSONObject("message").getString("InstituteName")
                        val className: String =
                            userObject.getJSONObject("message").getString("ClassName")
                        val activeStatus: String =
                            userObject.getJSONObject("message").getString("ActiveStatus")
                        val userInfoModel: UserInfoModel = UserInfoModel(
                            1,
                            name,
                            phone,
                            email,
                            password,
                            instituteName,
                            className,
                            activeStatus
                        )

                        if (inputtedPassword.equals(password, false)) {
                            if (activeStatus.equals("true", false)) {
                                viewModel.insert(userInfoModel)
                                shortMessage(resources.getString(R.string.login_successful))
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                finish()
                            } else {
                                if (!isFinishing) {
                                    showDialog(
                                        resources.getString(R.string.account_approval),
                                        resources.getString(
                                            R.string.need_to_approve_account
                                        )
                                    )
                                }
                            }
                        } else {
                            shortMessage(resources.getString(R.string.input_correct_password))
                        }
                    } else {
                        shortMessage(resources.getString(R.string.failed_to_login))
                    }
                } else {
                    shortMessage(resources.getString(R.string.failed_to_login))
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                binding.loginSpinKit.visibility = View.GONE
                shortMessage("${resources.getString(R.string.login_failed_with_clone)} ${t.message}")
            }

        })
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when(it.id) {
                R.id.signInButton -> startLoginOperation()
                R.id.doNotHaveAccountTextView -> {
                    startActivity(
                        Intent(
                            this,
                            RegistrationActivity::class.java
                        )
                    )
                    finish()
                }
                R.id.forgotPasswordTextView -> {
                    startActivity(
                        Intent(
                            this,
                            ForgotPasswordActivity::class.java
                        )
                    )
                    finish()
                }
            }
        }
    }


}