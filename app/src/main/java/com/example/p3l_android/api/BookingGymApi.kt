package com.example.p3l_android.api

class BookingGymApi {
    companion object{
        //insert your ip address
        val BASE_URL = "http://192.168.1.32:8000/api/"

        val GET_ALL_URL = BASE_URL + "bookingGym/"
        val GET_BY_ID_URL = BASE_URL + "bookingGym/"
        val ADD_URL = BASE_URL + "bookingGym/"
        val UPDATE_URL = BASE_URL + "bookingGym/"
        val DELETE_URL = BASE_URL + "bookingGym/"
    }
}