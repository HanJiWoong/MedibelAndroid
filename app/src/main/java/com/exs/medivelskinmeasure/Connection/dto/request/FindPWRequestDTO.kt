package com.exs.medivelskinmeasure.Connection.dto.request

import com.google.gson.annotations.SerializedName

data class FindPWRequestDTO(
    @SerializedName("id") var memberId:String,
    @SerializedName("email") var memberEmail:String?,
    @SerializedName("phone_no") var memberMobile:String?
)
