package com.ggg.gggapp.di

import com.ggg.gggapp.repository.RoleRepository
import com.ggg.gggapp.service.RoleService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoleModule {
    @Provides
    @Singleton
    @Named("roleService")
    fun provideService(@Named("retrofit") retrofit: Retrofit): RoleService =
        retrofit.create(RoleService::class.java)

    @Provides
    @Named("roleRepository")
    fun provideRepository(@Named("roleService") service: RoleService): RoleRepository =
        RoleRepository(service)
}