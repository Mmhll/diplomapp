package com.ggg.gggapp.viewmodel.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ggg.gggapp.repository.TaskRepository
import com.ggg.gggapp.utils.ApiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class EditTaskViewModel @Inject constructor(@Named("taskRepository") private val repository: TaskRepository) :
    ViewModel() {
    private val _apiStatus: MutableLiveData<ApiStatus> = MutableLiveData()
    val apiStatus: LiveData<ApiStatus> get() = _apiStatus

    fun updateTask(
        token: String,
        taskId: Long,
        name: String,
        description: String,
        deadline: String
    ) {
        viewModelScope.launch {
            _apiStatus.value = ApiStatus.LOADING
            try {
                repository.updateTask(token, taskId, name, description, deadline).collect{
                    if (it.message == "Task was updated") {
                        _apiStatus.value = ApiStatus.COMPLETE
                    } else {
                        _apiStatus.value = ApiStatus.FAILED
                    }
                }
            } catch (e: HttpException){
                _apiStatus.value = ApiStatus.FAILED
            }
        }
    }
}