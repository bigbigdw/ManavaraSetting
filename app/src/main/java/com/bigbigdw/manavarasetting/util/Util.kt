package com.bigbigdw.manavarasetting.util

import android.annotation.SuppressLint
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.util.Log
import com.bigbigdw.manavarasetting.firebase.FCMAlert
import com.bigbigdw.manavarasetting.main.model.ItemBookInfo
import com.bigbigdw.manavarasetting.main.model.ItemBestInfo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.util.Calendar
import java.util.Date


@SuppressLint("SimpleDateFormat")
fun dateMMDDHHMM(): String {
    val currentTime: Date = Calendar.getInstance().time
    val format = SimpleDateFormat("YYYYMMddHHmm")
    return format.format(currentTime).toString()
}

val NaverSeriesComicGenre = arrayListOf(
    "ALL",
    "99",
    "93",
    "90",
    "88",
    "107",
)

val NaverSeriesNovelGenre = arrayListOf(
    "ALL",
    "201",
    "207",
    "202",
    "208",
    "206",
)

val WeekKor = arrayListOf(
    "일",
    "월",
    "화",
    "수",
    "목",
    "금",
    "토",
)

fun getNaverSeriesGenre(genre : String) : String {
    when (genre) {
        "ALL" -> {
            return "ALL"
        }
        "99" -> {
            return "MELO"
        }
        "93" -> {
            return "DRAMA"
        }
        "90" -> {
            return "YOUNG"
        }
        "88" -> {
            return "ACTION"
        }
        "107" -> {
            return "BL"
        }
        "201" -> {
            return "ROMANCE"
        }
        "207" -> {
            return "ROMANCE_FANTASY"
        }
        "202" -> {
            return "FANTASY"
        }
        "208" -> {
            return "MODERN_FANTASY"
        }
        "206" -> {
            return "MARTIAL_ARTS"
        }
        else -> {
            return "없음"
        }
    }
}

fun getNaverSeriesGenreKor(genre : String) : String {
    return when (genre) {
        "ALL" -> {
            "전체"
        }
        "99" -> {
            "멜로"
        }
        "93" -> {
            "드라마"
        }
        "90" -> {
            "소년"
        }
        "88" -> {
            "액션"
        }
        "107" -> {
            "BL"
        }
        "201" -> {
            "로맨스"
        }
        "207" -> {
            "로판"
        }
        "202" -> {
            "판타지"
        }
        "208" -> {
            "현판"
        }
        "206" -> {
            "무협"
        }
        else -> {
            "없음"
        }
    }
}

@SuppressLint("SuspiciousIndentation")
fun convertBestItemData(bestItemData : ItemBookInfo) : JsonObject {
    val jsonObject = JsonObject()
        jsonObject.addProperty("writer", bestItemData.writer)
        jsonObject.addProperty("title", bestItemData.title)
        jsonObject.addProperty("bookImg", bestItemData.bookImg)
        jsonObject.addProperty("bookCode", bestItemData.bookCode)
        jsonObject.addProperty("type", bestItemData.type)
        jsonObject.addProperty("info1", bestItemData.info1)
        jsonObject.addProperty("info2", bestItemData.info2)
        jsonObject.addProperty("info3", bestItemData.info3)
        jsonObject.addProperty("current", bestItemData.current)
        jsonObject.addProperty("total", bestItemData.total)
        jsonObject.addProperty("totalCount", bestItemData.totalCount)
        jsonObject.addProperty("totalWeek", bestItemData.totalWeek)
        jsonObject.addProperty("totalWeekCount", bestItemData.totalWeekCount)
        jsonObject.addProperty("totalMonth", bestItemData.totalMonth)
        jsonObject.addProperty("totalMonthCount", bestItemData.totalMonthCount)
    return jsonObject
}

