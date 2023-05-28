package com.ggg.gggapp.viewmodel.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ggg.gggapp.model.Role
import com.ggg.gggapp.remote_model.UpdatePasswordRequest
import com.ggg.gggapp.remote_model.UpdateUserRequest
import com.ggg.gggapp.repository.RoleRepository
import com.ggg.gggapp.repository.UserRepository
import com.ggg.gggapp.utils.ApiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class OneUserViewModel @Inject constructor(
    @Named("userRepository") private val repository: UserRepository,
    @Named("roleRepository") private val roleRepository: RoleRepository
) : ViewModel() {
    private val _updateStatus = MutableLiveData<ApiStatus>()
    val updateStatus: LiveData<ApiStatus>
        get() = _updateStatus
    private val _deleteStatus = MutableLiveData<ApiStatus>()
    val deleteStatus: LiveData<ApiStatus> get() = _deleteStatus

    private val _roles: MutableLiveData<ArrayList<Role>> = MutableLiveData()
    val roles: LiveData<ArrayList<Role>> get() = _roles
    private val _roleStatus: MutableLiveData<ApiStatus> = MutableLiveData()
    val roleStatus: LiveData<ApiStatus> get() = _roleStatus
    private val _roleUpdateStatus: MutableLiveData<ApiStatus> = MutableLiveData()
    val roleUpdateStatus: LiveData<ApiStatus> get() = _roleUpdateStatus


    fun setUserData(token: String, request: UpdateUserRequest) {
        viewModelScope.launch {
            _updateStatus.value = ApiStatus.LOADING
            try {
                repository.updateUserData(token, request).collect {
                    _updateStatus.value = ApiStatus.COMPLETE
                }
            } catch (e: HttpException) {
                _updateStatus.value = ApiStatus.FAILED
            }
        }
    }

    fun setUserPassword(token: String, request: UpdatePasswordRequest) {
        viewModelScope.launch {
            _updateStatus.value = ApiStatus.LOADING
            try {
                repository.updateUserPassword(token, request).collect {
                    _updateStatus.value = ApiStatus.COMPLETE
                }
            } catch (e: HttpException) {
                _updateStatus.value = ApiStatus.FAILED
            }
        }
    }

    fun deleteUser(token: String, id: String) {
        viewModelScope.launch {
            _deleteStatus.value = ApiStatus.LOADING
            try {
                repository.deleteUser(token, id).collect {
                    if (it.message == "User was successfully deleted") {
                        _deleteStatus.value = ApiStatus.COMPLETE
                    } else {
                        _deleteStatus.value = ApiStatus.FAILED
                    }
                }
            } catch (e: HttpException) {
                _deleteStatus.value = ApiStatus.FAILED
            }
        }
    }


    fun findAll(token: String) {
        viewModelScope.launch {
            _roleStatus.value = ApiStatus.LOADING
            try {
                roleRepository.getRoles(token).collect {
                    _roles.value = it
                    _roleStatus.value = ApiStatus.COMPLETE
                }
            } catch (e: HttpException) {
                _roleStatus.value = ApiStatus.FAILED
            }
        }
    }

    fun updateUserRole(token: String, userId: Long, roleId: Long) {
        viewModelScope.launch {
            _roleUpdateStatus.value = ApiStatus.LOADING
            try {
                repository.updateUserRole(token, userId, roleId).collect {
                    if (it.message == "User role was updated") {
                        _roleUpdateStatus.value = ApiStatus.COMPLETE
                    }
                }
            } catch (e: HttpException) {
                _roleUpdateStatus.value = ApiStatus.FAILED
            }
        }
    }
}