package com.dimasnoufal.githubuser.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.dimasnoufal.githubuser.adapter.FavoriteAdapter
import com.dimasnoufal.githubuser.data.database.UserEntity
import com.dimasnoufal.githubuser.databinding.ActivityFavoriteBinding
import com.dimasnoufal.githubuser.utils.AppConstant
import com.dimasnoufal.githubuser.viewmodels.FavoriteViewModel

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private val favoriteViewModel by viewModels<FavoriteViewModel>()
    private val favoriteAdapter by lazy { FavoriteAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Favorite Github User"

        favoriteViewModel.favoriteUserList.observe(this) { res ->
            if (res.isEmpty()) {
                binding.apply {
                    rvFavorite.isVisible = false
                    ivEmpty.isVisible = true
                    tvError.isVisible = true
                }
            } else {
                binding.rvFavorite.apply {
                    adapter = favoriteAdapter
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(
                        this@FavoriteActivity
                    )
                }
                favoriteAdapter.apply {
                    setData(res)
                    setOnItemClickCallback(object:FavoriteAdapter.IOnFavoriteItemCallBack{
                        override fun onFavoriteItemClickCallback(data: UserEntity) {
                            val detailFavorite = Intent(this@FavoriteActivity, FavoriteDetailActivity::class.java)
                            val datas = data.user
                            detailFavorite.putExtra(AppConstant.EXTRA_USER,datas)
                            startActivity(detailFavorite)
                            finish()
                        }
                    })
                }
            }
        }
    }
}