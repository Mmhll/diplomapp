package com.ggg.gggapp.service

import com.ggg.gggapp.model.Role
import com.ggg.gggapp.remote_model.MessageResponse
import com.ggg.gggapp.remote_model.OneParamRequest
import com.ggg.gggapp.remote_model.RolenameAndPermissionRequest
import com.ggg.gggapp.remote_model.UpdateRoleRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface RoleService {
    @GET("roles/get_all")
    suspend fun getRoles(@Header("Authorization") token: String): ArrayList<Role>

    @POST("roles/find_role_by_name")
    suspend fun findRoleByName(
        @Header("Authorization") token: String,
        @Body request: OneParamRequest
    ): Role

    @POST("roles/edit_role")
    suspend fun editRoleName(
        @Header("Authorization") token: String,
        @Body request: UpdateRoleRequest
    ): MessageResponse

    @POST("roles/add_role")
    suspend fun addRole(
        @Header("Authorization") token: String,
        @Body request: RolenameAndPermissionRequest
    ): MessageResponse
}