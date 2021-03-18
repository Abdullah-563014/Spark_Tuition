package habibur.rahman.spark.tuition.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import habibur.rahman.spark.tuition.R
import habibur.rahman.spark.tuition.databinding.ActivityMainBinding
import habibur.rahman.spark.tuition.model.UserInfoModel
import habibur.rahman.spark.tuition.ui.MyApplication
import habibur.rahman.spark.tuition.ui.class_list.ClassListActivity
import habibur.rahman.spark.tuition.ui.tutors_list.TutorsListActivity
import habibur.rahman.spark.tuition.utils.Constants

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initAll()


    }


    private fun initAll() {
        binding.ourTutorsButton.setOnClickListener(this)
        binding.studentsButton.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when(it.id) {
                R.id.ourTutorsButton -> startActivity(Intent(this,TutorsListActivity::class.java))
                R.id.studentsButton -> startActivity(Intent(this,ClassListActivity::class.java))
            }
        }
    }



}