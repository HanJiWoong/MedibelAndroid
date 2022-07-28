package com.exs.medivelskinmeasure.Connection.dto.request

import com.google.gson.annotations.SerializedName

data class FindIDRequestDTO(
    @SerializedName("name") val memberName:String,
    @SerializedName("email") val memberEmail:String?,
    @SerializedName("phone_no") val memberMobile:String?
)
