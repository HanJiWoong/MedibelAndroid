package com.exs.medivelskinmeasure.Connection.dto.request

import com.google.gson.annotations.SerializedName

data class SignupRequestDTO(
    @SerializedName("id") var userId:String,
    @SerializedName("password") var password:String,
    @SerializedName("name") var userName:String,
    @SerializedName("company") var company:String,
    @SerializedName("email") var email:String,
    @SerializedName("phone_no") var mobile:String
)
