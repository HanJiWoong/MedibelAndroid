package com.exs.medivelskinmeasure.Connection.dto.request

import com.google.gson.annotations.SerializedName

data class ChangePWRequestDTO(
    @SerializedName("hash_token") var token: String,
    @SerializedName("new_password") var password: String
)