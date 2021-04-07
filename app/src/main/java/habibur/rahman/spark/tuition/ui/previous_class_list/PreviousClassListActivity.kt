package habibur.rahman.spark.tuition.ui.previous_class_list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.FadingCircle
import com.google.gson.Gson
import com.google.gson.JsonElement
import habibur.rahman.spark.tuition.R
import habibur.rahman.spark.tuition.databinding.ActivityPreviousClassListBinding
import habibur.rahman.spark.tuition.model.SavedMediaLinkModel
import habibur.rahman.spark.tuition.model.TutorInfoModel
import habibur.rahman.spark.tuition.model.UserInfoModel
import habibur.rahman.spark.tuition.model.VideoModel
import habibur.rahman.spark.tuition.network_database.MyApi
import habibur.rahman.spark.tuition.ui.MyApplication
import habibur.rahman.spark.tuition.ui.class_list.ClassListViewModel
import habibur.rahman.spark.tuition.ui.class_list.ClassListViewModelFactory
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
    private lateinit var viewModel: PreviousClassListViewModel
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: PreviousClassListAdapter
    private var list: MutableList<VideoModel> = mutableListOf()
    private var userInfoModel: UserInfoModel?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPreviousClassListBinding.inflate(layoutInflater)
        val factory: PreviousClassListViewModelFactory = PreviousClassListViewModelFactory((application as MyApplication).appDatabase.userInfoDao())
        viewModel = ViewModelProvider(this, factory).get(PreviousClassListViewModel::class.java)
        setContentView(binding.root)


        initAll()

        loadUserInfo()

        setUpRecyclerView()





    }


    private fun initAll() {
        val fadingCircle: Sprite = FadingCircle()
        binding.previousClassListSpinKit.setIndeterminateDrawable(fadingCircle)
        binding.previousClassListSpinKit.visibility=View.GONE

    }

    private fun loadUserInfo() {
        viewModel.userInfoLiveData.observe(this,object : Observer<UserInfoModel> {
            override fun onChanged(t: UserInfoModel?) {
                t?.let {
                    userInfoModel=it
                    loadVideoInfo()
                }
            }

        })
    }

    private fun setUpRecyclerView() {
        layoutManager= LinearLayoutManager(this)
        adapter= PreviousClassListAdapter(this,list)
        binding.previousClassListRecyclerView.layoutManager=layoutManager
        binding.previousClassListRecyclerView.adapter=adapter
    }

    private fun loadVideoInfo() {
        binding.previousClassListSpinKit.visibility=View.VISIBLE
        userInfoModel?.let {
            val call: Call<JsonElement> =MyApi.invoke().getPreviousVideoList(it.className)
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
                                val lastPartOfUrl=temporaryJsonObject.getString("VideoUrl").substring(8)
                                val videoModel: VideoModel = VideoModel(temporaryJsonObject.getString("Title"), "https://www.$lastPartOfUrl", temporaryJsonObject.getString("Duration"), temporaryJsonObject.getString("TimeStamp"), temporaryJsonObject.getString("IsLiveVideo"))
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