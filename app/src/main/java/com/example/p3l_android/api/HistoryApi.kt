package com.example.p3l_android.api

class HistoryApi {
    companion object{
        //insert your ip address
        val BASE_URL = "https://gofit123.xyz/p3l_laravel/public/"

        val GET_HISTORY_TRANSAKSI = BASE_URL + "historyMemberTransaksi/"
        val GET_HISTORY_PRESENSI = BASE_URL + "historyMemberPresensi/"
        val GET_HISTORY_INSTRUKTUR = BASE_URL + "historyInstruktur/"
        val UPDATE_URL = BASE_URL + "bookingGym/"
        val DELETE_URL = BASE_URL + "bookingGym/"
        val GET_PROFILE_DATA = BASE_URL + "profileMember/"
    }
}