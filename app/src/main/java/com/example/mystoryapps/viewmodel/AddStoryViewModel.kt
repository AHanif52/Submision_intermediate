package com.example.mystoryapps.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mystoryapps.db.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val storyRepository: StoryRepository): ViewModel(){

    val message: LiveData<String> = storyRepository.message

    val isLoading: LiveData<Boolean> = storyRepository.isLoading

    val isError: LiveData<Boolean> = storyRepository.isEror

    fun uploadImage(
        photo: MultipartBody.Part,
        desc: RequestBody,
        lat : Double,
        lon : Double,
        token: String
    ) {
        storyRepository.uploadImage(photo, desc, lat, lon, token)
    }

}