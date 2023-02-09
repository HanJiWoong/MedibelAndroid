package com.exs.medivelskinmeasure.Connection.dto.result

import com.google.gson.annotations.SerializedName

data class FindIDResultDTO(
    @SerializedName("code") val code:Int,
    @SerializedName("data") val data:FindIDDataDTO
) {
    data class FindIDDataDTO(
        @SerializedName("Success") val success:Boolean,
        @SerializedName("Message") val Message:String,
        @SerializedName("data") val content:FindIDContentDTO
    ) {
        data class FindIDContentDTO(
            @SerializedName("id") val memberId:String
        )
    }
}
