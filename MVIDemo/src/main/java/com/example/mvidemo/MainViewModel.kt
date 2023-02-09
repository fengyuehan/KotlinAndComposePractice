package com.example.mvidemo

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getToDos:IGetTodosUseCase,
    private val dispatcher: CoroutineDispatcher,
    private val mapper: MainScreenViewDataMapper
) :BaseViewModel<MainScreenState,MainScreenUiEvent>(){
    private val reducer = MainReducer(MainScreenState.initial())

    val timeMachine: TimeCapsule<MainScreenState>
        get() = reducer.timeCapsule

    init {
        viewModelScope.launch(dispatcher) {
            val data = getToDos.invoke()
            sendEvent(MainScreenUiEvent.showData(mapper.buildScreen(data)))
        }
    }

    private fun sendEvent(event: MainScreenUiEvent){
        reducer.sendEvent(event)
    }

    fun changeAddDialogState(show: Boolean) {
        sendEvent(MainScreenUiEvent.OnChangeDialogState(show))
    }

    fun addNewItem(text: String) {
        sendEvent(MainScreenUiEvent.AddNewItem(text))
    }

    fun onItemCheckedChanged(index: Int, isChecked: Boolean) {
        sendEvent(MainScreenUiEvent.OnItemCheckedChanged(index, isChecked))
    }

    class MainReducer(initial: MainScreenState):Reducer<MainScreenState,MainScreenUiEvent>(initial) {
        override fun reduce(oldState: MainScreenState, event: MainScreenUiEvent) {
            when (event) {
                is MainScreenUiEvent.OnChangeDialogState -> {
                    setState(oldState.copy(isShowAddDialog = event.show))
                }
                is MainScreenUiEvent.showData -> {
                    setState(oldState.copy(isLoading = false, data = event.items))
                }
                is MainScreenUiEvent.DismissDialog -> {
                    setState(oldState.copy(isShowAddDialog = false))
                }
                is MainScreenUiEvent.AddNewItem -> {
                    val newList = oldState.data.toMutableList()
                    newList.add(
                        index = oldState.data.size - 1,
                        element = MainScreenItem.MainScreenTodoItem(false, event.text),
                    )
                    setState(oldState.copy(
                        data = newList,
                        isShowAddDialog = false
                    ))
                }
                is MainScreenUiEvent.OnItemCheckedChanged -> {
                    val newList = oldState.data.toMutableList()
                    newList[event.index] = (newList[event.index] as MainScreenItem.MainScreenTodoItem).copy(isChecked = event.isChecked)
                    setState(oldState.copy(data = newList))
                }
            }
        }
        }

    override val state: StateFlow<MainScreenState>
        get() = reducer.state

}
