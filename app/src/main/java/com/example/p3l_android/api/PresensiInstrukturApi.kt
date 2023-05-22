package com.example.p3l_android.api

class PresensiInstrukturApi {
    companion object{
        //insert your ip address
        val BASE_URL = "http://192.168.1.32:8000/api/"

        val GET_ALL_URL = BASE_URL + "presensiInstruktur/"
        val GET_BY_ID_URL = BASE_URL + "presensiInstruktur/"
        val ADD_URL = BASE_URL + "presensiInstruktur/"
        val UPDATE_URL = BASE_URL + "presensiInstruktur/"
        val DELETE_URL = BASE_URL + "presensiInstruktur/"
    }
}