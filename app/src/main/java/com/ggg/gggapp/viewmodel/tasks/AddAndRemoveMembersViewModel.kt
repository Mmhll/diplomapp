package com.ggg.gggapp.viewmodel.tasks

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ggg.gggapp.model.Task
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
class AddAndRemoveMembersViewModel @Inject constructor(
    @Named("taskRepository") private val taskRepository: TaskRepository,
    @Named("userRepository") private val userRepository: UserRepository
) : ViewModel() {
    private val _task: MutableLiveData<Task> = MutableLiveData()
    val task: LiveData<Task> get() = _task
    private val _users: MutableLiveData<ArrayList<User>> = MutableLiveData()
    val users: LiveData<ArrayList<User>> get() = _users

    private val _taskStatus: MutableLiveData<ApiStatus> = MutableLiveData()
    val taskStatus: LiveData<ApiStatus> get() = _taskStatus

    private val _apiStatus: MutableLiveData<ApiStatus> = MutableLiveData()
    val apiStatus: LiveData<ApiStatus> get() = _apiStatus


    private val _internalStatus: MutableLiveData<ApiStatus> = MutableLiveData()

    private val _internalDeleteStatus: MutableLiveData<ApiStatus> = MutableLiveData()

    private val _userStatus: MutableLiveData<ApiStatus> = MutableLiveData()
    val userStatus: LiveData<ApiStatus> get() = _userStatus


    fun addMembers(token: String, taskId: Long, membersIds: ArrayList<Long>) {
        viewModelScope.launch {
            _apiStatus.value = ApiStatus.LOADING
            try {
                for (i in membersIds) {
                    taskRepository.addMember(token, i, taskId).collect { messageResponse ->
                        Log.e("MESSAGE", messageResponse.message)
                        if (messageResponse.message != "User was added to task") {
                            _internalStatus.value = ApiStatus.FAILED
                        }
                    }
                    if (_internalStatus.value == ApiStatus.FAILED) {
                        _apiStatus.value = ApiStatus.FAILED
                        break
                    }
                }
                if (_internalStatus.value != ApiStatus.FAILED) {
                    _apiStatus.value = ApiStatus.COMPLETE
                }
            } catch (e: HttpException) {
                _apiStatus.value = ApiStatus.FAILED
            }
        }
    }

    fun deleteMembers(token: String, taskId: Long, membersIds: ArrayList<Long>) {
        viewModelScope.launch {
            _apiStatus.value = ApiStatus.LOADING
            try {
                Log.e("TASK_ID", taskId.toString())
                Log.e("USER_IDS", membersIds.toString())
                for (i in membersIds) {
                    taskRepository.deleteMember(token, i, taskId).collect { messageResponse ->
                        Log.e("MESSAGE", messageResponse.message)
                        if (messageResponse.message != "User was deleted from task") {
                            _internalDeleteStatus.value = ApiStatus.FAILED
                        }
                    }
                    if (_internalDeleteStatus.value == ApiStatus.FAILED) {
                        _apiStatus.value = ApiStatus.FAILED
                        break
                    }
                }
                if (_internalDeleteStatus.value != ApiStatus.FAILED) {
                    _apiStatus.value = ApiStatus.COMPLETE
                }
            } catch (e: HttpException) {
                _apiStatus.value = ApiStatus.FAILED
            }
        }
    }

    fun getUsers(token: String) {
        viewModelScope.launch {
            _userStatus.value = ApiStatus.LOADING
            try {
                userRepository.getUsers(token).collect {
                    _users.value = it
                    _userStatus.value = ApiStatus.COMPLETE
                }
            } catch (e: HttpException) {
                _userStatus.value = ApiStatus.FAILED
            }
        }
    }

}