package com.ggg.gggapp.repository

import com.ggg.gggapp.model.Chat
import com.ggg.gggapp.service.ChatService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Named

class ChatRepository @Inject constructor(@Named("chatService") private val service: ChatService) {
    suspend fun getChats(token: String, id: Long): Flow<ArrayList<Chat>> {
        return flow {
            emit(service.getChats(token, id.toString()))
        }
    }

    suspend fun getChat(token: String, id: Long): Flow<Chat>{
        return flow {
            emit(service.getChat("Bearer $token", id.toString()))
        }
    }
}