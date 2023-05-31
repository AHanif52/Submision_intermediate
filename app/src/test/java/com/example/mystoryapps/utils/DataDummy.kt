package com.example.mystoryapps.utils

import com.example.mystoryapps.response.*

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items = ArrayList<ListStoryItem>()
        for (i in 0..5) {
            val stories = ListStoryItem(
                "story-$i",
                "https://story-api.dicoding.dev/images/stories/photos-1683293016264_VcufaLoy.jpg",
                "2023-05-05T13:23:36.266Z",
                "Name",
                "Description",
                106.15299,
                -6.1166367,
            )
            items.add(stories)
        }
        return items
    }

    fun generateDummyRequestLogin(): RequestLogin {
        return RequestLogin("123@gmail.com", "123456")
    }

    fun generateDummyResponseLogin(): LoginResponse{
        val loginResult = LoginResult("hanif", "hanif", "ini-token")
        return LoginResponse(loginResult,false, "Login successfully")
    }

    fun generateDummyRequestRegister(): RequestReg {
        return RequestReg("ciel", "ciel@ciel.com", "123456789")
    }

    fun generateDummyResponseReg(): RegResponse{
        return RegResponse(false, "User created")
    }
}