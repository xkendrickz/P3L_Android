package com.example.p3l_android.models

import java.util.*

class Member (var member_id: String, var nama_member:String, var alamat:String, var tanggal_lahir: Date, var tanggal_daftar: Date, var telepon: String, var email: String, var status: Boolean, var sisa_deposit_reguler: Int, var sisa_deposit_paket: Int, var username: String, var password: String){
    var id_member: Long? = null
}