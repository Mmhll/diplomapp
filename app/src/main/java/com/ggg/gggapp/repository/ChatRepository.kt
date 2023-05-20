package com.ggg.gggapp.repository

import com.ggg.gggapp.model.Chat
import com.ggg.gggapp.remote_model.ChatAndUserRequest
import com.ggg.gggapp.remote_model.CreateChatRequest
import com.ggg.gggapp.remote_model.MessageResponse
import com.ggg.gggapp.service.ChatService
import com.ggg.gggapp.utils.generateToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Named

class ChatRepository @Inject constructor(@Named("chatService") private val service: ChatService) {
    suspend fun getChats(token: String, id: Long): Flow<ArrayList<Chat>> {
        return flow {
            emit(service.getChats(generateToken(token), id.toString()))
        }
    }

    suspend fun getChat(token: String, id: Long): Flow<Chat> {
        return flow {
            emit(service.getChat(generateToken(token), id.toString()))
        }
    }

    suspend fun createChat(
        token: String,
        name: String,
        members: ArrayList<Long>
    ): Flow<MessageResponse> {
        return flow {
            emit(service.createChat(generateToken(token), CreateChatRequest(members, name)))
        }
    }

    suspend fun addMember(token: String, chat_id: Long, user_id: Long): Flow<MessageResponse> {
        return flow {
            emit(service.addMember(generateToken(token), ChatAndUserRequest(chat_id, user_id)))
        }
    }
}