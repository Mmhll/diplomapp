package com.ggg.gggapp.viewmodel.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ggg.gggapp.model.Chat
import com.ggg.gggapp.repository.ChatRepository
import com.ggg.gggapp.utils.ApiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ChatUsersViewModel @Inject constructor(@Named("chatRepository") val repository: ChatRepository) :
    ViewModel() {

    private val _chat = MutableLiveData<Chat>()
    val chat: LiveData<Chat> get() = _chat
    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus> get() = _status

    fun getChat(id: Long, token: String) {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                repository.getChat(token, id).collect {
                    _chat.value = it
                    _status.value = ApiStatus.COMPLETE
                }
            } catch (e: HttpException) {
                _status.value = ApiStatus.FAILED
            }
        }
    }

}