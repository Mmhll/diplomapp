package com.ggg.gggapp.service

import com.ggg.gggapp.remote_model.MessageResponse
import com.ggg.gggapp.remote_model.SendMessageRequest
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface MessageService {
    @POST("message/add_message")
    suspend fun addMessage(@Header("Authorization") token: String, @Body request: SendMessageRequest): MessageResponse
}