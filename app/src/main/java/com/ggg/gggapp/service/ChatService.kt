package com.ggg.gggapp.service

import com.ggg.gggapp.model.Chat
import com.ggg.gggapp.remote_model.ChatAndUserRequest
import com.ggg.gggapp.remote_model.CreateChatRequest
import com.ggg.gggapp.remote_model.MessageResponse
import retrofit2.http.*

interface ChatService {
    @GET("chat/get_chats")
    suspend fun getChats(@Header("Authorization") token: String, @Query("id") id: String): ArrayList<Chat>
    @GET("chat/get_chat")
    suspend fun getChat(@Header("Authorization") token: String, @Query("id") id: String): Chat
    @POST("chat/create")
    suspend fun createChat(@Header("Authorization") token: String, @Body request: CreateChatRequest): MessageResponse
    @POST("chat/add_member")
    suspend fun addMember(@Header("Authorization") token: String, @Body request: ChatAndUserRequest): MessageResponse
}