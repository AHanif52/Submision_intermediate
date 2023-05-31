package com.example.mystoryapps.di

import android.content.Context
import com.example.mystoryapps.db.StoryRepository
import com.example.mystoryapps.db.story.StoryDatabase
import com.example.mystoryapps.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(database, apiService)
    }
}