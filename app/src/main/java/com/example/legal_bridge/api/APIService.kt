package com.example.legal_bridge.api

import com.example.legal_bridge.model.user.LoginUserResquest
import com.example.legal_bridge.model.user.UserResponse
import com.example.legal_bridge.model.user.RegisterUserResquest
import com.example.legal_bridge.model.forgetPassModel.forgetPasswordRequest
import com.example.legal_bridge.model.forgetPassModel.forgetPasswordResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface APIService {

    @POST("api/v1/auth/register")
    fun registerUser(@Body userRequest: RegisterUserResquest): Call<UserResponse>

    @POST("api/v1/auth/login")
    fun loginUser(@Body userRequest: LoginUserResquest): Call<UserResponse>

    @POST("api/v1/auth/forgotPassword")
    fun forgotPassword(@Body request: forgetPasswordRequest): Call<forgetPasswordResponse>

//    @POST("api/v1/auth/forgotPassword")
//    fun forgotPassword(@Body request: ): Call<forgetPasswordResponse>

}
