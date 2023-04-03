package com.exs.medivelskinmeasure.Connection.dto.result

import com.google.gson.annotations.SerializedName

data class WithDrawalResultDTO(
    @SerializedName("code") val code: Int,
    @SerializedName("data") val data: WithDrawalDataDTO
) {
    data class WithDrawalDataDTO(
        @SerializedName("Success") val success: Boolean,
        @SerializedName("Message") val Message: String,
        @SerializedName("data") val content: WithDrawalContentDTO?
    ) {
        data class WithDrawalContentDTO(
            @SerializedName("status") val status: String,
        )
    }
}