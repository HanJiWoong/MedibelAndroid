package com.exs.medivelskinmeasure.Connection.dto.result

import com.google.gson.annotations.SerializedName

data class LoginResultDTO(
    @SerializedName("code") val code:Int,
    @SerializedName("data") val data: LoginResultDataDTO
) {
    data class LoginResultDataDTO(
        @SerializedName("Success") val success:Boolean,
        @SerializedName("Message") val Message:String,
        @SerializedName("data") val content: LoginResultContentDTO
    ) {
        data class LoginResultContentDTO(
            @SerializedName("hash_token") val token:String,
            @SerializedName("admin_flag") val adminFlag:Int
        )
    }
}
