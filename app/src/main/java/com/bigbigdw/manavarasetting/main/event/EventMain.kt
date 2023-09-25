package com.bigbigdw.manavarasetting.main.event

sealed interface EventMain {
    object Loaded : EventMain
    class GetDataStoreWorker(
        val timeTest: String,
        val testBest: String,
        val testJson: String,
        val testTrophy: String,
        val statusTest: String,
        val statusBet: String,
        val statusJson: String,
        val statusTrophy: String,
        val timeMillTest: String,
        val timeMillBest: String,
        val timeMillJson: String,
        val timeMillTrophy: String
    ) : EventMain

    class GetDataStoreFCM(
        val countTest: String,
        val countTodayTest: String,
        val countBest: String,
        val countTodayBest: String,
        val countJson: String,
        val countTodayJson: String,
        val countTrophy: String,
        val countTodayTrophy: String,
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
    val countTest: String = "",
    val countTodayTest: String = "",
    val countBest: String = "",
    val countTodayBest: String = "",
    val countJson: String = "",
    val countTodayJson: String = "",
    val countTrophy: String = "",
    val countTodayTrophy: String = "",
    val timeMillTest: String = "",
    val timeMillBest: String = "",
    val timeMillJson: String = "",
    val timeMillTrophy: String = "",
)