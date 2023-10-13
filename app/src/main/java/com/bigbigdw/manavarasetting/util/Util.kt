package com.bigbigdw.manavarasetting.util

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.bigbigdw.manavarasetting.main.model.ItemBookInfo
import com.bigbigdw.manavarasetting.main.model.ItemBestInfo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

val WeekKor = arrayListOf(
    "일",
    "월",
    "화",
    "수",
    "목",
    "금",
    "토",
)

fun novelListEng(): List<String> {
    return listOf(
        "JOARA",
        "JOARA_NOBLESS",
        "JOARA_PREMIUM",
        "NAVER_SERIES",
        "NAVER_CHALLENGE",
        "NAVER_BEST",
        "NAVER_WEBNOVEL_PAY",
        "NAVER_WEBNOVEL_FREE",
        "KAKAO_STAGE",
        "ONESTORY_FANTAGY",
        "ONESTORY_ROMANCE",
        "ONESTORY_PASS_FANTAGY",
        "ONESTORY_PASS_ROMANCE",
        "MUNPIA",
        "TOKSODA",
    )
}


@SuppressLint("SuspiciousIndentation")
fun convertItemBook(bestItemData : ItemBookInfo) : JsonObject {
    val jsonObject = JsonObject()
        jsonObject.addProperty("writer", bestItemData.writer)
        jsonObject.addProperty("title", bestItemData.title)
        jsonObject.addProperty("bookImg", bestItemData.bookImg)
        jsonObject.addProperty("bookCode", bestItemData.bookCode)
        jsonObject.addProperty("type", bestItemData.type)
        jsonObject.addProperty("intro", bestItemData.intro)
        jsonObject.addProperty("cntPageRead", bestItemData.cntPageRead)
        jsonObject.addProperty("cntFavorite", bestItemData.cntFavorite)
        jsonObject.addProperty("cntRecom", bestItemData.cntRecom)
        jsonObject.addProperty("cntTotalComment", bestItemData.cntTotalComment)
        jsonObject.addProperty("cntChapter", bestItemData.cntChapter)
        jsonObject.addProperty("number", bestItemData.number)
        jsonObject.addProperty("point", bestItemData.point)
        jsonObject.addProperty("total", bestItemData.total)
        jsonObject.addProperty("totalCount", bestItemData.totalCount)
        jsonObject.addProperty("totalWeek", bestItemData.totalWeek)
        jsonObject.addProperty("totalWeekCount", bestItemData.totalWeekCount)
        jsonObject.addProperty("totalMonth", bestItemData.totalMonth)
        jsonObject.addProperty("totalMonthCount", bestItemData.totalMonthCount)
        jsonObject.addProperty("currentDiff", bestItemData.currentDiff)
    return jsonObject
}

@SuppressLint("SuspiciousIndentation")
fun convertItemBookJson(jsonObject: JSONObject): ItemBookInfo {

    return ItemBookInfo(
        writer = jsonObject.optString("writer"),
        title = jsonObject.optString("title"),
        bookImg = jsonObject.optString("bookImg"),
        bookCode = jsonObject.optString("bookCode"),
        type = jsonObject.optString("type"),
        intro = jsonObject.optString("intro"),
        cntPageRead = jsonObject.optString("cntPageRead"),
        cntFavorite = jsonObject.optString("cntFavorite"),
        cntRecom = jsonObject.optString("cntRecom"),
        cntTotalComment = jsonObject.optString("cntTotalComment"),
        cntChapter = jsonObject.optString("cntChapter"),
        point = jsonObject.optInt("point"),
        number = jsonObject.optInt("number"),
        total = jsonObject.optInt("total"),
        totalCount = jsonObject.optInt("totalCount"),
        totalWeek = jsonObject.optInt("totalWeek"),
        totalWeekCount = jsonObject.optInt("totalWeekCount"),
        totalMonth = jsonObject.optInt("totalMonth"),
        totalMonthCount = jsonObject.optInt("totalMonthCount"),
        currentDiff = jsonObject.optInt("currentDiff"),
    )
}

