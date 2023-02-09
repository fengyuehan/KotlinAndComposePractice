package com.example.mvidemo

sealed class MainScreenItem {
    object MainScreenAddButtonItem:MainScreenItem()
    data class MainScreenTodoItem(
        val isChecked: Boolean,
        val text: String,
    ):MainScreenItem()
}