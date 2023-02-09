package com.exs.medivelskinmeasure.Connection

import android.util.Log
import com.exs.medivelskinmeasure.Connection.dto.request.FindIDRequestDTO
import com.exs.medivelskinmeasure.Connection.dto.request.LoginRequestDTO
import com.exs.medivelskinmeasure.Connection.dto.request.SignupRequestDTO
import com.exs.medivelskinmeasure.Connection.dto.result.FindIDResultDTO
import com.exs.medivelskinmeasure.Connection.dto.result.LoginResultDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ConnectionService {
    private val TAG: String = "ConnectionService"

    //    private val mServerUrl:String = "http://3.39.48.213:5000/"
    private val mServerUrl: String = "https://wavu-api.medivelbio.com/"

    private lateinit var mRetrofit: Retrofit
    private lateinit var mService: RetrofitService


    init {
        mRetrofit = Retrofit.Builder().baseUrl(mServerUrl)
            .addConverterFactory(GsonConverterFactory.create()).build()
        mService = mRetrofit.create(RetrofitService::class.java)
    }

    fun signUp(params: SignupRequestDTO, result: (result: Boolean, data: LoginResultDTO?) -> Unit) {
        mService.memberSignup(params).enqueue(object : Callback<LoginResultDTO> {
            override fun onResponse(
                call: Call<LoginResultDTO>,
                response: Response<LoginResultDTO>
            ) {
                if (response.code() == 200) {
                    if (response.body()!!.code == 200) {
                        result(true, response.body())
                    } else {
                        result(false, null)
                    }
                } else {
                    result(false, null)
                }
            }

            override fun onFailure(call: Call<LoginResultDTO>, t: Throwable) {
                result(false, null)
            }
        })
    }

//    fun login(params:LoginRequestDTO, result:(result:Boolean, data:LoginResultDTO?)->Unit) {
//        mService.memberLogin(params).enqueue(object:Callback<LoginResultDTO> {
//            override fun onResponse(
//                call: Call<LoginResultDTO>,
//                response: Response<LoginResultDTO>
//            ) {
//                if(response.code() == 200) {
//                    if(response.body()!!.code == 200) {
//                        result(true, response.body())
//                    } else {
//                        result(false,null)
//                    }
//                } else {
//                    result(false,null)
//                }
//            }
//
//            override fun onFailure(call: Call<LoginResultDTO>, t: Throwable) {
//                result(false,null)
//            }
//        })
//    }

    fun login(id: String, pw: String, result: (result: Boolean, data: LoginResultDTO?) -> Unit) {
        mService.memberLogin(id, pw).enqueue(object : Callback<LoginResultDTO> {
            override fun onResponse(
                call: Call<LoginResultDTO>,
                response: Response<LoginResultDTO>
            ) {
                if (response.code() == 200) {
                    if (response.body()!!.code == 200) {
                        result(true, response.body())
                    } else {
                        result(false, null)
                    }
                } else {
                    result(false, null)
                }
            }

            override fun onFailure(call: Call<LoginResultDTO>, t: Throwable) {
                result(false, null)
            }
        })
    }

    fun findID(
        params: FindIDRequestDTO,
        result: (result: Boolean, data: FindIDResultDTO?) -> Unit
    ) {
        mService.findID(params.memberEmail,params.memberMobile).enqueue(object : Callback<FindIDResultDTO> {
            override fun onResponse(
                call: Call<FindIDResultDTO>,
                response: Response<FindIDResultDTO>
            ) {
                if (response.code() == 200) {
                    if (response.body()!!.data.success) {
                        result(true, response.body())
                    } else {
                        result(false, null)
                    }
                } else {
                    result(false, null)
                }
            }

            override fun onFailure(call: Call<FindIDResultDTO>, t: Throwable) {
                result(false, null)
            }

        })
    }

}