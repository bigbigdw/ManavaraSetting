package com.bigbigdw.manavarasetting.main.event

sealed interface EventMain{
    object Loaded: EventMain
}

data class StateMain(
    val Loaded: Boolean = false,
)