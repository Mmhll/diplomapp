package com.ggg.gggapp.viewmodel.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ggg.gggapp.model.Chat
import com.ggg.gggapp.repository.ChatRepository
import com.ggg.gggapp.repository.MessageRepository
import com.ggg.gggapp.utils.ApiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class OneChatViewModel @Inject constructor(
    @Named("chatRepository") val repository: ChatRepository,
    @Named("messageRepository") val messageRepository: MessageRepository
) : ViewModel() {
    private var undestroyed = true
    private val _chat = MutableLiveData<Chat>()
    val chat: LiveData<Chat> get() = _chat
    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus> get() = _status
    private val _messageStatus = MutableLiveData<ApiStatus>()
    val messageStatus get() = _messageStatus
    fun clear() = onCleared()
    fun getChat(id: Long, token: String) {

        viewModelScope.launch {
            while (undestroyed) {
                _status.value = ApiStatus.LOADING
                try {
                    repository.getChat(token, id).collect {
                        _chat.value = it
                        _status.value = ApiStatus.COMPLETE
                    }
                } catch (e: HttpException) {
                    _status.value = ApiStatus.FAILED
                }

                delay(1500)
            }
        }
    }

    fun sendMessage(token: String, user_id: Long, message: String, chat_id: Long) {
        viewModelScope.launch {
            _messageStatus.value = ApiStatus.LOADING
            try {
                messageRepository.sendMessage(token, user_id, message, chat_id).collect {
                    if (it.message == "Message was saved") {
                        _messageStatus.value = ApiStatus.COMPLETE
                    } else {
                        _messageStatus.value = ApiStatus.FAILED
                    }
                }
            } catch (e: HttpException){
                _messageStatus.value = ApiStatus.FAILED
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        undestroyed = false
    }
}