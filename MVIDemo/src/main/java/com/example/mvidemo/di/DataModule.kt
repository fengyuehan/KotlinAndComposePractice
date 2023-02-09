package com.example.mvidemo.di

import com.example.mvidemo.TodoDataSource
import com.example.mvidemo.TodoLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface DataModule {
    @Binds
    @ViewModelScoped
    fun bindTodoDataSource(todoDataSource: TodoLocalDataSource): TodoDataSource
}