package com.narde.gestionaleosteopatabetto.ui.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Base ViewModel implementing MVI pattern
 * 
 * MVI (Model-View-Intent) Pattern:
 * - Intent: User actions/events that trigger state changes
 * - Model: The state that represents the UI
 * - View: The UI that renders the state and sends intents
 * 
 * Benefits:
 * - Unidirectional data flow
 * - Predictable state management
 * - Easy to test and debug
 * - Clear separation of concerns
 */
abstract class BaseViewModel<Intent, State> : ViewModel() {
    
    // Intent flow - user actions/events
    private val _intents = MutableSharedFlow<Intent>()
    val intents: SharedFlow<Intent> = _intents.asSharedFlow()
    
    // State flow - current UI state
    private val _state = MutableStateFlow(initialState())
    val state: StateFlow<State> = _state.asStateFlow()
    
    // Side effects flow - one-time events like navigation, showing messages
    private val _sideEffects = MutableSharedFlow<SideEffect>()
    val sideEffects: SharedFlow<SideEffect> = _sideEffects.asSharedFlow()
    
    init {
        // Process intents when they arrive
        viewModelScope.launch {
            intents.collect { intent ->
                processIntent(intent)
            }
        }
    }
    
    /**
     * Send an intent/event to the ViewModel
     */
    fun sendIntent(intent: Intent) {
        viewModelScope.launch {
            _intents.emit(intent)
        }
    }
    
    /**
     * Emit a side effect (one-time event)
     */
    protected fun emitSideEffect(sideEffect: SideEffect) {
        viewModelScope.launch {
            _sideEffects.emit(sideEffect)
        }
    }
    
    /**
     * Update the current state
     */
    protected fun updateState(newState: State) {
        _state.value = newState
    }
    
    /**
     * Update state using a reducer function
     */
    protected fun updateState(reducer: State.() -> State) {
        _state.value = _state.value.reducer()
    }
    
    /**
     * Define the initial state
     */
    protected abstract fun initialState(): State
    
    /**
     * Process incoming intents and update state accordingly
     */
    protected abstract suspend fun processIntent(intent: Intent)
}

/**
 * Base class for side effects (one-time events)
 */
sealed class SideEffect {
    data class ShowMessage(val message: String) : SideEffect()
    data class NavigateTo(val destination: String) : SideEffect()
    data class ShowError(val error: String) : SideEffect()
    object NavigateBack : SideEffect()
}
