package com.ggg.gggapp.viewmodel.common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ggg.gggapp.model.Chat
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommonChatViewModel @Inject constructor(): ViewModel() {
    var chat = MutableLiveData<Chat>()
}