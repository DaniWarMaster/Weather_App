package com.example.android.common_view

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class BaseViewModel<Event: UiEvent, State: UiState, Effect : UiEffect> : ViewModel() {

    private val initialState : State by lazy { createInitialState() }
    abstract fun createInitialState() : State

    val currentState: State
        get() = uiState.value

    private val _uiState: MutableStateFlow<State> = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()
    val event = _event.asSharedFlow()

    private val _effect: Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()

    init {
        subscribeEvents()
    }

    private fun subscribeEvents() {
        viewModelScope.launch {
            event.collect {
                handleEvent(it)
            }
        }
    }

    abstract fun handleEvent(event: Event)

    fun setUiEvent(newEvent: Event) {
        viewModelScope.launch {
            Log.d("BaseViewModel", "setUiEvent: EventTriggered $newEvent")
            _event.emit(newEvent)
        }
    }

    fun setUiState(reduce: State.() -> State) {
        val newState = currentState.reduce()
        Log.d("BaseViewModel", "setUiState_old: ${_uiState.value}")
        Log.d("BaseViewModel", "setUiState_new: $newState")
        _uiState.value = newState
    }

    protected fun setUiEffect(builder: () -> Effect) {
        val effectValue = builder()
        viewModelScope.launch {
            _effect.send(effectValue)
        }
    }
}