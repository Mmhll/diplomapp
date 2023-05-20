package com.ggg.gggapp.di

import com.ggg.gggapp.repository.ChatRepository
import com.ggg.gggapp.repository.TaskRepository
import com.ggg.gggapp.service.ChatService
import com.ggg.gggapp.service.TaskService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TaskModule {
    @Provides
    @Singleton
    @Named("taskService")
    fun provideService(@Named("retrofit") retrofit: Retrofit): TaskService =
        retrofit.create(TaskService::class.java)

    @Provides
    @Named("taskRepository")
    fun provideRepository(@Named("taskService") service: TaskService): TaskRepository =
        TaskRepository(service)
}