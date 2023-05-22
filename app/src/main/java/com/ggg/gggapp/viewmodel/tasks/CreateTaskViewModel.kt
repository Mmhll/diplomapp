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
class CreateTaskViewModel @Inject constructor(@Named("taskRepository") private val taskRepository: TaskRepository): ViewModel() {

    private val _apiStatus: MutableLiveData<ApiStatus> = MutableLiveData()
    private val apiStatus: LiveData<ApiStatus> get() = _apiStatus

    fun createTask(token: String, creationDate: String, taskName: String, creatorId: Long, description: String, deadline: String?, executor: Long, members: ArrayList<Long>){
        viewModelScope.launch {
            _apiStatus.value = ApiStatus.LOADING
            try {
                taskRepository.saveTask(
                    token,
                    creationDate,
                    creatorId,
                    deadline,
                    description,
                    executor,
                    members,
                    taskName
                ).collect {
                    if (it.message == "Task was saved") {
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