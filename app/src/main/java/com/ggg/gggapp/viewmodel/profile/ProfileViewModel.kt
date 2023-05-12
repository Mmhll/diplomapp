package com.ggg.gggapp.viewmodel.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ggg.gggapp.remote_model.UpdatePasswordRequest
import com.ggg.gggapp.remote_model.UpdateUserRequest
import com.ggg.gggapp.repository.UserRepository
import com.ggg.gggapp.utils.ApiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ProfileViewModel @Inject constructor(@Named("userRepository") private val repository: UserRepository) : ViewModel(){
    private val _isDataUpdated : MutableLiveData<Boolean> = MutableLiveData()
    val isDataUpdated get() = _isDataUpdated
    private val _isPasswordUpdated : MutableLiveData<Boolean> = MutableLiveData()
    val isPasswordUpdated get() = _isPasswordUpdated
    private val _updateStatus = MutableLiveData<ApiStatus>()
    val updateStatus : LiveData<ApiStatus>
        get() = _updateStatus

    fun setUserData(token: String, request: UpdateUserRequest) {
        viewModelScope.launch {
            _updateStatus.value = ApiStatus.LOADING
            try {
            repository.updateUserData(token, request).collect {
                _updateStatus.value = ApiStatus.COMPLETE
            }
            } catch (e: HttpException){
                _updateStatus.value = ApiStatus.FAILED
            }
        }
    }

    fun setUserPassword(token: String, request: UpdatePasswordRequest){
        viewModelScope.launch {
            _updateStatus.value = ApiStatus.LOADING
            try {
                repository.updateUserPassword(token, request).collect {
                    _updateStatus.value = ApiStatus.COMPLETE
                }
            } catch (e: HttpException){
                _updateStatus.value = ApiStatus.FAILED
            }
        }
    }
}