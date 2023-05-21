package com.ggg.gggapp.service

import com.ggg.gggapp.model.Task
import com.ggg.gggapp.model.User
import com.ggg.gggapp.remote_model.CreateTaskRequest
import com.ggg.gggapp.remote_model.IdRequest
import com.ggg.gggapp.remote_model.MessageResponse
import com.ggg.gggapp.remote_model.TaskAndStatusRequest
import com.ggg.gggapp.remote_model.UpdateTaskRequest
import com.ggg.gggapp.remote_model.UserAndTaskRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface TaskService {
    @GET("tasks/get_task")
    suspend fun getTask(@Header("Authorization") token: String, @Query("id") id: String): Task
    @GET("tasks/get_all_tasks_member")
    suspend fun getTasksMember(@Header("Authorization") token: String, @Query("id") id: String): ArrayList<Task>
    @GET("tasks/get_all_tasks_executor")
    suspend fun getTasksExecutor(@Header("Authorization") token: String, @Query("id") id: String): ArrayList<Task>
    @GET("tasks/get_all_tasks_creator")
    suspend fun getTasksCreator(@Header("Authorization") token: String, @Query("id") id: String): ArrayList<Task>
    @POST("tasks/save_task")
    suspend fun saveTask(@Header("Authorization") token: String, @Body request: CreateTaskRequest): MessageResponse
    @POST("tasks/add_member")
    suspend fun addMember(@Header("Authorization") token: String, @Body request: UserAndTaskRequest): MessageResponse
    @PUT("tasks/update_executor")
    suspend fun updateExecutor(@Header("Authorization") token: String, @Body request: UserAndTaskRequest): MessageResponse
    @PUT("tasks/update_task")
    suspend fun updateTask(@Header("Authorization") token: String, @Body request: UpdateTaskRequest): MessageResponse
    @PUT("tasks/update_status")
    suspend fun updateStatus(@Header("Authorization") token: String, @Body request:TaskAndStatusRequest): MessageResponse
    @DELETE("tasks/delete_task")
    suspend fun deleteTask(@Header("Authorization") token: String, @Query("id") id: String): MessageResponse
    @DELETE("tasks/delete_member")
    suspend fun deleteMember(@Header("Authorization") token: String, @Query("user_id") userId: String, @Query("task_id") taskId: String): MessageResponse
}