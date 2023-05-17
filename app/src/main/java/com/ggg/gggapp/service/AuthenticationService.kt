package com.ggg.gggapp.service

import com.ggg.gggapp.remote_model.AuthenticationRequest
import com.ggg.gggapp.remote_model.AuthenticationResponse
import com.ggg.gggapp.remote_model.MessageResponse
import com.ggg.gggapp.remote_model.RegisterUserRequest
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


interface AuthenticationService {
    @POST("auth/signin")
    suspend fun auth(@Body request: AuthenticationRequest): AuthenticationResponse
    @POST("auth/signup")
    suspend fun register(@Header("Authorization") token: String, @Body request: RegisterUserRequest): MessageResponse

}