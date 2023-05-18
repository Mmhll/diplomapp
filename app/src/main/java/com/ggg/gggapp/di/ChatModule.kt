package com.ggg.gggapp.di

import com.ggg.gggapp.repository.ChatRepository
import com.ggg.gggapp.service.ChatService
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ChatModule {
    @Provides
    @Singleton
    @Named("chatService")
    fun provideService(@Named("retrofit") retrofit: Retrofit): ChatService =
        retrofit.create(ChatService::class.java)

    @Provides
    @Named("chatRepository")
    fun provideRepository(@Named("chatService") service: ChatService): ChatRepository =
        ChatRepository(service)
}