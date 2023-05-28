package com.ggg.gggapp.viewmodel.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ggg.gggapp.model.Role
import com.ggg.gggapp.repository.RoleRepository
import com.ggg.gggapp.utils.ApiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class RolesViewModel @Inject constructor(@Named("roleRepository") private val repository: RoleRepository) :
    ViewModel() {

    private val _roles: MutableLiveData<ArrayList<Role>> = MutableLiveData()
    val roles: LiveData<ArrayList<Role>> get() = _roles
    private val _roleStatus: MutableLiveData<ApiStatus> = MutableLiveData()
    val roleStatus: LiveData<ApiStatus> get() = _roleStatus
    val _updateStatus: MutableLiveData<ApiStatus> = MutableLiveData()
    val updateStatus: LiveData<ApiStatus> get() = _updateStatus


    fun findAll(token: String) {
        viewModelScope.launch {
            _roleStatus.value = ApiStatus.LOADING
            try {
                repository.getRoles(token).collect {
                    _roles.value = it
                    _roleStatus.value = ApiStatus.COMPLETE
                }
            } catch (e: HttpException) {
                _roleStatus.value = ApiStatus.FAILED
            }
        }
    }

    fun updateRole(token: String, id: Long, roleName: String, permission: String) {
        viewModelScope.launch {
            _updateStatus.value = ApiStatus.LOADING
            try {
                repository.editRole(token, id, roleName, permission).collect {
                    if (it.message == "Role was updated") {
                        _updateStatus.value = ApiStatus.COMPLETE
                    } else {
                        _updateStatus.value = ApiStatus.FAILED
                    }
                }
            } catch (e: HttpException) {
                _updateStatus.value = ApiStatus.FAILED
            }
        }
    }

    fun addRole(token: String, roleName: String, permission: String) {
        viewModelScope.launch {
            _updateStatus.value = ApiStatus.LOADING
            try {
                repository.addRole(token, roleName, permission).collect {
                    if (it.message == "Role has been created") {
                        _updateStatus.value = ApiStatus.COMPLETE
                    } else {
                        _updateStatus.value = ApiStatus.FAILED
                    }
                }
            } catch (e: HttpException) {
                _updateStatus.value = ApiStatus.FAILED
            }
        }
    }

}