package com.ggg.gggapp.viewmodel.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ggg.gggapp.remote_model.AuthenticationResponse
import com.ggg.gggapp.repository.AuthRepository
import com.ggg.gggapp.utils.ApiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class AuthViewModel @Inject constructor(
    @Named("authenticationRepository") private val authRepository: AuthRepository
) : ViewModel() {
    private val _data: MutableLiveData<AuthenticationResponse> = MutableLiveData()
    val data: LiveData<AuthenticationResponse> get() = _data
    private val _status = MutableLiveData<ApiStatus>()
    val status : LiveData<ApiStatus>
        get() = _status

    fun authorize(email: String, password: String) {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                authRepository.auth(email, password).collect {
                    _data.value = it
                    _status.value = ApiStatus.COMPLETE
                }
            } catch (e: HttpException){
                _status.value = ApiStatus.FAILED
            }
        }
    }

    fun register(token: String, email: String, password: String, firstname: String, lastname: String, middlename: String, phoneNumber: String){
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                authRepository.register(token, email, password, firstname, lastname, middlename, phoneNumber).collect{
                    if (it.message == "User was created"){
                        _status.value = ApiStatus.COMPLETE
                    }
                    else{
                        _status.value = ApiStatus.FAILED
                    }
                }
            } catch (e: HttpException){
                _status.value = ApiStatus.FAILED
            }
        }
    }
}