fun convertItemBestJson(jsonObject : JSONObject) : ItemBestInfo {

    return ItemBestInfo(
        point = jsonObject.optInt("point"),
        number = jsonObject.optInt("number"),
        cntPageRead = jsonObject.optString("cntPageRead"),
        cntFavorite = jsonObject.optString("cntFavorite"),
        cntRecom = jsonObject.optString("cntRecom"),
        cntTotalComment = jsonObject.optString("cntTotalComment"),
        total = jsonObject.optInt("total"),
        totalCount = jsonObject.optInt("totalCount"),
        bookCode = jsonObject.optString("bookCode"),
        currentDiff = jsonObject.optInt("currentDiff"),
    )
}

fun convertItemBest(bestItemData : ItemBestInfo) : JsonObject {
    val jsonObject = JsonObject()
    jsonObject.addProperty("number", bestItemData.number)
    jsonObject.addProperty("point", bestItemData.point)
    jsonObject.addProperty("cntPageRead", bestItemData.cntPageRead)
    jsonObject.addProperty("cntFavorite", bestItemData.cntFavorite)
    jsonObject.addProperty("cntRecom", bestItemData.cntRecom)
    jsonObject.addProperty("cntTotalComment", bestItemData.cntTotalComment)
    jsonObject.addProperty("total", bestItemData.total)
    jsonObject.addProperty("totalCount", bestItemData.totalCount)
    jsonObject.addProperty("bookCode", bestItemData.bookCode)
    jsonObject.addProperty("currentDiff", bestItemData.currentDiff)
    return jsonObject
}


fun setDataStore(data: String){
    val mRootRef = FirebaseDatabase.getInstance().reference.child("WORKER")

    val year = DBDate.dateMMDDHHMM().substring(0,4)
    val month = DBDate.dateMMDDHHMM().substring(4,6)
    val day = DBDate.dateMMDDHHMM().substring(6,8)
    val hour = DBDate.dateMMDDHHMM().substring(8,10)
    val min = DBDate.dateMMDDHHMM().substring(10,12)

    val child = data.replace(" 최신화 완료", "")

    mRootRef.child(child).setValue("${year}.${month}.${day} ${hour}:${min}")
}

