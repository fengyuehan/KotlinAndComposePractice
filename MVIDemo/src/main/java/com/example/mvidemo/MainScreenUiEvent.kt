package com.example.mvidemo

import javax.annotation.concurrent.Immutable

//Compose 会针对稳定类型进行编译期优化，通过对输入参数的比较跳过不必要的重组
//通过手动添加 @Stable 或者 @Immutable 注解让编译器将其看待为稳定类型，@Immutable 代表类型完全不可变，@Stable 代表类型虽然可变但是变化可追踪。
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