package com.ggg.gggapp.repository

import com.ggg.gggapp.remote_model.MessageResponse
import com.ggg.gggapp.remote_model.SendMessageRequest
import com.ggg.gggapp.service.MessageService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Named

class MessageRepository @Inject constructor(@Named("messageService") val service: MessageService) {
    fun sendMessage(token: String, user_id: Long, message: String, chat_id: Long): Flow<MessageResponse> {
        return flow {
            emit(service.addMessage("Bearer $token", SendMessageRequest(chat_id, message, user_id)))
        }
    }

}