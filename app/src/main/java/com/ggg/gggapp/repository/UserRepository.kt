package com.ggg.gggapp.repository

import com.ggg.gggapp.model.User
import com.ggg.gggapp.remote_model.MessageResponse
import com.ggg.gggapp.remote_model.OneParamRequest
import com.ggg.gggapp.remote_model.UpdatePasswordRequest
import com.ggg.gggapp.remote_model.UpdateUserRequest
import com.ggg.gggapp.service.UserService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepository @Inject constructor(private val userService: UserService) {

    fun updateUserData(token: String, request: UpdateUserRequest): Flow<MessageResponse> {
        return flow {
            emit(userService.updateUserData("Bearer $token", request))
        }
    }

    fun updateUserPassword(token: String, request: UpdatePasswordRequest): Flow<MessageResponse> {
        return flow {
            emit(userService.updateUserPassword("Bearer $token", request))
        }
    }

    fun getUsers(token: String): Flow<ArrayList<User>> {
        return flow {
            emit(userService.getAllUsers("Bearer $token"))
        }
    }

    fun deleteUser(token: String, email: String): Flow<MessageResponse> {
        return flow {
            emit(userService.deleteUser("Bearer $token", OneParamRequest(email)))
        }
    }

}