@SuppressLint("SuspiciousIndentation")
fun convertBestItemDataJson(jsonObject: JSONObject): ItemBookInfo {

    return ItemBookInfo(
        writer = jsonObject.optString("writer"),
        title = jsonObject.optString("title"),
        bookImg = jsonObject.optString("bookImg"),
        bookCode = jsonObject.optString("bookCode"),
        type = jsonObject.optString("type"),
        info1 = jsonObject.optString("info1"),
        info2 = jsonObject.optString("info2"),
        info3 = jsonObject.optString("info3"),
        current = jsonObject.optInt("current"),
        total = jsonObject.optInt("total"),
        totalCount = jsonObject.optInt("totalCount"),
        totalWeek = jsonObject.optInt("totalWeek"),
        totalWeekCount = jsonObject.optInt("totalWeekCount"),
        totalMonth = jsonObject.optInt("totalMonth"),
        totalMonthCount = jsonObject.optInt("totalMonthCount"),
    )
}

fun convertBestItemDataAnalyzeJson(jsonObject : JSONObject) : ItemBestInfo {

    return ItemBestInfo(
        number = jsonObject.optInt("number"),
        info1 = jsonObject.optString("info1"),
        total = jsonObject.optInt("total"),
        totalCount = jsonObject.optInt("totalCount"),
        bookCode = jsonObject.optString("bookCode"),
    )
}

fun convertBestItemDataAnalyze(bestItemData : ItemBestInfo) : JsonObject {
    val jsonObject = JsonObject()
    jsonObject.addProperty("number", bestItemData.number)
    jsonObject.addProperty("info1", bestItemData.info1)
    jsonObject.addProperty("total", bestItemData.total)
    jsonObject.addProperty("totalCount", bestItemData.totalCount)
    jsonObject.addProperty("bookCode", bestItemData.bookCode)
    return jsonObject
}

fun uploadJsonFile() {
    val storage = Firebase.storage
    val storageRef = storage.reference
    val jsonFileRef = storageRef.child("your_folder/your_json_file.json") // 저장할 경로 및 파일 이름 지정

    val json = Gson().toJson(ItemBookInfo()) // Gson 라이브러리를 사용하여 객체를 JSON으로 변환

    // JSON 문자열을 바이트 배열로 변환
    val jsonBytes = ByteArrayInputStream(json.toByteArray(Charsets.UTF_8))

    val uploadTask = jsonFileRef.putStream(jsonBytes)

    uploadTask.addOnSuccessListener {
        // 업로드 성공 시 처리
    }.addOnFailureListener {
        // 업로드 실패 시 처리
    }
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

fun updateWorker(context: Context, update: () -> Unit){
    val workerRef = FirebaseDatabase.getInstance().reference.child("WORKER")

    workerRef.addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {

                val dataStore = DataStoreManager(context)

                CoroutineScope(Dispatchers.IO).launch {
                    dataStore.setDataStoreString(DataStoreManager.BEST_NAVER_SERIES_COMIC, dataSnapshot.child("BEST_NAVER_SERIES_COMIC").getValue(String::class.java) ?: "")
                    dataStore.setDataStoreString(DataStoreManager.BEST_NAVER_SERIES_NOVEL, dataSnapshot.child("BEST_NAVER_SERIES_NOVEL").getValue(String::class.java) ?: "")

                    dataStore.setDataStoreString(DataStoreManager.JSON_NAVER_SERIES_COMIC, dataSnapshot.child("JSON_NAVER_SERIES_COMIC").getValue(String::class.java) ?: "")
                    dataStore.setDataStoreString(DataStoreManager.JSON_NAVER_SERIES_NOVEL, dataSnapshot.child("JSON_NAVER_SERIES_NOVEL").getValue(String::class.java) ?: "")

                    dataStore.setDataStoreString(DataStoreManager.TROPHY_NAVER_SERIES_COMIC, dataSnapshot.child("TROPHY_NAVER_SERIES_COMIC").getValue(String::class.java) ?: "")
                    dataStore.setDataStoreString(DataStoreManager.TROPHY_NAVER_SERIES_NOVEL, dataSnapshot.child("TROPHY_NAVER_SERIES_NOVEL").getValue(String::class.java) ?: "")

                    update()
                }

            } else {
                Log.d("HIHI", "FALSE")
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    })
}




