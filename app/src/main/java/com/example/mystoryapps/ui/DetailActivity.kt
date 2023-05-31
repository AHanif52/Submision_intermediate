package com.example.mystoryapps.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mystoryapps.R
import com.example.mystoryapps.databinding.ActivityDetailBinding
import com.example.mystoryapps.db.MyPreferences
import com.example.mystoryapps.viewmodel.HomeViewModel
import com.example.mystoryapps.viewmodel.MainViewModel
import com.example.mystoryapps.viewmodel.ViewModelFactory
import com.example.mystoryapps.response.Story
import com.example.mystoryapps.viewmodel.RepoViewModelFactory

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_STORY = "key_story"
    }

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: HomeViewModel by viewModels{
        RepoViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = MyPreferences.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(this, ViewModelFactory(pref))[MainViewModel::class.java]

        val storyList = intent.getStringExtra(EXTRA_STORY)

        mainViewModel.getToken().observe(this) { token ->
            storyList?.let { storyList ->
                detailViewModel.getStory(token, storyList)
            }
        }

        detailViewModel.detailStory.observe(this) {
            setUserData(it)
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setUserData(data: Story) {
        binding.apply {
            Glide.with(this@DetailActivity)
                .load(data.photoUrl)
                .apply(RequestOptions.overrideOf(500, 500))
                .apply(RequestOptions().centerCrop())
                .placeholder(R.drawable.baseline_face_24)
                .into(imgAvatar)
            tvName.text = data.name
            tvDesc.text = data.description
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}