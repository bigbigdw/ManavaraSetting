package com.bigbigdw.manavarasetting.util

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.bigbigdw.manavarasetting.main.model.ItemBookInfo
import com.bigbigdw.manavarasetting.main.model.ItemBestInfo
import com.bigbigdw.manavarasetting.main.model.ItemKeyword
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

@SuppressLint("SuspiciousIndentation")
fun convertItemBook(bestItemData: ItemBookInfo): JsonObject {
    val jsonObject = JsonObject()
    jsonObject.addProperty("writer", bestItemData.writer)
    jsonObject.addProperty("title", bestItemData.title)
    jsonObject.addProperty("bookImg", bestItemData.bookImg)
    jsonObject.addProperty("bookCode", bestItemData.bookCode)
    jsonObject.addProperty("platform", bestItemData.platform)
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
    jsonObject.addProperty("genre", bestItemData.genre)
    jsonObject.addProperty("belong", bestItemData.belong)
    return jsonObject
}

@SuppressLint("SuspiciousIndentation")
fun convertItemBookJson(jsonObject: JSONObject): ItemBookInfo {

    return ItemBookInfo(
        writer = jsonObject.optString("writer"),
        title = jsonObject.optString("title"),
        bookImg = jsonObject.optString("bookImg"),
        bookCode = jsonObject.optString("bookCode"),
        platform = jsonObject.optString("platform"),
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
        belong = jsonObject.optString("belong"),
    )
}

fun convertItemBestJson(jsonObject: JSONObject): ItemBestInfo {

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
        date = jsonObject.optString("date"),
    )
}

fun convertItemBest(bestItemData: ItemBestInfo): JsonObject {
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
    jsonObject.addProperty("date", bestItemData.date)
    return jsonObject
}

fun convertItemKeywordJson(itemKeyword: ItemKeyword): JsonObject {
    val jsonObject = JsonObject()

    jsonObject.addProperty("key", itemKeyword.key)
    jsonObject.addProperty("value", itemKeyword.value)
    jsonObject.addProperty("date", itemKeyword.date)
    return jsonObject
}


fun setItemBestInfoRef(ref: MutableMap<String?, Any>): ItemBestInfo {
    return ItemBestInfo(
        number = ref["number"] as Int,
        point = ref["point"] as Int,
        cntPageRead = ref["cntPageRead"] as String? ?: "",
        cntFavorite = ref["cntFavorite"] as String? ?: "",
        cntRecom = ref["cntRecom"] as String? ?: "",
        cntTotalComment = ref["cntTotalComment"] as String? ?: "",
        total = ref["total"] as Int,
        totalCount = ref["totalCount"] as Int,
        bookCode = ref["bookCode"] as String,
        currentDiff = ref["currentDiff"] as Int,
        date = ref["date"] as String,
    )
}

fun setItemBookInfoRef(ref: MutableMap<String?, Any>): ItemBookInfo {
    return ItemBookInfo(
        writer = ref["writerName"] as String,
        title = ref["subject"] as String,
        bookImg = ref["bookImg"] as String,
        bookCode = ref["bookCode"] as String,
        number = ref["number"] as Int,
        point = ref["point"] as Int,
        platform = ref["platform"] as String,
        intro = ref["intro"] as String? ?: "",
        cntPageRead = ref["cntPageRead"] as String? ?: "",
        cntFavorite = ref["cntFavorite"] as String? ?: "",
        cntRecom = ref["cntRecom"] as String? ?: "",
        cntChapter = ref["cntChapter"] as String? ?: "",
        cntTotalComment = ref["cntTotalComment"] as String? ?: "",
        total = ref["total"] as Int,
        totalCount = ref["totalCount"] as Int,
        totalWeek = ref["totalWeek"] as Int,
        totalWeekCount = ref["totalWeekCount"] as Int,
        totalMonth = ref["totalMonth"] as Int,
        totalMonthCount = ref["totalMonthCount"] as Int,
        currentDiff = ref["currentDiff"] as Int,
        genre = ref["genre"] as String? ?: "",
        belong = ref["belong"] as String? ?: "",
    )
}

@SuppressLint("SuspiciousIndentation")
fun convertItemKeyword(jsonObject: JSONObject): ItemKeyword {

    return ItemKeyword(
        key = jsonObject.optString("key"),
        value = jsonObject.optString("value"),
        date = jsonObject.optString("date")
    )
}


fun setDataStore(data: String) {
    val mRootRef = FirebaseDatabase.getInstance().reference.child("WORKER")

    val year = DBDate.dateMMDDHHMM().substring(0, 4)
    val month = DBDate.dateMMDDHHMM().substring(4, 6)
    val day = DBDate.dateMMDDHHMM().substring(6, 8)
    val hour = DBDate.dateMMDDHHMM().substring(8, 10)
    val min = DBDate.dateMMDDHHMM().substring(10, 12)

    val child = data.replace(" 최신화 완료", "")

    mRootRef.child(child).setValue("${year}.${month}.${day} ${hour}:${min}")
}

