package com.example.p3l_android.api

class PresensiInstrukturApi {
    companion object{
        //insert your ip address
        val BASE_URL = "https://gofit123.xyz/p3l_laravel/public/"

        val GET_ALL_URL = BASE_URL + "presensiInstruktur/"
        val GET_BY_ID_URL = BASE_URL + "presensiInstruktur/"
        val ADD_URL = BASE_URL + "presensiInstruktur/"
        val UPDATE_URL = BASE_URL + "presensiInstruktur/"
        val DELETE_URL = BASE_URL + "presensiInstruktur/"
    }
}