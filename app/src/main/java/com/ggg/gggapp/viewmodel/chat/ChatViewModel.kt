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
class ChatViewModel @Inject constructor(
    @Named("chatRepository") private val chatRepository: ChatRepository,
) : ViewModel() {
    private val _status: MutableLiveData<ApiStatus> = MutableLiveData()
    val status : LiveData<ApiStatus> get() = _status
    private val _chats: MutableLiveData<List<Chat>> = MutableLiveData()
    val chats: LiveData<List<Chat>> get() = _chats

    fun getChats(token: String, id: Long){
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            chatRepository.getChats(token, id).collect{
                try {
                    _chats.value = it
                    _status.value =ApiStatus.COMPLETE
                } catch (e: HttpException){
                    _status.value = ApiStatus.FAILED
                }
            }
        }
    }
}