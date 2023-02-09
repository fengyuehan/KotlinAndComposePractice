package com.example.mvidemo.di

import com.example.mvidemo.ITodoRepository
import com.example.mvidemo.TodoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {
    @Binds
    @ViewModelScoped
    fun bindToDoRepository(repository: TodoRepository):ITodoRepository
}