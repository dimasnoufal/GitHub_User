package com.dimasnoufal.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dimasnoufal.githubuser.R
import com.dimasnoufal.githubuser.databinding.ItemRowUserBinding
import com.dimasnoufal.githubuser.model.DetailUserResponse

class FollowAdapter : RecyclerView.Adapter<FollowAdapter.ViewHolder>() {

    private val diffCallBack = object : DiffUtil.ItemCallback<DetailUserResponse>() {
        override fun areItemsTheSame(oldItem: DetailUserResponse, newItem: DetailUserResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DetailUserResponse, newItem: DetailUserResponse): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallBack)

    inner class ViewHolder(private val binding: ItemRowUserBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DetailUserResponse) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(data.avatarUrl)
                    .error(R.drawable.img_placeholder)
                    .into(ivPoster)
                tvUser.text = data.login
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val rowBinding = ItemRowUserBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(rowBinding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemData = differ.currentList[position]
        holder.bind(itemData)
    }

    fun setData(list: List<DetailUserResponse?>?) {
        differ.submitList(list)
    }

}