package com.bigbigdw.manavarasetting.main.event

import com.bigbigdw.manavarasetting.firebase.FCMAlert
import com.bigbigdw.manavarasetting.main.model.ItemBookInfo
import com.bigbigdw.manavarasetting.main.model.ItemBestInfo
import com.bigbigdw.manavarasetting.main.model.ItemKeyword

sealed interface EventMain {
    object Loaded : EventMain

    class SetFcmAlertList(
        val fcmAlertList : ArrayList<FCMAlert> = ArrayList(),
    ) : EventMain

    class SetFcmNoticeList(
        val fcmNoticeList : ArrayList<FCMAlert> = ArrayList(),
    ) : EventMain

    class SetBestBookList(
        val bestBookList : ArrayList<ItemBookInfo> = ArrayList(),
    ) : EventMain

    class SetBestBookWeekList(
        val bestListWeek : ArrayList<ArrayList<ItemBookInfo>> = ArrayList(),
    ) : EventMain

    class SetTrophyList(
        val trophyList : ArrayList<ItemBestInfo> = ArrayList(),
    ) : EventMain

    class SetFCMList(
        val fcmBestList : ArrayList<FCMAlert> = ArrayList(),
        val fcmJsonList : ArrayList<FCMAlert> = ArrayList(),
        val fcmTrophyList : ArrayList<FCMAlert> = ArrayList(),
        val fcmBestCount : Int = 0,
        val fcmJsonCount : Int = 0,
        val fcmTrophyCount : Int = 0,
    ) : EventMain

    class SetGenreDay(
        val genreDay : ArrayList<ItemKeyword> = ArrayList()
    ) : EventMain

    class SetKeywordDay(
        val keywordDay : ArrayList<ItemKeyword> = ArrayList()
    ) : EventMain

    class SetGenreWeek(
        val genreDay : ArrayList<ItemKeyword> = ArrayList(),
        val genreDayList : ArrayList<ArrayList<ItemKeyword>> = ArrayList()
    ) : EventMain

    class SetKeywordWeek(
        val keywordDay : ArrayList<ItemKeyword> = ArrayList(),
        val keywordDayList : ArrayList<ArrayList<ItemKeyword>> = ArrayList()
    ) : EventMain

    class SetUserList(
        val userList : ArrayList<UserInfo> = ArrayList()
    ) : EventMain
}

data class StateMain(
    val Loaded: Boolean = false,
    val fcmAlertList : ArrayList<FCMAlert> = ArrayList(),
    val fcmNoticeList : ArrayList<FCMAlert> = ArrayList(),
    val bestBookList : ArrayList<ItemBookInfo> = ArrayList(),
    val fcmBestList : ArrayList<FCMAlert> = ArrayList(),
    val fcmJsonList : ArrayList<FCMAlert> = ArrayList(),
    val fcmTrophyList : ArrayList<FCMAlert> = ArrayList(),
    val bestListWeek : ArrayList<ArrayList<ItemBookInfo>> = ArrayList(),
    val trophyList : ArrayList<ItemBestInfo> = ArrayList(),
    val fcmBestCount : Int = 0,
    val fcmJsonCount : Int = 0,
    val fcmTrophyCount : Int = 0,
    val genreDay : ArrayList<ItemKeyword> = ArrayList(),
    val genreDayList : ArrayList<ArrayList<ItemKeyword>> = ArrayList(),
    val userList : ArrayList<UserInfo> = ArrayList(),
    val keywordDay : ArrayList<ItemKeyword> = ArrayList(),
    val keywordDayList : ArrayList<ArrayList<ItemKeyword>> = ArrayList()
)

data class UserInfo (
    var userNickName: String = "",
    var userEmail: String = "",
    var userFcmToken : String = "",
    var userUID : String = "",
    var userStatus : String = "LOCKED",
)
