package com.exs.medivelskinmeasure.Connection.dto.result

import com.google.gson.annotations.SerializedName

data class CheckDupIDResultDTO(
    @SerializedName("code") val code: Int,
    @SerializedName("data") val data: CheckDupIDDataDTO
) {
    data class CheckDupIDDataDTO(
        @SerializedName("Success") val success: Boolean,
        @SerializedName("Message") val Message: String,
        @SerializedName("data") val content: CheckDupIDContentDTO?
    ) {
        data class CheckDupIDContentDTO(
            @SerializedName("status") val status: String,
        )
    }
}
