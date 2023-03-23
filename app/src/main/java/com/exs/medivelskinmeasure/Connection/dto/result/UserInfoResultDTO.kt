package com.exs.medivelskinmeasure.Connection.dto.result

import com.google.gson.annotations.SerializedName

data class UserInfoResultDTO(
    @SerializedName("code") val code: Int,
    @SerializedName("data") val data: UserInfoDataDTO
) {
    data class UserInfoDataDTO(
        @SerializedName("Success") val success: Boolean,
        @SerializedName("Message") val Message: String,
        @SerializedName("data") val content: UserInfoContentDTO?
    ) {
        data class UserInfoContentDTO(
            @SerializedName("user_id") val memberId: String,
            @SerializedName("user_name") val memberName: String,
            @SerializedName("e_mail") val email: String,
            @SerializedName("company") val company: String,
            @SerializedName("phone_no") val mobile: String
        )
    }
}

