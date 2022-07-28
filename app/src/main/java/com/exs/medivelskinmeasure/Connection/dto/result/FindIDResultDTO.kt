package com.exs.medivelskinmeasure.Connection.dto.result

import com.google.gson.annotations.SerializedName

data class FindIDResultDTO(
    @SerializedName("code") val code:Int,
    @SerializedName("id") val memberList:ArrayList<FindIDContentDTO>
) {
    data class FindIDContentDTO(
        @SerializedName("admin_flag") val admin:Int,
        @SerializedName("capture_count") val captureCnt:Int,
        @SerializedName("e_mail") val email:String,
        @SerializedName("hash_token") val token:String,
        @SerializedName("nickname") val nick:String,
        @SerializedName("sign_time") val joinDate:String,
        @SerializedName("id") val id:String,
        @SerializedName("name") val name:String,
        @SerializedName("company") val company:String
    )
}
