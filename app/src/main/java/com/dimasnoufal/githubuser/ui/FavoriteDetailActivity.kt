package com.dimasnoufal.githubuser.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dimasnoufal.githubuser.R
import com.dimasnoufal.githubuser.adapter.SectionsPagerAdapter
import com.dimasnoufal.githubuser.data.database.UserEntity
import com.dimasnoufal.githubuser.data.network.handler.NetworkResult
import com.dimasnoufal.githubuser.databinding.ActivityFavoriteDetailBinding
import com.dimasnoufal.githubuser.model.ItemsItem
import com.dimasnoufal.githubuser.utils.AppConstant
import com.dimasnoufal.githubuser.viewmodels.FavoriteDetailViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class FavoriteDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteDetailBinding
    private val favoriteDetailViewModel by viewModels<FavoriteDetailViewModel>()
    private lateinit var userId: ItemsItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Favorite Detail Github User"

        val sectionsPagerAdapter = SectionsPagerAdapter(this@FavoriteDetailActivity)
        val viewPager: ViewPager2 = findViewById(R.id.view_page_favoriter)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs_favorite)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(FavoriteDetailActivity.TAB_TITLES[position])
        }.attach()

        val user = intent.getParcelableExtra<ItemsItem>(AppConstant.EXTRA_USER)
        favoriteDetailViewModel.getDetailUser(user!!.login!!)
        userId = user

        favoriteDetailViewModel.userDetail.observe(this@FavoriteDetailActivity) { res ->
            when (res) {
                is NetworkResult.Loading -> {
                    handleUi(
                        clwrapper = false,
                        progressbar = true,
                        tverror = false,
                        iverror = false
                    )
                }
                is NetworkResult.Error -> {
                    binding.tvError.text = res.errorMessage
                    handleUi(
                        clwrapper = false,
                        progressbar = false,
                        tverror = true,
                        iverror = true
                    )
                    Toast.makeText(this@FavoriteDetailActivity, res.errorMessage, Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Success -> {
                    val result = res.data!!
                    binding.apply {
                        Glide.with(this@FavoriteDetailActivity)
                            .load(result.avatarUrl)
                            .error(R.drawable.img_placeholder)
                            .into(ivPoster)
                        tvName.text = result.login
                        tvUsername.text = result.name
                        tvFollowers.text = result.followers.toString()
                        tvFollowing.text = result.following.toString()
                    }
                    handleUi(
                        clwrapper = true,
                        progressbar = false,
                        tverror = false,
                        iverror = false
                    )
                }
            }

            isFavoriteUser(user)
        }

    }

    private fun isFavoriteUser(userSelected: ItemsItem) {
        favoriteDetailViewModel.favoriteUserList.observe(this@FavoriteDetailActivity) { res ->
            val user = res.find { fav ->
                fav.user.id == userSelected.id
            }
            if (user != null) {
                binding.btnRemoveFavorite.apply {
                    setText(R.string.remove_from_favorite)
                    setBackgroundColor(
                        ContextCompat.getColor(
                            this@FavoriteDetailActivity,
                            R.color.red
                        )
                    )
                    setOnClickListener {
                        deleteFavoriteMeal(user.id)
                    }
                }
            }
        }
    }

    private fun deleteFavoriteMeal(userEntityId: Int) {
        val userEntity = UserEntity(userEntityId, userId)
        favoriteDetailViewModel.deleteFavoriteUser(userEntity)
        val delete = Intent(this, FavoriteActivity::class.java)
        startActivity(delete)
        finish()
        Toast.makeText(this, "Successfully remove from favorite", Toast.LENGTH_SHORT).show()

    }

    private fun handleUi(
        clwrapper: Boolean,
        progressbar: Boolean,
        tverror: Boolean,
        iverror: Boolean
    ) {
        binding.apply {
            clWrapper.isVisible = clwrapper
            progressBar2.isVisible = progressbar
            tvError.isVisible = tverror
            ivError.isVisible = iverror
        }
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}