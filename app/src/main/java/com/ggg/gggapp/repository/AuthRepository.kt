package com.ggg.gggapp.repository

import com.ggg.gggapp.remote_model.AuthenticationRequest
import com.ggg.gggapp.remote_model.AuthenticationResponse
import com.ggg.gggapp.remote_model.MessageResponse
import com.ggg.gggapp.remote_model.RegisterUserRequest
import com.ggg.gggapp.service.AuthenticationService
import com.ggg.gggapp.utils.generateToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Named


class AuthRepository @Inject constructor(@Named("authenticationService") val service: AuthenticationService) {
    fun auth(email: String, password: String): Flow<AuthenticationResponse> {
        return flow {
            emit(service.auth(AuthenticationRequest(email, password)))
        }
    }

    fun register(token: String, email: String, password: String, firstname: String, lastname: String, middlename: String, phoneNumber: String): Flow<MessageResponse>{
        return flow {
            emit(service.register(generateToken(token), RegisterUserRequest(email, firstname, lastname, middlename, password, phoneNumber)))
        }
    }
}