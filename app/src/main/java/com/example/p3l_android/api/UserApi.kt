package com.example.p3l_android.api

class UserApi {
    companion object{
        //insert your ip address
        val BASE_URL = "http://192.168.1.32:8000/api/"

        val LOGIN = BASE_URL + "loginAndroid/"
        val REGISTER = BASE_URL + "register/"

        val GET_BY_USERNAME_URL = BASE_URL + "User/"
        val UPDATE_URL = BASE_URL + "User/"
    }
}