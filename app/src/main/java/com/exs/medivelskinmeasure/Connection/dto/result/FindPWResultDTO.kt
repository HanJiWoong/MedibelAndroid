package com.exs.medivelskinmeasure.Connection.dto.result

import com.google.gson.annotations.SerializedName

data class FindPWResultDTO(
    @SerializedName("code") val code: Int,
    @SerializedName("data") val data: FindPWDataDTO
) {
    data class FindPWDataDTO(
        @SerializedName("Success") val success: Boolean,
        @SerializedName("Message") val Message: String,
        @SerializedName("data") val content: FindPWContentDTO?
    ) {
        data class FindPWContentDTO(
            @SerializedName("id") val memberId: String,
            @SerializedName("name") val memberName: String
        )
    }
}

