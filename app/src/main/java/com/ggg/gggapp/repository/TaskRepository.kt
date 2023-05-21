package com.ggg.gggapp.repository

import com.ggg.gggapp.model.Task
import com.ggg.gggapp.remote_model.CreateTaskRequest
import com.ggg.gggapp.remote_model.MessageResponse
import com.ggg.gggapp.remote_model.TaskAndStatusRequest
import com.ggg.gggapp.remote_model.UpdateTaskRequest
import com.ggg.gggapp.remote_model.UserAndTaskRequest
import com.ggg.gggapp.service.TaskService
import com.ggg.gggapp.utils.generateToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Named

class TaskRepository @Inject constructor(@Named("taskService") val service: TaskService) {
    fun getTasksMember(token: String, id: Long): Flow<ArrayList<Task>> {
        return flow {
            emit(service.getTasksMember(generateToken(token), id.toString()))
        }
    }

    fun getTasksExecutor(token: String, id: Long): Flow<ArrayList<Task>> {
        return flow {
            emit(service.getTasksExecutor(generateToken(token), id.toString()))
        }
    }

    fun getTasksCreator(token: String, id: Long): Flow<ArrayList<Task>> {
        return flow {
            emit(service.getTasksCreator(generateToken(token), id.toString()))
        }
    }

    fun getTask(token: String, id: Long): Flow<Task> {
        return flow {
            emit(service.getTask(generateToken(token), id.toString()))
        }
    }

    fun updateStatus(token: String, taskId: Long, status: String): Flow<MessageResponse> {
        return flow {
            emit(service.updateStatus(generateToken(token), TaskAndStatusRequest(taskId, status)))
        }
    }

    fun deleteTask(token: String, taskId: Long): Flow<MessageResponse> {
        return flow {
            emit(service.deleteTask(generateToken(token), taskId.toString()))
        }
    }

    fun updateExecutor(token: String, taskId: Long, executorId: Long): Flow<MessageResponse> {
        return flow {
            emit(
                service.updateExecutor(
                    generateToken(token),
                    UserAndTaskRequest(taskId, executorId)
                )
            )
        }
    }

    fun updateTask(
        token: String,
        taskId: Long,
        name: String,
        description: String,
        deadline: String
    ): Flow<MessageResponse> {
        return flow {
            emit(
                service.updateTask(
                    generateToken(token),
                    UpdateTaskRequest(
                        deadline = deadline,
                        description = description,
                        name = name,
                        task_id = taskId
                    )
                )
            )
        }
    }


    fun addMember(token: String, user_id: Long, task_id: Long): Flow<MessageResponse> {
        return flow {
            emit(service.addMember(token, UserAndTaskRequest(task_id, user_id)))
        }
    }

    fun saveTask(
        token: String,
        creation_date: String,
        creator_id: Long,
        deadline: String?,
        description: String,
        executor_id: Long,
        members: List<Long>,
        task_name: String
    ): Flow<MessageResponse> {
        return flow{
            emit(service.saveTask(generateToken(token), CreateTaskRequest(
                creation_date, creator_id, deadline, description, executor_id, members, task_name
            )))
        }
    }

    fun deleteMember(token: String, user_id: Long, task_id: Long): Flow<MessageResponse>{
        return flow {
            emit(service.deleteMember(generateToken(token), userId = user_id.toString(), taskId = task_id.toString()))
        }
    }
}