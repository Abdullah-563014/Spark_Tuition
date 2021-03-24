package habibur.rahman.spark.tuition.ui.previous_class_list

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import habibur.rahman.spark.tuition.R
import habibur.rahman.spark.tuition.databinding.PreviousClassRecyclerViewModelBinding
import habibur.rahman.spark.tuition.model.VideoModel
import habibur.rahman.spark.tuition.ui.player.PlayerActivity
import habibur.rahman.spark.tuition.utils.Constants
import habibur.rahman.spark.tuition.utils.MyExtension.shortMessage

class PreviousClassListAdapter(context: Context, list: List<VideoModel>): RecyclerView.Adapter<PreviousClassListAdapter.PreviousClassListViewHolder>() {

    private var context: Context=context
    private var list: List<VideoModel> =list


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviousClassListViewHolder {
        val binding: PreviousClassRecyclerViewModelBinding= PreviousClassRecyclerViewModelBinding.inflate(
            LayoutInflater.from(
                parent.context
            ), parent, false
        )
        return PreviousClassListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PreviousClassListViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }


    inner class PreviousClassListViewHolder(binding: PreviousClassRecyclerViewModelBinding): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private val binding: PreviousClassRecyclerViewModelBinding=binding

        fun bind(item: VideoModel) {
            binding.videoTitleTextView.text=item.Title
            binding.videoTimeStampTextView.text=item.TimeStamp
            binding.videoDurationTextView.text=secondToTimeConverter(item.Duration)

            binding.videoRecyclerRootLayout.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val previousVideoIntent: Intent= Intent(context,PlayerActivity::class.java)
            previousVideoIntent.putExtra(Constants.videoPlayModeIsLive,false)
            p0?.let {
                when(it.id) {
                    R.id.videoRecyclerRootLayout -> {
                        previousVideoIntent.putExtra(Constants.videoUrl,list[adapterPosition].VideoUrl)
                        context.startActivity(previousVideoIntent)
                    }
                }
            }
        }

    }


    private fun secondToTimeConverter(seconds: String): String {
        val hours: Int= (seconds.toInt())/3600
        val minutes: Int=(seconds.toInt()%3600)/60
        val second: Int=seconds.toInt()%60
        return "${twoDigitString(hours)}:${twoDigitString(minutes)}:${twoDigitString(second)}"
    }

    private fun twoDigitString(number: Int): String {
        if (number == 0) {
            return "00"
        }
        return if (number / 10 == 0) {
            "0$number"
        } else number.toString()
    }


}