package com.exs.medivelskinmeasure.Connection.dto.result

import com.google.gson.annotations.SerializedName

data class UserInfoUpdateResultDTO(
    @SerializedName("code") val code: Int,
    @SerializedName("data") val data: UserInfoUpdateDataDTO
) {
    data class UserInfoUpdateDataDTO(
        @SerializedName("Success") val success: Boolean,
        @SerializedName("Message") val Message: String,
        @SerializedName("data") val content: UserInfoUpdateContentDTO?
    ) {
        data class UserInfoUpdateContentDTO(
            @SerializedName("status") val status: String,
        )
    }
}