package com.example.mystoryapps.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryapps.R
import com.example.mystoryapps.adapter.LoadingStateAdapter
import com.example.mystoryapps.adapter.StoryAdapter
import com.example.mystoryapps.databinding.ActivityMainBinding
import com.example.mystoryapps.db.MyPreferences
import com.example.mystoryapps.viewmodel.HomeViewModel
import com.example.mystoryapps.viewmodel.MainViewModel
import com.example.mystoryapps.viewmodel.ViewModelFactory
import com.example.mystoryapps.viewmodel.RepoViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val homeViewModel: HomeViewModel by viewModels {
        RepoViewModelFactory(this)
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(false)

        val pref = MyPreferences.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(this, ViewModelFactory(pref))[MainViewModel::class.java]

        mainViewModel.getToken().observe(this) {
            if (it != null) {
                setUserData(it)
                homeViewModel.getStories(it).observe(this) { pagingData ->
                    Log.d("HomeViewModel", "Data: $pagingData")
                }
            } else  {
                Toast.makeText(this, getString(R.string.token_is_null), Toast.LENGTH_SHORT).show()
                finishAffinity()
            }
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStory.addItemDecoration(itemDecoration)

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }

    }

    @ExperimentalPagingApi
    private fun setUserData(token: String) {
        val adapter = StoryAdapter()
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        homeViewModel.getStories(token).observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.maps -> {
                val intent = Intent(this@MainActivity, MapsActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.lang -> {
                val i = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(i)
                return true
            }
            R.id.logout -> {
                showLogoutDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(this)
        builder
            .setTitle(getString(R.string.logout))
            .setMessage(getString(R.string.alert_logout))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                logout()
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun logout() {
        showLoading(true)
        val pref = MyPreferences.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(this, ViewModelFactory(pref))[MainViewModel::class.java]
        mainViewModel.apply {
            setSession(false)
            saveToken("")
            saveName("")
        }
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
        finishAffinity()
        showLoading(false)
    }
}