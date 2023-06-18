package com.example.p3l_android.api

class UserApi {
    companion object{
        //insert your ip address
        val BASE_URL = "https://gofit123.xyz/p3l_laravel/public/"

        val LOGIN = BASE_URL + "loginAndroid/"
        val REGISTER = BASE_URL + "register/"

        val GET_BY_USERNAME_URL = BASE_URL + "User/"
        val UPDATE_URL = BASE_URL + "User/"
    }
}