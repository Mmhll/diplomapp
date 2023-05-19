package com.ggg.gggapp.service

import com.ggg.gggapp.model.Task
import com.ggg.gggapp.model.User
import retrofit2.http.GET
import retrofit2.http.Header
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
}