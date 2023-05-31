package com.example.mystoryapps.viewmodel


import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mystoryapps.db.StoryRepository
import com.example.mystoryapps.response.RegResponse
import com.example.mystoryapps.response.RequestReg

class RegisViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    val dataUser: LiveData<RegResponse> = storyRepository.regUser

    val isLoading: LiveData<Boolean> = storyRepository.isLoading

    var isError: Boolean = storyRepository.isError

    fun regis(context: Context, requestReg: RequestReg) {
        storyRepository.regis(context, requestReg)
    }
}