package com.example.mystoryapps.response

data class RequestLogin(
    var email: String,
    var password: String
)

data class RequestReg(
    var name: String,
    var email: String,
    var password: String
)