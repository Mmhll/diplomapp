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
class CreateChatViewModel @Inject constructor(
    @Named("userRepository") private val userRepository: UserRepository,
    @Named("chatRepository") private val chatRepository: ChatRepository
) : ViewModel() {
    private val _users: MutableLiveData<ArrayList<User>> = MutableLiveData()
    val users: LiveData<ArrayList<User>> get() = _users
    private val _userStatus: MutableLiveData<ApiStatus> = MutableLiveData()
    val userStatus: LiveData<ApiStatus> get() = _userStatus
    private val _chatStatus: MutableLiveData<ApiStatus> = MutableLiveData()
    val chatStatus: LiveData<ApiStatus> get() = _chatStatus

    fun getUsers(token: String){
        viewModelScope.launch {
            _userStatus.value = ApiStatus.LOADING
            try {
                userRepository.getUsers(token).collect {
                    _users.value = it
                    _userStatus.value = ApiStatus.COMPLETE
                }
            } catch (e: HttpException){
                _userStatus.value = ApiStatus.FAILED
            }
        }
    }

    fun createChat(token: String, user_ids: ArrayList<Long>, name: String){
        viewModelScope.launch {
            _chatStatus.value = ApiStatus.LOADING
            try {
                chatRepository.createChat(token, name, user_ids).collect{
                    _chatStatus.value = ApiStatus.COMPLETE
                    if (it.message != "Chat was created"){
                        ApiStatus.FAILED
                    }
                }
            } catch (e: HttpException){
                _chatStatus.value = ApiStatus.FAILED
            }
        }
    }

}