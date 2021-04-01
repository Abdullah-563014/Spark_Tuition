package habibur.rahman.spark.tuition.ui.previous_class_list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.FadingCircle
import com.google.gson.Gson
import com.google.gson.JsonElement
import habibur.rahman.spark.tuition.R
import habibur.rahman.spark.tuition.databinding.ActivityPreviousClassListBinding
import habibur.rahman.spark.tuition.model.SavedMediaLinkModel
import habibur.rahman.spark.tuition.model.TutorInfoModel
import habibur.rahman.spark.tuition.model.VideoModel
import habibur.rahman.spark.tuition.network_database.MyApi
import habibur.rahman.spark.tuition.utils.Constants
import habibur.rahman.spark.tuition.utils.Coroutines
import habibur.rahman.spark.tuition.utils.MyExtension.shortMessage
import habibur.rahman.spark.tuition.utils.SharedPreUtils
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PreviousClassListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreviousClassListBinding
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: PreviousClassListAdapter
    private var list: MutableList<VideoModel> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPreviousClassListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initAll()

        setUpRecyclerView()

        loadVideoInfo()



    }


    private fun initAll() {
        val fadingCircle: Sprite = FadingCircle()
        binding.previousClassListSpinKit.setIndeterminateDrawable(fadingCircle)
        binding.previousClassListSpinKit.visibility=View.GONE

    }

    private fun setUpRecyclerView() {
        layoutManager= LinearLayoutManager(this)
        adapter= PreviousClassListAdapter(this,list)
        binding.previousClassListRecyclerView.layoutManager=layoutManager
        binding.previousClassListRecyclerView.adapter=adapter
    }

    private fun loadVideoInfo() {
        binding.previousClassListSpinKit.visibility=View.VISIBLE
        val call: Call<JsonElement> =MyApi.invoke().getPreviousVideoList()
        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                binding.previousClassListSpinKit.visibility=View.GONE
                if (response.isSuccessful && response.code()==200) {
                    val rootArray: JSONArray = JSONArray(response.body().toString())
                    if (rootArray.getJSONObject(0).getString("status").equals("success", false)) {
                        val videoObject = rootArray.getJSONObject(1)
                        val innerArray: JSONArray = videoObject.getJSONArray("message")
                        for (i in 0 until innerArray.length()) {
                            val temporaryJsonObject: JSONObject =innerArray.getJSONObject(i)
                            val videoModel: VideoModel = VideoModel(temporaryJsonObject.getString("Title"), temporaryJsonObject.getString("VideoUrl"), temporaryJsonObject.getString("Duration"), temporaryJsonObject.getString("TimeStamp"), temporaryJsonObject.getString("IsLiveVideo"))
                            list.add(videoModel)
                            savePreviousClassListToStorage(list)
                        }
                        adapter.notifyDataSetChanged()
                    } else {
                        shortMessage(rootArray.getJSONObject(1).getString("message"))
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                binding.previousClassListSpinKit.visibility=View.GONE
                shortMessage("${resources.getString(R.string.failed_for)} ${t.message}")
            }

        })
    }

    private fun savePreviousClassListToStorage(classList: MutableList<VideoModel>) {
        val savedMediaLinkModel: SavedMediaLinkModel= SavedMediaLinkModel(classList)
        val gson: Gson = Gson()
        val stringData: String=gson.toJson(savedMediaLinkModel)
        Coroutines.io {
            SharedPreUtils.setStringToStorage(
                applicationContext,
                Constants.savedMediaLink,
                stringData
            )
        }
    }





}