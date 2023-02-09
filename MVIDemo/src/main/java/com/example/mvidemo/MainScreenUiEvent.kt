package com.example.mvidemo

import javax.annotation.concurrent.Immutable


@Immutable
sealed class MainScreenUiEvent:UiEvent {
    data class showData(val items: List<MainScreenItem>):MainScreenUiEvent()
    data class OnChangeDialogState(val show: Boolean) : MainScreenUiEvent()
    data class AddNewItem(val text: String) : MainScreenUiEvent()
    data class OnItemCheckedChanged(val index: Int, val isChecked: Boolean) : MainScreenUiEvent()
    object DismissDialog : MainScreenUiEvent()
}

@Immutable
data class MainScreenState(
    val isLoading: Boolean,
    val data: List<MainScreenItem>,
    val isShowAddDialog: Boolean,
) : UiState {

    companion object {
        fun initial() = MainScreenState(
            isLoading = true,
            data = emptyList(),
            isShowAddDialog = false
        )
    }

    override fun toString(): String {
        return "isLoading: $isLoading, data.size: ${data.size}, isShowAddDialog: $isShowAddDialog"
    }
}