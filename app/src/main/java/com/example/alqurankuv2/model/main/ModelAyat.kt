package com.example.alqurankuv2.model.main

import com.google.gson.annotations.SerializedName

class ModelAyat {
    @SerializedName("ar")
    lateinit var arab: String

    @SerializedName("id")
    lateinit var indo: String

    @SerializedName("nomor")
    lateinit var nomor: String
}