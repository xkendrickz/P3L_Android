package com.example.p3l_android.api

class MemberApi {
    companion object{
        //insert your ip address
        val BASE_URL = "http://192.168.43.231:8000/api/"

        val GET_ALL_URL = BASE_URL + "bookingGym/"
        val GET_BY_ID_URL = BASE_URL + "bookingGym/"
        val ADD_URL = BASE_URL + "bookingGym/"
        val UPDATE_URL = BASE_URL + "bookingGym/"
        val DELETE_URL = BASE_URL + "bookingGym/"
        val GET_PROFILE_DATA = BASE_URL + "profileMember/"
        val GET_PROFILE_INSTRUKTUR = BASE_URL + "profileInstruktur/"
    }
}