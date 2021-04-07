package habibur.rahman.spark.tuition.ui.forgot_password

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.lifecycle.ViewModelProvider
import co.nedim.maildroidx.MaildroidX
import co.nedim.maildroidx.MaildroidXType
import com.github.ybq.android.spinkit.sprite.CircleSprite
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.FadingCircle
import com.google.gson.JsonElement
import habibur.rahman.spark.tuition.BuildConfig
import habibur.rahman.spark.tuition.R
import habibur.rahman.spark.tuition.databinding.ActivityForgotPasswordBinding
import habibur.rahman.spark.tuition.model.UserInfoModel
import habibur.rahman.spark.tuition.ui.MyApplication
import habibur.rahman.spark.tuition.ui.login.LoginActivity
import habibur.rahman.spark.tuition.ui.login.LoginActivityViewModel
import habibur.rahman.spark.tuition.ui.login.LoginActivityViewModelFactory
import habibur.rahman.spark.tuition.ui.main.MainActivity
import habibur.rahman.spark.tuition.utils.Constants
import habibur.rahman.spark.tuition.utils.MyExtension.shortMessage
import habibur.rahman.spark.tuition.utils.MyExtension.showDialog
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var viewModel: ForgotPasswordActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityForgotPasswordBinding.inflate(layoutInflater)
        val factory: ForgotPasswordActivityViewModelFactory = ForgotPasswordActivityViewModelFactory((application as MyApplication).appDatabase.userInfoDao())
        viewModel= ViewModelProvider(this, factory).get(ForgotPasswordActivityViewModel::class.java)
        setContentView(binding.root)


        initAll()




    }



    private fun initAll() {
        val fadingCircle: Sprite = FadingCircle()
        binding.forgotPasswordSpinKit.setIndeterminateDrawable(fadingCircle)
        binding.forgotPasswordSpinKit.visibility=View.GONE
        binding.forgotPasswordButton.setOnClickListener(this)
    }

    private fun checkUserExistence() {
        val inputtedEmail: String=binding.forgotPasswordEmailEditText.text.toString().trim()
        if (inputtedEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(inputtedEmail).matches()) {
            binding.forgotPasswordEmailEditText.error=resources.getString(R.string.input_valid_email)
            binding.forgotPasswordEmailEditText.isFocusable=true
            return
        }
        binding.forgotPasswordSpinKit.visibility = View.VISIBLE
        val response: Call<JsonElement> =viewModel.getUserInfoFromNetwork(inputtedEmail)
        response.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful && response.code() == 200) {
                    val rootArray: JSONArray = JSONArray(response.body().toString())
                    if (rootArray.getJSONObject(0).getString("status").equals("success", false)) {
                        val userObject = rootArray.getJSONObject(1)

                        val email: String = userObject.getJSONObject("message").getString("Email")
                        val password: String = userObject.getJSONObject("message").getString("Password")
                        val activeStatus: String = userObject.getJSONObject("message").getString("ActiveStatus")
                        val name: String = userObject.getJSONObject("message").getString("Name")

                        if (activeStatus.equals("true", false)) {
                            shortMessage(resources.getString(R.string.sending_reset_mail))
                            sendMail(email,"<p> ${resources.getString(R.string.hello)} $name, <br> ${resources.getString(R.string.your_current_password)} <h1><b>'$password'</b></h1> <br> Keep your password a secret and do not share your password with anyone for security. <br><br> Thanks, <br> Your Spark Tuition team. <br></p>")
                        } else {
                            binding.forgotPasswordSpinKit.visibility=View.GONE
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
                        binding.forgotPasswordSpinKit.visibility=View.GONE
                        shortMessage("${resources.getString(R.string.failed_for)} ${rootArray.getJSONObject(1).getString("message")}")
                    }
                } else {
                    binding.forgotPasswordSpinKit.visibility=View.GONE
                    shortMessage(resources.getString(R.string.failed_to_send_password_reset_mail))
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                binding.forgotPasswordSpinKit.visibility = View.GONE
                shortMessage("${resources.getString(R.string.login_failed_with_clone)} ${t.message}")
            }

        })
    }

    private fun sendMail(to: String, message: String) {
        MaildroidX.Builder()
            .smtp(BuildConfig.SMTP)
            .smtpUsername(BuildConfig.SMTP_EMAIL)
            .smtpPassword(BuildConfig.SMTP_PASSWORD)
            .port(BuildConfig.SMTP_PORT)
            .type(MaildroidXType.HTML)
            .to(to)
            .from(BuildConfig.SMTP_EMAIL)
            .subject(resources.getString(R.string.password_reset_request))
            .body(message)
            .isStartTLSEnabled(true)
            .isJavascriptDisabled(true)
            .onCompleteCallback(object : MaildroidX.onCompleteCallback{
                override val timeout: Long = 3000
                override fun onSuccess() {
                    binding.forgotPasswordSpinKit.visibility=View.GONE
                    shortMessage(resources.getString(R.string.forgot_mail_sent))
                    startActivity(Intent(this@ForgotPasswordActivity, LoginActivity::class.java))
                    finish()
                }
                override fun onFail(errorMessage: String) {
                    binding.forgotPasswordSpinKit.visibility=View.GONE
                    shortMessage("${resources.getString(R.string.failed_for)} $errorMessage")
                }
            })
            .mail()
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when(it.id) {
                R.id.forgotPasswordButton -> checkUserExistence()
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ForgotPasswordActivity, LoginActivity::class.java))
        finish()
    }

}