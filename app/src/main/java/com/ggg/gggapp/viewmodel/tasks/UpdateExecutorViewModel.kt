package com.ggg.gggapp.viewmodel.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ggg.gggapp.model.User
import com.ggg.gggapp.repository.TaskRepository
import com.ggg.gggapp.repository.UserRepository
import com.ggg.gggapp.utils.ApiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class UpdateExecutorViewModel @Inject constructor(
    @Named("taskRepository") private val taskRepository: TaskRepository,
    @Named("userRepository") private val userRepository: UserRepository
) : ViewModel() {
    private val _users: MutableLiveData<ArrayList<User>> = MutableLiveData()
    val users: LiveData<ArrayList<User>> get() = _users
    private val _userStatus: MutableLiveData<ApiStatus> = MutableLiveData()
    val userStatus: LiveData<ApiStatus> get() = _userStatus
    private val _status: MutableLiveData<ApiStatus> = MutableLiveData()
    val status: LiveData<ApiStatus> get() = _status

    fun updateExecutor(token: String, taskId: Long, executorId: Long) {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                taskRepository.updateExecutor(token, taskId, executorId).collect {
                    if (it.message == "Executor was updated") {
                        _status.value = ApiStatus.COMPLETE
                    } else {
                        _status.value = ApiStatus.FAILED
                    }
                }
            } catch (e: HttpException) {
                _status.value = ApiStatus.FAILED
            }
        }
    }

    fun getUsers(token: String) {
        viewModelScope.launch {
            _userStatus.value = ApiStatus.LOADING
            userRepository.getUsers(token).collect {
                _users.value = it
                _userStatus.value = ApiStatus.COMPLETE
            }
        }
    }

}