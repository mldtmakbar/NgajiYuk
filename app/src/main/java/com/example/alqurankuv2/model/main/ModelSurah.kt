package com.example.alqurankuv2.model.main

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ModelSurah : Serializable {
    @SerializedName("arti")
    lateinit var arti: String

    @SerializedName("asma")
    lateinit var asma: String

    @SerializedName("ayat")
    lateinit var ayat: String

    @SerializedName("nama")
    lateinit var nama: String

    @SerializedName("type")
    lateinit var type: String

    @SerializedName("audio")
    lateinit var audio: String

    @SerializedName("nomor")
    lateinit var nomor: String

    @SerializedName("keterangan")
    lateinit var keterangan: String
}
//class ModelSurah : Serializable {
//    @SerializedName("arti")
//    lateinit var arti: String
//
//    @SerializedName("nama")
//    lateinit var asma: String
//
//    @SerializedName("jumlah_ayat")
//    lateinit var ayat: String
//
//    @SerializedName("nama_latin")
//    lateinit var nama: String
//
//    @SerializedName("tempat_turun")
//    lateinit var type: String
//
//    @SerializedName("audio")
//    lateinit var audio: String
//
//    @SerializedName("nomor")
//    lateinit var nomor: String
//
//    @SerializedName("deskripsi")
//    lateinit var keterangan: String
//}