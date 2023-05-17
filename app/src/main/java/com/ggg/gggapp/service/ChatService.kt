package com.ggg.gggapp.service

import com.ggg.gggapp.model.Chat
import retrofit2.http.*

interface ChatService {
    @GET("chat/get_chats")
    suspend fun getChats(@Header("Authorization") token: String, @Query("id") id: String): ArrayList<Chat>
    @GET("chat/get_chat")
    suspend fun getChat(@Header("Authorization") token: String, @Query("id") id: String): Chat

}