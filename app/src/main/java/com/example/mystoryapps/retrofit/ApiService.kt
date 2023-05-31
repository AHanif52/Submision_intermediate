package com.example.mystoryapps.retrofit

import com.example.mystoryapps.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("login")
    fun login(
        @Body
        requestLogin: RequestLogin
    ): Call<LoginResponse>

    @POST("register")
    fun regis(
        @Body
        requestReg: RequestReg
    ): Call<RegResponse>

    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") location: Int,
        @Header("Authorization") token: String
    ): StoryResponse

    @GET("stories")
    fun getAllStory(
        @Query("location") location: Int? = null,
        @Header("Authorization") token: String
    ): Call<StoryResponse>

    @Multipart
    @POST("stories")
    fun uploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat : Double,
        @Part("lon") lon : Double
    ): Call<FileUploadResponse>

    @GET("stories/{id}")
    fun getStory(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<DetailResponse>

}