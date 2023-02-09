package com.example.mvidemo

import javax.inject.Inject

class MainScreenViewDataMapper @Inject constructor() {
    fun buildScreen(todos: List<Todo>): List<MainScreenItem> {
        val viewData = mutableListOf<MainScreenItem>()
        viewData.addAll(todos.map { todo ->
            MainScreenItem.MainScreenTodoItem(
                todo.isChecked,
                todo.text
            )
        })
        viewData.add(MainScreenItem.MainScreenAddButtonItem)
        return viewData
    }
}