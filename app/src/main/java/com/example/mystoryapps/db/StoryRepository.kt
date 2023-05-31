package com.example.mystoryapps.db

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.example.mystoryapps.data.StoryRemoteMediator
import com.example.mystoryapps.db.story.StoryDatabase
import com.example.mystoryapps.response.*
import com.example.mystoryapps.retrofit.ApiConfig
import com.example.mystoryapps.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryRepository(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService,
){
    private var _regUser = MutableLiveData<RegResponse>()
    val regUser: LiveData<RegResponse> = _regUser

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _dataUser = MutableLiveData<LoginResponse>()
    val dataUser: LiveData<LoginResponse> = _dataUser

    private val _detailStory = MutableLiveData<Story>()
    val detailStory: LiveData<Story> = _detailStory

    private val _isEror = MutableLiveData<Boolean>()
    val isEror: LiveData<Boolean> = _isEror

    var isError: Boolean = false

    fun regis(context: Context, requestReg: RequestReg) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().regis(requestReg)
        client.enqueue(object : Callback<RegResponse> {
            override fun onResponse(
                call: Call<RegResponse>,
                response: Response<RegResponse>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful) {
                    if (responseBody != null) {
                        isError = responseBody.error
                        _regUser.value = responseBody!!
                        Log.d(ContentValues.TAG, "onResponse: ${response.body()}")
                    }
                } else{
                    isError = responseBody!!.error
                    Log.e(ContentValues.TAG, "onResponse: ${response.body()}")
                }
            }

            override fun onFailure(call: Call<RegResponse>, t: Throwable) {
                _isLoading.value = false
                isError = true
                Toast.makeText(context, "onFailure: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun login(context: Context, requestLogin: RequestLogin) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().login(requestLogin)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful) {
                    if (responseBody != null) {
                        isError = responseBody.error
                        _dataUser.value = responseBody!!
                        _message.value = responseBody.message
                    }
                } else{
                    isError = true
                    _message.value = response.message()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                isError = true
                _message.value = t.message.toString()
                Toast.makeText(context, "onFailure: ${_message.value}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getStories(token: String) : LiveData<PagingData<ListStoryItem>>{
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, token),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStories()
            }
        ).liveData
    }

    fun getListStory(token: String,  location : Int = 1): LiveData<List<ListStoryItem>> {
        val story = MutableLiveData<List<ListStoryItem>>()
        _isLoading.value = true
        val client = ApiConfig.getApiService().getAllStory(location,"Bearer $token")
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(
                call: Call<StoryResponse>,
                response: Response<StoryResponse>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful) {
                    if (responseBody != null) {
                        story.value = responseBody.listStory!!
                    }
                } else{
                    Log.e(TAG, "onResponse: ${response.body()}")
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
        return story
    }

    fun getStory(token: String, id: String = "") {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getStory("Bearer $token", id)
        client.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful) {
                    if (responseBody != null) {
                        _detailStory.value = responseBody.story
                    }
                } else{
                    Log.e(TAG, "onResponse: ${response.body()}")
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun uploadImage(photo: MultipartBody.Part, desc: RequestBody, latitude: Double, longitude: Double , token: String) {
        _isLoading.value = true
        val service = ApiConfig.getApiService().uploadImage("Bearer $token", photo, desc, latitude, longitude)
        service.enqueue(object : Callback<FileUploadResponse> {
            override fun onResponse(
                call: Call<FileUploadResponse>,
                response: Response<FileUploadResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        _isEror.value = responseBody.error
                        _message.value = responseBody.message
                    }
                } else {
                    _message.value = response.message()
                    _isEror.value = true
                }
            }

            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                _isLoading.value = false
                _isEror.value = true
                _message.value = t.message.toString()
            }
        })
    }

    companion object{
        private const val TAG = "HomeViewModel"
    }
}
