package com.dimasnoufal.githubuser.ui

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
import com.dimasnoufal.githubuser.databinding.ActivityDetailBinding
import com.dimasnoufal.githubuser.model.ItemsItem
import com.dimasnoufal.githubuser.utils.AppConstant
import com.dimasnoufal.githubuser.viewmodels.DetailUserViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailUserViewModel>()
    private lateinit var userId: ItemsItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail Github User"

        val sectionsPagerAdapter = SectionsPagerAdapter(this@DetailActivity)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        val user = intent.getParcelableExtra<ItemsItem>(AppConstant.EXTRA_USER)
        detailViewModel.getDetailUser(user!!.login!!)
        userId = user

        detailViewModel.userDetail.observe(this@DetailActivity) { res ->
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
                    Toast.makeText(this@DetailActivity, res.errorMessage, Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Success -> {
                    val result = res.data!!
                    binding.apply {
                        Glide.with(this@DetailActivity)
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
        }

        isFavoriteUser(user)
    }

    private fun isFavoriteUser(userSelected: ItemsItem) {
        detailViewModel.favoriteUserList.observe(this@DetailActivity) { res ->
            val user = res.find { fav ->
                fav.user.id == userSelected.id
            }
            if (user != null) {
                binding.btnFavorite.apply {
                    setText(R.string.remove_from_favorite)
                    setBackgroundColor(
                        ContextCompat.getColor(
                            this@DetailActivity,
                            R.color.red
                        )
                    )
                    setOnClickListener {
                        deleteFavoriteMeal(user.id)
                    }
                }
            } else {
                binding.btnFavorite.apply {
                    setText(R.string.add_to_favorite)
                    setBackgroundColor(
                        ContextCompat.getColor(
                            this@DetailActivity,
                            R.color.purple_500
                        )
                    )
                    setOnClickListener {
                        insertFavoriteGame()
                    }
                }
            }
        }
    }

    private fun deleteFavoriteMeal(userEntityId: Int) {
        val userEntity = UserEntity(userEntityId, userId)
        detailViewModel.deleteFavoriteUser(userEntity)
        Toast.makeText(this, "Successfully remove from favorite", Toast.LENGTH_SHORT).show()
    }

    private fun insertFavoriteGame() {
        val userEntity = UserEntity(user = userId)
        detailViewModel.insertFavoriteUser(userEntity)
        Toast.makeText(this, "Successfully added to favorite", Toast.LENGTH_SHORT).show()
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