package com.exs.medivelskinmeasure.Connection.dto.request

import com.google.gson.annotations.SerializedName

data class WithDrawalRequestDTO(
    @SerializedName("hash_token") val token:String
)
