package com.ggg.gggapp.service

import com.ggg.gggapp.model.User
import com.ggg.gggapp.remote_model.MessageResponse
import com.ggg.gggapp.remote_model.UpdatePasswordRequest
import com.ggg.gggapp.remote_model.UpdateUserRequest
import com.ggg.gggapp.remote_model.UserIdAndRoleIdRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Query

interface UserService {
    @PUT("user/update_user_data")
    suspend fun updateUserData(
        @Header("Authorization") token: String,
        @Body request: UpdateUserRequest
    ): MessageResponse

    @PUT("user/update_user_password")
    suspend fun updateUserPassword(
        @Header("Authorization") token: String,
        @Body request: UpdatePasswordRequest
    ): MessageResponse

    @GET("user/find_all")
    suspend fun getAllUsers(@Header("Authorization") token: String): ArrayList<User>

    @DELETE("user/delete_user")
    suspend fun deleteUser(
        @Header("Authorization") token: String,
        @Query("id") id: String
    ): MessageResponse

    @PUT("user/update_user_role")
    suspend fun updateUserRole(
        @Header("Authorization") token: String,
        @Body request: UserIdAndRoleIdRequest
    ): MessageResponse
}