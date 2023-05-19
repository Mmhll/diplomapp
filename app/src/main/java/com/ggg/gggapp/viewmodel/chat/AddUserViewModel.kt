package com.ggg.gggapp.viewmodel.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ggg.gggapp.model.User
import com.ggg.gggapp.repository.ChatRepository
import com.ggg.gggapp.repository.UserRepository
import com.ggg.gggapp.utils.ApiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class AddUserViewModel @Inject constructor(
    @Named("userRepository") private val userRepository: UserRepository,
    @Named("chatRepository") private val chatRepository: ChatRepository
) : ViewModel() {
    private val _users: MutableLiveData<ArrayList<User>> = MutableLiveData()
    val users: LiveData<ArrayList<User>> get() = _users
    private val _userStatus: MutableLiveData<ApiStatus> = MutableLiveData()
    val userStatus: LiveData<ApiStatus> get() = _userStatus
    private val _chatStatus: MutableLiveData<ApiStatus> = MutableLiveData()
    val chatStatus: LiveData<ApiStatus> get() = _chatStatus
    private val _internalStatus: MutableLiveData<ApiStatus> = MutableLiveData()

    fun getUsers(token: String) {
        viewModelScope.launch {
            _userStatus.value = ApiStatus.LOADING
            try {
                userRepository.getUsers(token).collect {
                    _users.value = it
                    _userStatus.value = ApiStatus.COMPLETE
                }
            } catch (e: HttpException) {
                _userStatus.value = ApiStatus.FAILED
            }
        }
    }

    fun addUser(token: String, user_ids: ArrayList<Long>, chat_id: Long){
        viewModelScope.launch {
            _chatStatus.value = ApiStatus.LOADING
            try {
                for (i in user_ids){
                    chatRepository.addMember(token, chat_id, i).collect { messageResponse ->
                        if (messageResponse.message != "User was added") {
                            _internalStatus.value = ApiStatus.FAILED
                        }
                    }
                    if (_internalStatus.value == ApiStatus.FAILED){
                        _chatStatus.value = ApiStatus.FAILED
                        break
                    }
                }
                if (_internalStatus.value != ApiStatus.FAILED){
                    _chatStatus.value = ApiStatus.COMPLETE
                }
            } catch (e: HttpException) {
                _chatStatus.value = ApiStatus.FAILED
            }
        }
    }
}