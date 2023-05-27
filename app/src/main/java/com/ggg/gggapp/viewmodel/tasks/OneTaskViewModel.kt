package com.ggg.gggapp.viewmodel.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ggg.gggapp.model.Task
import com.ggg.gggapp.repository.TaskRepository
import com.ggg.gggapp.utils.ApiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class OneTaskViewModel @Inject constructor(
    @Named("taskRepository") private val taskRepository: TaskRepository,
) : ViewModel() {
    private val _task: MutableLiveData<Task> = MutableLiveData()
    val task: LiveData<Task> get() = _task
    private val _taskStatus: MutableLiveData<ApiStatus> = MutableLiveData()
    val taskStatus: LiveData<ApiStatus> get() = _taskStatus
    private val _status: MutableLiveData<ApiStatus> = MutableLiveData()
    val status: LiveData<ApiStatus> get() = _status
    private val _deleteStatus: MutableLiveData<ApiStatus> = MutableLiveData()
    val deleteStatus: LiveData<ApiStatus> get() = _deleteStatus


    fun getTask(token: String, taskId: Long) {
        viewModelScope.launch {
            _taskStatus.value = ApiStatus.LOADING
            try {
                taskRepository.getTask(token, taskId).collect {
                    _task.value = it
                    _taskStatus.value = ApiStatus.COMPLETE
                }
            } catch (e: HttpException) {
                _taskStatus.value = ApiStatus.FAILED
            }
        }
    }

    fun updateStatus(token: String, taskId: Long, status: String) {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                taskRepository.updateStatus(token, taskId, status).collect {
                    _status.value = if (it.message == "Task was updated") {
                        ApiStatus.COMPLETE
                    } else {
                        ApiStatus.FAILED
                    }
                }
            } catch (e: HttpException) {
                _status.value = ApiStatus.FAILED
            }
        }
    }

    fun deleteTask(token: String, taskId: Long) {
        viewModelScope.launch {
            _deleteStatus.value = ApiStatus.LOADING
            try {
                taskRepository.deleteTask(token, taskId).collect {
                    if (it.message == "Task was deleted") {
                        _deleteStatus.value = ApiStatus.COMPLETE
                    } else {
                        _deleteStatus.value = ApiStatus.FAILED
                    }
                }
            } catch (e: HttpException) {
                _deleteStatus.value = ApiStatus.FAILED
            }
        }
    }


}