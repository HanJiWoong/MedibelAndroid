package com.exs.medivelskinmeasure.Connection.dto.request

import com.google.gson.annotations.SerializedName

data class LoginRequestDTO(
    @SerializedName("id") var userId: String,
    @SerializedName("password") var password: String,
)
