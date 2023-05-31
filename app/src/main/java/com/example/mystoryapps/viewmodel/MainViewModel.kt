package com.example.mystoryapps.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mystoryapps.db.MyPreferences
import kotlinx.coroutines.launch

class MainViewModel(private val pref: MyPreferences): ViewModel(){
    fun getSession(): LiveData<Boolean> {
        return pref.getSession().asLiveData()
    }

    fun setSession(loginState: Boolean) {
        viewModelScope.launch {
            pref.setSession(loginState)
        }
    }

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    fun saveToken(token: String) {
        viewModelScope.launch {
            pref.saveToken(token)
        }
    }

    fun getName(): LiveData<String> {
        return pref.getName().asLiveData()
    }

    fun saveName(token: String) {
        viewModelScope.launch {
            pref.saveName(token)
        }
    }
}