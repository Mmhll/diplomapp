package com.ggg.gggapp.service

import com.ggg.gggapp.model.Chat
import com.ggg.gggapp.remote_model.IdRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.Query

interface ChatService {
    @GET("chat/get_chats")
    suspend fun getChats(@Header("Authorization") token: String, @Query("id") id: String): List<Chat>
}