package com.ggg.gggapp.viewmodel.service

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ggg.gggapp.model.User
import com.ggg.gggapp.repository.UserRepository
import com.ggg.gggapp.utils.ApiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class UsersViewModel @Inject constructor(@Named("userRepository") private val userRepository: UserRepository) :
    ViewModel() {
    private val _users: MutableLiveData<ArrayList<User>> = MutableLiveData()
    val users: LiveData<ArrayList<User>> get() = _users
    private val _userStatus: MutableLiveData<ApiStatus> = MutableLiveData()
    val userStatus: LiveData<ApiStatus> get() = _userStatus

    fun findAll(token: String) {
        viewModelScope.launch {
            _userStatus.value = ApiStatus.LOADING
            try {
                userRepository.getUsers(token).collect {
                    Log.e("TAGUS", it.toString())
                    _users.value = it
                    _userStatus.value = ApiStatus.COMPLETE
                }
            } catch (e: HttpException) {
                _userStatus.value = ApiStatus.FAILED
            }
        }
    }
}