package com.example.mvidemo.di

import com.example.mvidemo.GetTodosUseCase
import com.example.mvidemo.IGetTodosUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface UseCaseModule {
    @Binds
    @ViewModelScoped
    fun bindTodosUseCase(todosUseCase: GetTodosUseCase): IGetTodosUseCase
}