package com.example.alqurankuv2.model.response

import com.google.gson.annotations.SerializedName
import com.example.alqurankuv2.model.nearby.ModelResults
class ModelResultNearby {
    @SerializedName("results")
    lateinit var modelResults: List<ModelResults>
}