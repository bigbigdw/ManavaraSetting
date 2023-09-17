package com.bigbigdw.manavarasetting.main.event

sealed interface EventFCM{
    object Loaded: EventFCM
}

data class StateFCM(
    val Loaded: Boolean = false,
)