package com.ggg.gggapp.viewmodel.common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ggg.gggapp.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommonTaskViewModel @Inject constructor(): ViewModel() {
    var task = MutableLiveData<Task>()
}