package habibur.rahman.spark.tuition.ui.registration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.CompoundButton
import androidx.lifecycle.ViewModelProvider
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.FadingCircle
import com.google.gson.JsonElement
import habibur.rahman.spark.tuition.R
import habibur.rahman.spark.tuition.databinding.ActivityRegistrationBinding
import habibur.rahman.spark.tuition.ui.MyApplication
import habibur.rahman.spark.tuition.ui.login.LoginActivity
import habibur.rahman.spark.tuition.utils.CommonMethod
import habibur.rahman.spark.tuition.utils.CommonMethod.openUrl
import habibur.rahman.spark.tuition.utils.MyExtension.shortMessage
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var viewModel: RegistrationActivityViewModel
    private var checkStatus: Boolean=false
    private val options: ArrayList<String> = ArrayList()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegistrationBinding.inflate(layoutInflater)
        val factory: RegistrationActivityViewModelFactory= RegistrationActivityViewModelFactory((application as MyApplication).appDatabase.userInfoDao())
        viewModel=ViewModelProvider(this,factory).get(RegistrationActivityViewModel::class.java)
        setContentView(binding.root)



        for (i in 3..6) {
            options.add("Year $i")
        }
        binding.classEditText.setOptions(options)

        initAll()


    }


    private fun initAll() {
        binding.signUpButton.setOnClickListener(this)
        binding.haveAnAccountTextView.setOnClickListener(this)
        binding.termsAndConditionTextView.setOnClickListener(this)
        val fadingCircle: Sprite = FadingCircle()
        binding.registrationSpinKit.setIndeterminateDrawable(fadingCircle)
        binding.registrationSpinKit.visibility=View.GONE
        binding.termsAndConditionCheckBox.setOnCheckedChangeListener { p0, p1 ->
            p0?.let {
                checkStatus = p1
            }
        }
    }

    private fun startingOperation() {
        val name: String=binding.nameEditText.text.toString().trim()
        val phone: String=binding.phoneEditText.text.toString().trim()
        val email: String=binding.emailEditText.text.toString().trim()
        val instituteName: String=binding.instituteEditText.text.toString().trim()
        val className: String=binding.classEditText.text.toString().trim()
        val password: String=binding.passwordEditText.text.toString().trim()
        val confirmPassword: String=binding.confirmPasswordEditText.text.toString().trim()
        if (name.isEmpty()) {
            binding.nameEditText.error=resources.getString(R.string.input_your_name)
            binding.nameEditText.isFocusable = true
            return
        }
        if (phone.isEmpty() || !Patterns.PHONE.matcher(phone).matches() || phone.length!=11) {
            binding.phoneEditText.error=resources.getString(R.string.input_valid_phone)
            binding.phoneEditText.isFocusable = true
            return
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailEditText.error=resources.getString(R.string.input_valid_email)
            binding.emailEditText.isFocusable = true
            return
        }
        if (instituteName.isEmpty()) {
            binding.instituteEditText.error=resources.getString(R.string.input_institute_name)
            binding.instituteEditText.isFocusable = true
            return
        }
        if (className.isEmpty() || className.equals(resources.getString(R.string.input_class_name),true)) {
            binding.classEditText.error=resources.getString(R.string.input_class_name)
            binding.classEditText.isFocusable = true
            return
        }
        if (password.isEmpty()) {
            binding.passwordEditText.error=resources.getString(R.string.input_correct_password)
            binding.passwordEditText.isFocusable = true
            return
        }
        if (password.length<8) {
            binding.passwordEditText.error=resources.getString(R.string.input_minimum_eight_digit_password)
            binding.passwordEditText.isFocusable = true
            return
        }
        if (confirmPassword.isEmpty() || !password.equals(confirmPassword,false)) {
            binding.confirmPasswordEditText.error=resources.getString(R.string.confirm_password_not_matching)
            binding.confirmPasswordEditText.isFocusable = true
            return
        }
        if (!checkStatus) {
            shortMessage(resources.getString(R.string.need_to_accept_terms_and_condition))
            return
        }
        binding.registrationSpinKit.visibility=View.VISIBLE
        val call: Call<JsonElement> = viewModel.createNewAccount(name,phone,email,CommonMethod.encrypt(password),instituteName,className,"false")
        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                binding.registrationSpinKit.visibility=View.GONE
                if (response.isSuccessful && response.code()==200) {
                    val rootArray = JSONArray(response.body().toString())
                    shortMessage(rootArray.getJSONObject(1).getString("message"))
                    if (rootArray.getJSONObject(0).getString("status").equals("success",false)) {
                        startActivity(Intent(this@RegistrationActivity,LoginActivity::class.java))
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                binding.registrationSpinKit.visibility=View.GONE
                shortMessage("${resources.getString(R.string.failed_for)} ${CommonMethod.filterMessage(t.message)}")
            }

        })



    }

    override fun onClick(p0: View?) {
        p0?.let {
            when(it.id) {
                R.id.signUpButton -> startingOperation()
                R.id.haveAnAccountTextView -> {
                    startActivity(Intent(this,LoginActivity::class.java))
                    finish()
                }
                R.id.termsAndConditionTextView -> openUrl(this,resources.getString(R.string.terms_and_condition_url))
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }


}