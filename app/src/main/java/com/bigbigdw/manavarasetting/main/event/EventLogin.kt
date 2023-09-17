package com.bigbigdw.manavarasetting.main.event

sealed interface EventLogin{
    object Loaded: EventLogin
}

data class StateLogin(
    val Loaded: Boolean = false,
)