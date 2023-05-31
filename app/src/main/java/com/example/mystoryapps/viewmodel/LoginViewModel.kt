package com.example.mystoryapps.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mystoryapps.db.StoryRepository
import com.example.mystoryapps.response.LoginResponse
import com.example.mystoryapps.response.RequestLogin

class LoginViewModel(private val storyRepository: StoryRepository): ViewModel() {

    val message: LiveData<String> = storyRepository.message

    val dataUser: LiveData<LoginResponse> = storyRepository.dataUser

    val isLoading: LiveData<Boolean> = storyRepository.isLoading

    var isError: Boolean = storyRepository.isError

    fun login(context: Context, requestLogin: RequestLogin) {
        storyRepository.login(context, requestLogin)
    }
}