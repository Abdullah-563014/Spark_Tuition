package habibur.rahman.spark.tuition.ui.live_support

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import habibur.rahman.spark.tuition.R
import habibur.rahman.spark.tuition.databinding.TutorListRecyclerViewModelLayoutBinding
import habibur.rahman.spark.tuition.model.LiveSupportModel
import habibur.rahman.spark.tuition.model.TutorInfoModel
import habibur.rahman.spark.tuition.utils.CommonMethod
import habibur.rahman.spark.tuition.utils.MyExtension.shortMessage

class LiveSupportAdapter(context: Context, list: List<LiveSupportModel>): RecyclerView.Adapter<LiveSupportAdapter.LiveSupportViewHolder>() {

    private val context: Context=context
    private var list: List<LiveSupportModel> =list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LiveSupportViewHolder {
        val binding: TutorListRecyclerViewModelLayoutBinding= TutorListRecyclerViewModelLayoutBinding.inflate(
            LayoutInflater.from(parent.context),parent,false)
        return LiveSupportViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LiveSupportViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class LiveSupportViewHolder(binding: TutorListRecyclerViewModelLayoutBinding): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private val binding=binding

        fun bind(item: LiveSupportModel) {
            binding.tutorProfileNameTextView.text=item.Name
            Glide.with(context).load(item.ProfilePhotoUrl).circleCrop().into(binding.tutorProfileImageView)
            binding.tutorProfileRootLayout.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            p0?.let {
                when(it.id) {
                    R.id.tutorProfileRootLayout -> CommonMethod.openWhatsApp(context,list[adapterPosition].Phone)
                }
            }
        }

    }


}