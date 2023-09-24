package com.bigbigdw.manavarasetting.main.event

sealed interface EventMain {
    object Loaded : EventMain
    class GetDataStore(
        val timeTest: String,
        val testBest: String,
        val testJson: String,
        val testTrophy: String,
        val statusTest: String,
        val statusBet: String,
        val statusJson: String,
        val statusTrophy: String
    ) : EventMain
}

data class StateMain(
    val Loaded: Boolean = false,
    val timeTest: String = "",
    val testBest: String = "",
    val testJson: String = "",
    val testTrophy: String = "",
    val statusTest: String = "",
    val statusBest: String = "",
    val statusJson: String = "",
    val statusTrophy: String = "",
)