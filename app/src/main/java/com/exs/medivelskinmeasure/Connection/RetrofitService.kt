package com.exs.medivelskinmeasure.Connection

import com.exs.medivelskinmeasure.Connection.dto.request.FindIDRequestDTO
import com.exs.medivelskinmeasure.Connection.dto.request.LoginRequestDTO
import com.exs.medivelskinmeasure.Connection.dto.request.SignupRequestDTO
import com.exs.medivelskinmeasure.Connection.dto.result.FindIDResultDTO
import com.exs.medivelskinmeasure.Connection.dto.result.LoginResultDTO
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {
    // 회원 가입
    @PUT("user/signup")
    fun memberSignup(
        @Body parameters:SignupRequestDTO
    ): Call<LoginResultDTO>

    // 로그인
//    @POST("login")
//    fun memberLogin(
//        @Body Parameters:LoginRequestDTO
//    ): Call<LoginResultDTO>

    @GET("user/signin")
    fun memberLogin(
        @Query("id") id:String,
        @Query("password") pw:String
    ): Call<LoginResultDTO>


    // 아이디찾기
    @GET("user/find_id")
    fun findID(
        @Query("email") email:String?,
        @Query("phone_no") phone_no:String?
    ):Call<FindIDResultDTO>


}