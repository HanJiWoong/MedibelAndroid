package com.exs.medivelskinmeasure.Connection.dto.result

import com.google.gson.annotations.SerializedName

data class ChangePWResultDTO(
    @SerializedName("code") val code: Int,
    @SerializedName("data") val data: ChangePWDataDTO
) {
    data class ChangePWDataDTO(
        @SerializedName("Success") val success: Boolean,
        @SerializedName("Message") val Message: String,
        @SerializedName("data") val content: ChangePWContentDTO?
    ) {
        data class ChangePWContentDTO(
            @SerializedName("hash_token") val token: String,
        )
    }
}


