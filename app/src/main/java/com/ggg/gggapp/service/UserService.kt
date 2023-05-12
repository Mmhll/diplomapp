package com.ggg.gggapp.service

import com.ggg.gggapp.model.User
import com.ggg.gggapp.remote_model.MessageResponse
import com.ggg.gggapp.remote_model.OneParamRequest
import com.ggg.gggapp.remote_model.UpdatePasswordRequest
import com.ggg.gggapp.remote_model.UpdateUserRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT

interface UserService {
    @PUT("user/update_user_data")
    suspend fun updateUserData(@Header("Authorization") token: String, @Body request: UpdateUserRequest): MessageResponse

    @PUT("user/update_user_password")
    suspend fun updateUserPassword(@Header("Authorization") token: String, @Body request: UpdatePasswordRequest): MessageResponse

    @GET("user/find_all")
    suspend fun getAllUsers(@Header("Authorization") token: String): List<User>

    @GET("user/delete_user")
    suspend fun deleteUser(@Header("Authorization") token: String, @Body request: OneParamRequest): MessageResponse
}