package com.example.mystoryapps.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mystoryapps.db.StoryRepository
import com.example.mystoryapps.response.ListStoryItem
import com.example.mystoryapps.response.Story

class HomeViewModel(private val storyRepository: StoryRepository): ViewModel() {

    val detailStory: LiveData<Story> = storyRepository.detailStory

    val isLoading: LiveData<Boolean> = storyRepository.isLoading

    fun getStories(token: String): LiveData<PagingData<ListStoryItem>> =
        storyRepository.getStories(token).cachedIn(viewModelScope)

    fun checkStories(token: String): LiveData<PagingData<ListStoryItem>> {
        val pagingData = storyRepository.getStories(token)
            .cachedIn(viewModelScope)

        pagingData.observeForever { data ->
            Log.e("HomeViewModel", "data: $data")
        }
        return pagingData
    }

    fun getStory(token: String, id: String) {
        storyRepository.getStory(token, id)
    }

    fun getAllStories(token: String): LiveData<List<ListStoryItem>> {
        return storyRepository.getListStory(token)
    }
}