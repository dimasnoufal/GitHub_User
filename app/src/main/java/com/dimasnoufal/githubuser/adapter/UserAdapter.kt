package com.dimasnoufal.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dimasnoufal.githubuser.R
import com.dimasnoufal.githubuser.databinding.ItemRowUserBinding
import com.dimasnoufal.githubuser.model.ItemsItem

class UserAdapter : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private val diffCallBack = object : DiffUtil.ItemCallback<ItemsItem>() {
        override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
            return oldItem.id!! == newItem.id!!
        }

        override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallBack)
    private lateinit var onItemCallBack: IOnItemCallBack

    inner class ViewHolder(private val binding: ItemRowUserBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ItemsItem) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(data.avatarUrl)
                    .error(R.drawable.img_placeholder)
                    .into(ivPoster)
                tvUser.text = data.login
                clRoot.setOnClickListener { onItemCallBack.onItemClickCallback(data)
                }
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

    fun setData(list: List<ItemsItem?>?) {
        differ.submitList(list)
    }

    fun setOnItemClickCallback(action: IOnItemCallBack) {
        this.onItemCallBack = action
    }

    interface IOnItemCallBack {
        fun onItemClickCallback(data: ItemsItem)
    }

}