fun getDataStoreStatus(context: Context, update : () -> Unit){
    val mRootRef = FirebaseDatabase.getInstance().reference.child("WORKER")

    mRootRef.addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if(dataSnapshot.exists()){

                val dataStore = DataStoreManager(context)

                CoroutineScope(Dispatchers.IO).launch {
                    dataStore.setDataStoreString(DataStoreManager.MINING_NAVER_SERIES_COMIC, dataSnapshot.child("MINING_NAVER_SERIES_COMIC").getValue(String::class.java) ?: "")
                    dataStore.setDataStoreString(DataStoreManager.MINING_NAVER_SERIES_NOVEL, dataSnapshot.child("MINING_NAVER_SERIES_NOVEL").getValue(String::class.java) ?: "")
                    dataStore.setDataStoreString(DataStoreManager.MINING_JOARA_NOVEL, dataSnapshot.child("MINING_JOARA_NOVEL").getValue(String::class.java) ?: "")
                    dataStore.setDataStoreString(DataStoreManager.MINING_JOARA_PREMIUM_NOVEL, dataSnapshot.child("MINING_JOARA_PREMIUM_NOVEL").getValue(String::class.java) ?: "")
                    dataStore.setDataStoreString(DataStoreManager.MINING_JOARA_NOBLESS_NOVEL, dataSnapshot.child("MINING_JOARA_NOBLESS_NOVEL").getValue(String::class.java) ?: "")
                    dataStore.setDataStoreString(DataStoreManager.MINING_NAVER_CHALLENGE_NOVEL, dataSnapshot.child("MINING_NAVER_CHALLENGE_NOVEL").getValue(String::class.java) ?: "")
                    dataStore.setDataStoreString(DataStoreManager.MINING_NAVER_BEST_NOVEL, dataSnapshot.child("MINING_NAVER_BEST_NOVEL").getValue(String::class.java) ?: "")
                    dataStore.setDataStoreString(DataStoreManager.MINING_NAVER_WEBNOVEL_PAY_NOVEL, dataSnapshot.child("MINING_NAVER_WEBNOVEL_PAY_NOVEL").getValue(String::class.java) ?: "")
                    dataStore.setDataStoreString(DataStoreManager.MINING_NAVER_WEBNOVEL_FREE_NOVEL, dataSnapshot.child("MINING_NAVER_WEBNOVEL_FREE_NOVEL").getValue(String::class.java) ?: "")
                    dataStore.setDataStoreString(DataStoreManager.MINING_RIDI_FANTAGY_NOVEL, dataSnapshot.child("MINING_RIDI_FANTAGY_NOVEL").getValue(String::class.java) ?: "")
                    dataStore.setDataStoreString(DataStoreManager.MINING_RIDI_ROMANCE_NOVEL, dataSnapshot.child("MINING_RIDI_ROMANCE_NOVEL").getValue(String::class.java) ?: "")
                    dataStore.setDataStoreString(DataStoreManager.MINING_ONESTORY_FANTAGY_NOVEL, dataSnapshot.child("MINING_ONESTORY_FANTAGY_NOVEL").getValue(String::class.java) ?: "")
                    dataStore.setDataStoreString(DataStoreManager.MINING_ONESTORY_ROMANCE_NOVEL, dataSnapshot.child("MINING_ONESTORY_ROMANCE_NOVEL").getValue(String::class.java) ?: "")
                    dataStore.setDataStoreString(DataStoreManager.MINING_ONESTORY_PASS_FANTAGY_NOVEL, dataSnapshot.child("MINING_ONESTORY_PASS_FANTAGY_NOVEL").getValue(String::class.java) ?: "")
                    dataStore.setDataStoreString(DataStoreManager.MINING_ONESTORY_PASS_ROMANCE_NOVEL, dataSnapshot.child("MINING_ONESTORY_PASS_ROMANCE_NOVEL").getValue(String::class.java) ?: "")
                    dataStore.setDataStoreString(DataStoreManager.MINING_KAKAO_STAGE_NOVEL, dataSnapshot.child("MINING_KAKAO_STAGE_NOVEL").getValue(String::class.java) ?: "")
                    dataStore.setDataStoreString(DataStoreManager.MINING_MUNPIA_NOVEL, dataSnapshot.child("MINING_MUNPIA_NOVEL").getValue(String::class.java) ?: "")
                    dataStore.setDataStoreString(DataStoreManager.MINING_TOKSODA_NOVEL, dataSnapshot.child("MINING_TOKSODA_NOVEL").getValue(String::class.java) ?: "")

                    update()
                }

            } else {
                Log.d("HIHI", "FALSE")
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    })
}

fun checkMiningTrophyValue(yesterDayItem: ItemBookInfo) : ItemBookInfo{

    yesterDayItem.totalWeek = if (DBDate.getYesterdayDayOfWeek() == 7) {
        0
    } else {
        yesterDayItem.totalWeek
    }

    yesterDayItem.totalWeekCount = if (DBDate.getYesterdayDayOfWeek() == 7) {
        0
    } else {
        yesterDayItem.totalWeekCount
    }

    yesterDayItem.totalMonth = if (DBDate.datedd() == "01") {
        0
    } else {
        yesterDayItem.totalMonth
    }

    yesterDayItem.totalMonthCount = if (DBDate.getYesterdayDayOfWeek() == 7) {
        0
    } else {
        yesterDayItem.totalMonthCount
    }

    return yesterDayItem
}

