package com.example.mystoryapps.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class StoryResponse(

	@field:SerializedName("listStory")
	val listStory: List<ListStoryItem> ? = null,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

@Entity(tableName = "stories")
data class ListStoryItem(

	@PrimaryKey
	@field:SerializedName("id")
	var id: String,

	@field:SerializedName("photoUrl")
	var photoUrl: String,

	@field:SerializedName("createdAt")
	var createdAt: String,

	@field:SerializedName("name")
	var name: String,

	@field:SerializedName("description")
	var description: String,

	@field:SerializedName("lon")
	var lon: Double,

	@field:SerializedName("lat")
	var lat: Double
)
