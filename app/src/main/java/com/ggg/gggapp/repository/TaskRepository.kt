package com.ggg.gggapp.repository

import com.ggg.gggapp.model.Task
import com.ggg.gggapp.service.TaskService
import com.ggg.gggapp.utils.generateToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Named

class TaskRepository @Inject constructor(@Named("taskService") val service: TaskService) {
    fun getTasksMember(token: String, id: Long): Flow<ArrayList<Task>>{
        return flow {
            emit(service.getTasksMember(generateToken(token), id.toString()))
        }
    }
    fun getTasksExecutor(token: String, id: Long): Flow<ArrayList<Task>>{
        return flow {
            emit(service.getTasksExecutor(generateToken(token), id.toString()))
        }
    }
    fun getTasksCreator(token: String, id: Long): Flow<ArrayList<Task>>{
        return flow {
            emit(service.getTasksCreator(generateToken(token), id.toString()))
        }
    }
    fun getTask(token: String, id: Long): Flow<Task>{
        return flow {
            emit(service.getTask(generateToken(token), id.toString()))
        }
    }

}