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
class TasksViewModel @Inject constructor(@Named("taskRepository") private val taskRepository: TaskRepository): ViewModel(){
    private val _tasks: MutableLiveData<ArrayList<Task>> = MutableLiveData()
    val tasks: LiveData<ArrayList<Task>> get() = _tasks
    private val _taskStatus: MutableLiveData<ApiStatus> = MutableLiveData()
    val taskStatus: LiveData<ApiStatus> get() = _taskStatus

    fun getTasks(selectedItem: Int, token: String, user_id: Long){
        viewModelScope.launch {
            _taskStatus.value = ApiStatus.LOADING
            try {
                when (selectedItem){
                    0 -> {
                        taskRepository.getTasksCreator(token, user_id).collect{
                            _tasks.value = it
                            _taskStatus.value = ApiStatus.COMPLETE
                        }
                    }
                    1 -> {
                        taskRepository.getTasksExecutor(token, user_id).collect {
                            _tasks.value = it
                            _taskStatus.value = ApiStatus.COMPLETE
                        }
                    }
                    2 -> {
                        taskRepository.getTasksMember(token, user_id).collect {
                            _tasks.value = it
                            _taskStatus.value = ApiStatus.COMPLETE
                        }
                    }
                }
            } catch (e: HttpException){
                _taskStatus.value = ApiStatus.FAILED
            }
        }
    }

}