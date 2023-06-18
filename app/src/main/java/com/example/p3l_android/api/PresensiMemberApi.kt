package com.example.p3l_android.api

class PresensiMemberApi {
    companion object{
        //insert your ip address
        val BASE_URL = "https://gofit123.xyz/p3l_laravel/public/"

        val GET_ALL_URL = BASE_URL + "presensiKelas/"
        val GET_BY_ID_URL = BASE_URL + "presensiKelas/"
        val ADD_URL = BASE_URL + "presensiKelas/"
        val UPDATE_URL = BASE_URL + "presensiKelas/"
        val DELETE_URL = BASE_URL + "presensiKelas/"
    }
}