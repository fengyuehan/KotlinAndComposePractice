package com.example.mvidemo

import io.realm.BuildConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class Reducer<S :UiState,E:UiEvent>(init:S){
    private val _state:MutableStateFlow<S> = MutableStateFlow(init)
    val state:StateFlow<S>
        get() = _state

    val timeCapsule: TimeCapsule<S> = TimeTravelCapsule { storedState ->
        _state.tryEmit(storedState)
    }

    init {
        timeCapsule.addState(init)
    }

    fun sendEvent(event: E){
        reduce(_state.value,event)
    }

    fun setState(newState:S){
        val success = _state.tryEmit(newState)

        if (BuildConfig.DEBUG && success){
            timeCapsule.addState(newState)
        }
    }

    abstract fun reduce(value: S, event: E)
}

interface UiState

interface UiEvent