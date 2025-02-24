package com.example.actapp.BaseDeDatos.ErrorAPI

import com.google.gson.annotations.SerializedName

data class ApiError(
    @SerializedName("message")
    val message: String
)
