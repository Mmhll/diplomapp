package com.ggg.gggapp.viewmodel.common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ggg.gggapp.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommonServiceUserViewModel @Inject constructor() : ViewModel(
) {
    val user: MutableLiveData<User> = MutableLiveData()
}