fun checkMiningTrophyValue(yesterDayItem: ItemBookInfo): ItemBookInfo {

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

fun getBookCount(context: Context, type: String, platform: String) {
    val mRootRef =
        FirebaseDatabase.getInstance().reference.child("BOOK").child(type).child(platform)

    mRootRef.addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {

            val dataStore = DataStoreManager(context)

            if (dataSnapshot.exists()) {
                CoroutineScope(Dispatchers.IO).launch {
                    if (type == "NOVEL") {
                        dataStore.setDataStoreString(
                            getPlatformDataKeyNovel(platform),
                            dataSnapshot.childrenCount.toString()
                        )
                    } else {
                        dataStore.setDataStoreString(
                            getPlatformDataKeyComic(platform),
                            dataSnapshot.childrenCount.toString()
                        )
                    }
                }
            } else {
                Log.d("HIHIHI", "FAIL == NOT EXIST")
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    })
}

fun saveBook(platform: String, type: String, child : String = "BOOK") {
    val mRootRef =
        FirebaseDatabase.getInstance().reference.child(child).child(type).child(platform)

    val storage = Firebase.storage
    val storageRef = storage.reference

    val book = storageRef.child("${platform}/${type}/${child}/${platform}.json")

    mRootRef.addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {

            Log.d("HIHI", "TEST")

            if (dataSnapshot.exists()) {

                val jsonArray = JsonObject()

                try {

                    for (snapshot in dataSnapshot.children) {
                        val key = snapshot.key
                        val value = snapshot.value

                        if (key != null && value != null) {

                            val item: ItemBookInfo? = snapshot.getValue(ItemBookInfo::class.java)
                            jsonArray.add(item?.bookCode, item?.let { convertItemBook(it) })
                        }
                    }

                    book.putBytes(jsonArray.toString().toByteArray(Charsets.UTF_8))
                        .addOnSuccessListener {
                            Log.d("HIHI", "saveBook addOnSuccessListener == $it")
                        }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            } else {
                Log.d("HIHI", "dataSnapshot.exists() == NULL")
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    })
}

fun saveGenreKeyword(platform: String, type: String, child : String = "DATA") {
    val mRootRef =
        FirebaseDatabase.getInstance().reference.child(child).child(type).child(platform)

    val storage = Firebase.storage
    val storageRef = storage.reference

    val book = storageRef.child("${platform}/${type}/${child}/${platform}.json")

    mRootRef.addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {

                val jsonObject = JsonObject()

                try {

                    for (snapshot in dataSnapshot.children) {

                        for(item in snapshot.children){
                            val key = item.key
                            val value = item.value

                            if (key != null && value != null) {

                                if(child == "DATA"){
                                    val dataItem: ItemBookInfo? = item.getValue(ItemBookInfo::class.java)
                                    jsonObject.add(dataItem?.bookCode, dataItem?.let { convertItemBook(it) })
                                } else {
                                    val genreKeywordItem: ItemKeyword? = item.getValue(ItemKeyword::class.java)
                                    jsonObject.add(genreKeywordItem?.key, genreKeywordItem?.let { convertItemKeywordJson(it) })
                                }
                            }
                        }
                    }

                    book.putBytes(jsonObject.toString().toByteArray(Charsets.UTF_8))
                        .addOnSuccessListener {
                            Log.d("HIHI", "saveBook addOnSuccessListener == $it")
                        }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    })
}

@OptIn(DelicateCoroutinesApi::class)
fun saveKeyword(
    context: Context,
    platform: String,
    type: String,
    itemBookInfoList: JsonArray,
    callback: (MutableMap<String, String>) -> Unit
) {

    Log.d("MINING", "saveKeyword PLATFORM == $platform")

    val list = JSONArray(itemBookInfoList.toString())
    val keywordList = mutableMapOf<String, String>()

    itemBookInfoList.forEachIndexed { i, _ ->
        val item = convertItemBestJson(list.getJSONObject(i))
        GlobalScope.launch {
            getKeyword(
                context = context,
                platform = platform,
                bookCode = item.bookCode
            ){

                it.forEach { (key, value) ->
                    synchronized(keywordList) {
                        keywordList[key] = keywordList[key]?.let { "$it, $value" } ?: value
                    }
                }

                if(i == itemBookInfoList.size() - 1){
                    BestRef.setBestGenre(
                        platform = platform,
                        genreDate = "KEYWORD_DAY",
                        type = type,
                    ).setValue(keywordList)

                    BestRef.setBestGenre(
                        platform = platform,
                        genreDate = "KEYWORD_WEEK",
                        type = type,
                    ).child(DBDate.getDayOfWeekAsNumber().toString()).setValue(keywordList)

                    BestRef.setBestGenre(
                        platform = platform,
                        genreDate = "KEYWORD_MONTH",
                        type = type,
                    ).child(DBDate.datedd()).setValue(keywordList)

                    callback(keywordList)
                }
            }
        }
    }


}

fun changeUserState(UID: String, status: String) {

    val result = if (status != "ALLOW") {
        "ALLOW"
    } else {
        "LOCKED"
    }

    FirebaseDatabase.getInstance().reference.child("USER").child(UID).child("USERINFO")
        .child("userStatus").setValue(result)
}

fun getNextNovelInListEng(currentNovel: String): String {
    val novelList = miningListAllEng()
    val currentIndex = novelList.indexOf(currentNovel)

    return if (currentIndex != -1 && currentIndex < novelList.size - 1) {
        // 현재 아이템이 리스트에 있고, 마지막 아이템이 아닌 경우
        novelList[currentIndex + 1]
    } else {
        // 현재 아이템이 리스트에 없거나, 마지막 아이템인 경우
        ""
    }
}

fun findIndexInNovelList(target: String): Int {
    val novelList = miningListAllEng()
    return novelList.indexOf(target)
}

