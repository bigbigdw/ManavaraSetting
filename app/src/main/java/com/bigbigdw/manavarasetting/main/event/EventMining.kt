package com.bigbigdw.manavarasetting.main.event

sealed interface EventMining{
    object Loaded: EventMining
}

data class StateMining(
    val Loaded: Boolean = false,
)