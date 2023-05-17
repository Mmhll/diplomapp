package com.ggg.gggapp.di

import com.ggg.gggapp.repository.MessageRepository
import com.ggg.gggapp.service.MessageService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MessageModule {
    @Provides
    @Singleton
    @Named("messageService")
    fun provideService(@Named("retrofit") retrofit: Retrofit): MessageService =
        retrofit.create(MessageService::class.java)

    @Provides
    @Singleton
    @Named("messageRepository")
    fun provideRepository(@Named("messageService") service: MessageService): MessageRepository =
        MessageRepository(service)
}