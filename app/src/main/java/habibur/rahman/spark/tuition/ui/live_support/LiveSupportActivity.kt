package habibur.rahman.spark.tuition.ui.live_support

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.FadingCircle
import com.google.gson.JsonElement
import habibur.rahman.spark.tuition.R
import habibur.rahman.spark.tuition.databinding.ActivityLiveSupportBinding
import habibur.rahman.spark.tuition.model.LiveSupportModel
import habibur.rahman.spark.tuition.model.TutorInfoModel
import habibur.rahman.spark.tuition.network_database.MyApi
import habibur.rahman.spark.tuition.ui.tutors_list.TutorListAdapter
import habibur.rahman.spark.tuition.utils.MyExtension.shortMessage
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LiveSupportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLiveSupportBinding
    private lateinit var adapter: LiveSupportAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var list: MutableList<LiveSupportModel> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLiveSupportBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initAll()

        setUpRecyclerView()

        loadLiveSupportList()




    }


    private fun initAll() {
        val fadingCircle: Sprite = FadingCircle()
        binding.liveSupportListSpinKit.setIndeterminateDrawable(fadingCircle)
        binding.liveSupportListSpinKit.visibility= View.GONE
    }

    private fun setUpRecyclerView() {
        adapter= LiveSupportAdapter(this,list)
        layoutManager= GridLayoutManager(this,2)
        binding.liveSupportListRecyclerView.layoutManager=layoutManager
        binding.liveSupportListRecyclerView.adapter=adapter
    }

    private fun loadLiveSupportList() {
        binding.liveSupportListSpinKit.visibility=View.VISIBLE
        val call: Call<JsonElement> = MyApi.invoke().getLiveSupportList()
        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                binding.liveSupportListSpinKit.visibility=View.GONE
                if (response.isSuccessful && response.code() == 200) {
                    val rootArray: JSONArray = JSONArray(response.body().toString())
                    if (rootArray.getJSONObject(0).getString("status").equals("success", false)) {
                        val userObject = rootArray.getJSONObject(1)
                        val innerArray: JSONArray = userObject.getJSONArray("message")
                        for (i in 0 until innerArray.length()) {
                            val temporaryJsonObject: JSONObject =innerArray.getJSONObject(i)
                            val liveSupportModel: LiveSupportModel = LiveSupportModel(temporaryJsonObject.getString("Name"), temporaryJsonObject.getString("ProfilePhotoUrl"), temporaryJsonObject.getString("Phone"))
                            list.add(i,liveSupportModel)
                        }
                        adapter.notifyDataSetChanged()
                    } else {
                        shortMessage(rootArray.getJSONObject(1).getString("message"))
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                binding.liveSupportListSpinKit.visibility=View.GONE
                shortMessage("${resources.getString(R.string.failed_for)} ${t.message}")
            }

        })
    }




}