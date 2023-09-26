package com.bigbigdw.manavarasetting.main.event

import com.bigbigdw.manavarasetting.firebase.FCMAlert
import com.bigbigdw.manavarasetting.main.model.BestItemData

sealed interface EventMain {
    object Loaded : EventMain
    class GetDataStoreWorker(
        val timeTest: String,
        val timeBest: String,
        val timeJson: String,
        val timeTrophy: String,
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

    class SetFcmAlertList(
        val fcmAlertList : ArrayList<FCMAlert> = ArrayList(),
    ) : EventMain

    class SetFcmNoticeList(
        val fcmNoticeList : ArrayList<FCMAlert> = ArrayList(),
    ) : EventMain

    class SetBestBookList(
        val setBestBookList : ArrayList<BestItemData> = ArrayList(),
    ) : EventMain
}

data class StateMain(
    val Loaded: Boolean = false,
    val timeTest: String = "",
    val timeBest: String = "",
    val timeJson: String = "",
    val timeTrophy: String = "",
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
    val fcmAlertList : ArrayList<FCMAlert> = ArrayList(),
    val fcmNoticeList : ArrayList<FCMAlert> = ArrayList(),
    val setBestBookList : ArrayList<BestItemData> = ArrayList(),
)