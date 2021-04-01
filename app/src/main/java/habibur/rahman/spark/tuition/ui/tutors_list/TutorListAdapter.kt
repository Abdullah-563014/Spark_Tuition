package habibur.rahman.spark.tuition.ui.tutors_list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import habibur.rahman.spark.tuition.R
import habibur.rahman.spark.tuition.databinding.TutorListRecyclerViewModelLayoutBinding
import habibur.rahman.spark.tuition.model.TutorInfoModel
import habibur.rahman.spark.tuition.utils.MyExtension.shortMessage

class TutorListAdapter(private val context: Context, private var list: List<TutorInfoModel>): RecyclerView.Adapter<TutorListAdapter.TutorListViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TutorListViewHolder {
        val binding: TutorListRecyclerViewModelLayoutBinding= TutorListRecyclerViewModelLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TutorListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TutorListViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class TutorListViewHolder(binding: TutorListRecyclerViewModelLayoutBinding): RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        private val binding =binding

        fun bind(item: TutorInfoModel) {
            binding.tutorProfileNameTextView.text=item.name
            Glide.with(context).load(item.profilePicture).circleCrop().error(R.drawable.tutor_icon).into(binding.tutorProfileImageView)
            binding.tutorProfileRootLayout.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            p0?.let {
                when(it.id) {
//                    R.id.tutorProfileRootLayout -> context.shortMessage("clicked position:- $adapterPosition")
                }
            }
        }
    }




}