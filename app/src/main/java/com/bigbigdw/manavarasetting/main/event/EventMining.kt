package com.bigbigdw.manavarasetting.main.event

import androidx.work.WorkManager

sealed interface EventMining{
    object Loaded: EventMining
}

data class StateMining(
    val Loaded: Boolean = false
)