package com.umc.halo.presentation.base

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<STATE,EVENT>(initialState: STATE): ViewModel() {

    //state
    protected val _uiState = MutableStateFlow(initialState) //StateFlow로 UI 상태 관리
    val uiState: StateFlow<STATE> = _uiState.asStateFlow()

    protected val currentState: STATE //현재 UI 상태 저장 변수
        get() = _uiState.value

    protected fun updateState(reducer: @Composable STATE.() -> STATE) { //UI 상태 업데이트 함수
        _uiState.update { currentState.reducer() }
    }

    //event
    abstract fun onEvent(event: EVENT)
}