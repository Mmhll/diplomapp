package com.ggg.gggapp.repository

import com.ggg.gggapp.model.User
import com.ggg.gggapp.remote_model.MessageResponse
import com.ggg.gggapp.remote_model.UpdatePasswordRequest
import com.ggg.gggapp.remote_model.UpdateUserRequest
import com.ggg.gggapp.remote_model.UserIdAndRoleIdRequest
import com.ggg.gggapp.service.UserService
import com.ggg.gggapp.utils.generateToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepository @Inject constructor(private val userService: UserService) {

    fun updateUserData(token: String, request: UpdateUserRequest): Flow<MessageResponse> {
        return flow {
            emit(userService.updateUserData(generateToken(token), request))
        }
    }

    fun updateUserPassword(token: String, request: UpdatePasswordRequest): Flow<MessageResponse> {
        return flow {
            emit(userService.updateUserPassword(generateToken(token), request))
        }
    }

    fun getUsers(token: String): Flow<ArrayList<User>> {
        return flow {
            emit(userService.getAllUsers(generateToken(token)))
        }
    }

    fun deleteUser(token: String, id: String): Flow<MessageResponse> {
        return flow {
            emit(userService.deleteUser(generateToken(token), id))
        }
    }

    fun updateUserRole(token: String, userId: Long, roleId: Long): Flow<MessageResponse> {
        return flow {
            emit(
                userService.updateUserRole(
                    generateToken(token),
                    UserIdAndRoleIdRequest(userId, roleId)
                )
            )
        }
    }

}