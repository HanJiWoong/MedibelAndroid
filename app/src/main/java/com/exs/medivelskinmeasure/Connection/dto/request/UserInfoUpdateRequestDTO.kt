package com.exs.medivelskinmeasure.Connection.dto.request

import com.google.gson.annotations.SerializedName

data class UserInfoUpdateRequestDTO(
    @SerializedName("hash_token") var token:String,
    @SerializedName("name") var name:String,
    @SerializedName("company") var company:String,
    @SerializedName("email") var email:String,
    @SerializedName("phone_no") var mobile:String
)
