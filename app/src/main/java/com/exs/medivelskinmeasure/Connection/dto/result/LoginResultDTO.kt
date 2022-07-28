package com.exs.medivelskinmeasure.Connection.dto.result

import com.google.gson.annotations.SerializedName

data class LoginResultDTO(
    @SerializedName("admin_flag") var admin:Int,
    @SerializedName("code") var code:Int,
    @SerializedName("hash_token") var token:String,
    @SerializedName("msg") var resMsg:String,
    @SerializedName("status") var statusMsg:String
)
