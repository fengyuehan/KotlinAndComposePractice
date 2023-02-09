package com.example.mvidemo

import javax.inject.Inject

interface ITodoRepository {
    suspend fun getToDos():List<Todo>
}

class TodoRepository @Inject constructor(
    private val todoMapper: TodoMapper,
    private val todoDataSource: TodoDataSource
):ITodoRepository{
    override suspend fun getToDos(): List<Todo> {
        return todoDataSource.getToDos().map(todoMapper::mapFromLocal)
    }

}