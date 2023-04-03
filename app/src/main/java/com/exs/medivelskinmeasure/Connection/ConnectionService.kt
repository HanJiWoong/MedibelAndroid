package com.exs.medivelskinmeasure.Connection

import android.util.Log
import com.exs.medivelskinmeasure.Connection.dto.request.*
import com.exs.medivelskinmeasure.Connection.dto.result.*
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

    fun autoLogin(token: String, result: (result: Boolean, data: LoginResultDTO?) -> Unit) {
        mService.memberAutoLogin(token).enqueue(object : Callback<LoginResultDTO> {
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
        mService.findID(params.memberName, params.memberEmail, params.memberMobile)
            .enqueue(object : Callback<FindIDResultDTO> {
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

    fun findPW(
        params: FindPWRequestDTO,
        result: (result: Boolean, data: FindPWResultDTO?) -> Unit
    ) {
        mService.findPW(params).enqueue(object : Callback<FindPWResultDTO> {
            override fun onResponse(
                call: Call<FindPWResultDTO>,
                response: Response<FindPWResultDTO>
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

            override fun onFailure(call: Call<FindPWResultDTO>, t: Throwable) {
                result(false, null)
            }
        })
    }

    fun checkDuplicationID(
        memberId: String,
        result: (result: Boolean, data: CheckDupIDResultDTO?) -> Unit
    ) {
        mService.checkDupID(memberId).enqueue(object : Callback<CheckDupIDResultDTO> {
            override fun onResponse(
                call: Call<CheckDupIDResultDTO>,
                response: Response<CheckDupIDResultDTO>
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

            override fun onFailure(call: Call<CheckDupIDResultDTO>, t: Throwable) {
                result(false, null)
            }
        })
    }

    fun getUserInfo(token: String, result: (result: Boolean, data: UserInfoResultDTO?) -> Unit) {
        mService.userInfo(token).enqueue(object : Callback<UserInfoResultDTO> {
            override fun onResponse(
                call: Call<UserInfoResultDTO>,
                response: Response<UserInfoResultDTO>
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

            override fun onFailure(call: Call<UserInfoResultDTO>, t: Throwable) {
                result(false, null)
            }
        })
    }

    /**
     * 회원 정보 업데이트
     */
    fun updateUserInfo(
        params: UserInfoUpdateRequestDTO,
        result: (result: Boolean, data: UserInfoUpdateResultDTO?) -> Unit
    ) {
        mService.userInfoUpdate(params).enqueue(object : Callback<UserInfoUpdateResultDTO> {
            override fun onResponse(
                call: Call<UserInfoUpdateResultDTO>,
                response: Response<UserInfoUpdateResultDTO>
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

            override fun onFailure(call: Call<UserInfoUpdateResultDTO>, t: Throwable) {
                result(false, null)
            }

        })
    }

    /**
     * 비밀번호 변경
     */
    fun changePW(
        params: ChangePWRequestDTO,
        result: (result: Boolean, data: ChangePWResultDTO?) -> Unit
    ) {
        mService.changePW(params).enqueue(object : Callback<ChangePWResultDTO> {
            override fun onResponse(
                call: Call<ChangePWResultDTO>,
                response: Response<ChangePWResultDTO>
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

            override fun onFailure(call: Call<ChangePWResultDTO>, t: Throwable) {
                result(false, null)
            }

        })
    }

    /**
     * 회원 탈퇴
     */
    fun withDrawal(
        token: String,
        result: (result: Boolean, data: WithDrawalResultDTO?) -> Unit
    ) {
        val params = WithDrawalRequestDTO(token)
        mService.withDrawal(params).enqueue(object : Callback<WithDrawalResultDTO> {
            override fun onResponse(
                call: Call<WithDrawalResultDTO>,
                response: Response<WithDrawalResultDTO>
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

            override fun onFailure(call: Call<WithDrawalResultDTO>, t: Throwable) {
                result(false, null)
            }
        })
    }

}