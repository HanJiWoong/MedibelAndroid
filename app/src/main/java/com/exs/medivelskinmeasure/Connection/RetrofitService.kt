package com.exs.medivelskinmeasure.Connection

import com.exs.medivelskinmeasure.Connection.dto.request.*
import com.exs.medivelskinmeasure.Connection.dto.result.*
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {
    // 회원 가입
    @PUT("user/signup")
    fun memberSignup(
        @Body parameters: SignupRequestDTO
    ): Call<LoginResultDTO>

    // 로그인
//    @POST("login")
//    fun memberLogin(
//        @Body Parameters:LoginRequestDTO
//    ): Call<LoginResultDTO>

    // 로그인
    @GET("user/signin")
    fun memberLogin(
        @Query("id") id: String,
        @Query("password") pw: String
    ): Call<LoginResultDTO>

    // 자동로그인
    @GET("user/auto_signin")
    fun memberAutoLogin(
        @Query("hash_token") token: String
    ): Call<LoginResultDTO>


    // 아이디찾기
    @GET("user/find_id")
    fun findID(
        @Query("user_name") name: String?,
        @Query("email") email: String?,
        @Query("phone_no") phone_no: String?
    ): Call<FindIDResultDTO>


    // 비밀번호 재설정
    @PUT("user/renew_password")
    fun findPW(
        @Body body: FindPWRequestDTO
    ): Call<FindPWResultDTO>

    // ID 중복 확인
    @GET("user/duplicated_check")
    fun checkDupID(
        @Query("id") memberID: String
    ): Call<CheckDupIDResultDTO>

    // 회원 정보
    @GET("user/user_info")
    fun userInfo(@Query("hash_token") token: String): Call<UserInfoResultDTO>

    // 회원 정보 업데이트
    @POST("user/user_info_update")
    fun userInfoUpdate(@Body body: UserInfoUpdateRequestDTO): Call<UserInfoUpdateResultDTO>

    // 비밀번호 변경
    @POST("user/password_update")
    fun changePW(@Body body:ChangePWRequestDTO): Call<ChangePWResultDTO>

    // 회원 탈퇴
    @DELETE("user/withdrawal")
    fun withDrawal(@Body body:WithDrawalRequestDTO): Call<WithDrawalResultDTO>

}