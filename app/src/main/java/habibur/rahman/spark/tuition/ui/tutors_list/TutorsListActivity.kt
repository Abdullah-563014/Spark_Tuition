package habibur.rahman.spark.tuition.ui.tutors_list

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.FadingCircle
import com.google.gson.JsonElement
import habibur.rahman.spark.tuition.R
import habibur.rahman.spark.tuition.databinding.ActivityTutorsListBinding
import habibur.rahman.spark.tuition.model.TutorInfoModel
import habibur.rahman.spark.tuition.network_database.MyApi
import habibur.rahman.spark.tuition.utils.Constants
import habibur.rahman.spark.tuition.utils.MyExtension.shortMessage
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TutorsListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTutorsListBinding
    private lateinit var adapter: TutorListAdapter
    private lateinit var layoutManager: GridLayoutManager
    private var list: MutableList<TutorInfoModel> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityTutorsListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initAll()

        setUpRecyclerView()

        loadTutorList()

    }


    private fun initAll() {
        val fadingCircle: Sprite = FadingCircle()
        binding.tutorListSpinKit.setIndeterminateDrawable(fadingCircle)
        binding.tutorListSpinKit.visibility= View.GONE
    }

    private fun setUpRecyclerView() {
        adapter= TutorListAdapter(this,list)
        layoutManager= GridLayoutManager(this,2)
        binding.tutorListRecyclerView.layoutManager=layoutManager
        binding.tutorListRecyclerView.adapter=adapter
    }

    private fun loadTutorList() {
        binding.tutorListSpinKit.visibility=View.VISIBLE
        val call: Call<JsonElement> =MyApi.invoke().getTutorList()
        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                binding.tutorListSpinKit.visibility=View.GONE
                if (response.isSuccessful && response.code() == 200) {
                    val rootArray: JSONArray = JSONArray(response.body().toString())
                    if (rootArray.getJSONObject(0).getString("status").equals("success", false)) {
                        val userObject = rootArray.getJSONObject(1)
                        val innerArray: JSONArray = userObject.getJSONArray("message")
                        for (i in 0 until innerArray.length()) {
                            val temporaryJsonObject: JSONObject=innerArray.getJSONObject(i)
                            val lastPartOfUrl=temporaryJsonObject.getString("ProfilePhotoUrl").substring(8)
                            val tutorInfoModel: TutorInfoModel = TutorInfoModel("https://www.$lastPartOfUrl", temporaryJsonObject.getString("Name"))
                            list.add(i,tutorInfoModel)
                        }
                        adapter.notifyDataSetChanged()
                    } else {
                        shortMessage(rootArray.getJSONObject(1).getString("message"))
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                binding.tutorListSpinKit.visibility=View.GONE
                shortMessage("${resources.getString(R.string.failed_for)} ${t.message}")
            }

        })
    }





}