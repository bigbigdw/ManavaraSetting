package com.bigbigdw.manavarasetting.util

import android.annotation.SuppressLint
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.util.Log
import com.bigbigdw.manavarasetting.firebase.FCMAlert
import com.bigbigdw.manavarasetting.main.model.ItemBookInfo
import com.bigbigdw.manavarasetting.main.model.ItemBestInfo
import com.bigbigdw.manavarasetting.main.viewModels.DataStoreManager
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
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
    if(genre == "ALL"){
        return "ALL"
    } else if(genre == "99"){
        return "MELO"
    } else if(genre == "93"){
        return "DRAMA"
    } else if(genre == "90"){
        return "YOUNG"
    } else if(genre == "88"){
        return "ACTION"
    }else if(genre == "107"){
        return "BL"
    } else {
        return "없음"
    }
}

fun getNaverSeriesGenreKor(genre : String) : String {
    if(genre == "ALL"){
        return "전체"
    } else if(genre == "99"){
        return "멜로"
    } else if(genre == "93"){
        return "드라마"
    } else if(genre == "90"){
        return "소년"
    } else if(genre == "88"){
        return "액션"
    }else if(genre == "107"){
        return "BL"
    } else {
        return "없음"
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


fun setDataStore(activity: String){
    val mRootRef = FirebaseDatabase.getInstance().reference.child("WORKER")

    val year = DBDate.dateMMDDHHMM().substring(0,4)
    val month = DBDate.dateMMDDHHMM().substring(4,6)
    val day = DBDate.dateMMDDHHMM().substring(6,8)
    val hour = DBDate.dateMMDDHHMM().substring(8,10)
    val min = DBDate.dateMMDDHHMM().substring(10,12)

    var currentUser :  FirebaseUser? = null
    currentUser = Firebase.auth.currentUser

    if(activity.contains("TROPHY")){

        mRootRef.child("WORKER_TROPHY").setValue("${year}.${month}.${day} ${hour}:${min}")
        mRootRef.child("UID_TROPHY").setValue(currentUser?.uid ?: "NONE")

    } else if(activity.contains("JSON")){

        mRootRef.child("WORKER_JSON").setValue("${year}.${month}.${day} ${hour}:${min}")
        mRootRef.child("UID_JSON").setValue(currentUser?.uid ?: "NONE")

    } else if(activity.contains("BEST")){

        mRootRef.child("WORKER_BEST").setValue("${year}.${month}.${day} ${hour}:${min}")
        mRootRef.child("UID_BEST").setValue(currentUser?.uid ?: "NONE")
    } else {
        mRootRef.child("WORKER_TEST").setValue("${year}.${month}.${day} ${hour}:${min}")
        mRootRef.child("UID_TEST").setValue(currentUser?.uid ?: "NONE")
    }
}

fun updateWorker(context: Context, update: () -> Unit){
    val workerRef = FirebaseDatabase.getInstance().reference.child("WORKER")

    workerRef.addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {

                val dataStore = DataStoreManager(context)

                val workerTest: String? = dataSnapshot.child("WORKER_TEST").getValue(String::class.java)
                val workerBest: String? = dataSnapshot.child("WORKER_BEST").getValue(String::class.java)
                val workerJson: String? = dataSnapshot.child("WORKER_JSON").getValue(String::class.java)
                val workerTrophy: String? = dataSnapshot.child("WORKER_TROPHY").getValue(String::class.java)

                CoroutineScope(Dispatchers.IO).launch {
                    dataStore.setDataStoreString(DataStoreManager.TEST_TIME, workerTest ?: "")
                    dataStore.setDataStoreString(DataStoreManager.BESTWORKER_TIME, workerBest ?: "")
                    dataStore.setDataStoreString(DataStoreManager.JSONWORKER_TIME, workerJson ?: "")
                    dataStore.setDataStoreString(DataStoreManager.TROPHYWORKER_TIME, workerTrophy ?: "")
                    update()
                }

            } else {
                Log.d("HIHI", "FALSE")
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    })
}

fun updateFcmCount(context: Context, update: () -> Unit){
    val mRootRef = FirebaseDatabase.getInstance().reference.child("MESSAGE").child("ALERT")

    val year = DBDate.dateMMDDHHMM().substring(0,4)
    val month = DBDate.dateMMDDHHMM().substring(4,6)
    val day = DBDate.dateMMDDHHMM().substring(6,8)

    var numFcm = 0
    var numFcmToday = 0
    var numBest = 0
    var numBestToday = 0
    var numJson = 0
    var numJsonToday = 0
    var numTrophy = 0
    var numTrophyToday = 0

    val dataStore = DataStoreManager(context)

    mRootRef.addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if(dataSnapshot.exists()){

                for(item in dataSnapshot.children){
                    val fcm: FCMAlert? = dataSnapshot.child(item.key ?: "").getValue(FCMAlert::class.java)

                    if(fcm?.body?.contains("테스트") == true){
                        numFcm += 1

                        if(fcm.body.contains("${year}.${month}.${day}")){
                            numFcmToday += 1
                        }
                    } else if (fcm?.activity?.contains("BEST") == true) {
                        numBest += 1

                        if (fcm.body.contains("${year}.${month}.${day}")) {
                            numBestToday += 1
                        }
                    } else if (fcm?.activity?.contains("JSON") == true) {

                        numJson += 1

                        if (fcm.body.contains("${year}.${month}.${day}")) {
                            numJsonToday += 1
                        }
                    } else if (fcm?.activity?.contains("TROPHY") == true) {
                        numTrophy += 1

                        if (fcm.body.contains("${year}.${month}.${day}")) {
                            numTrophyToday += 1
                        }
                    } else {
                        Log.d("HIHIHIHI", "item = $item")
                    }

                }

                CoroutineScope(Dispatchers.IO).launch {
                    dataStore.setDataStoreString(DataStoreManager.FCM_COUNT_TEST, numFcm.toString())
                    dataStore.setDataStoreString(DataStoreManager.FCM_COUNT_TEST_TODAY, numFcmToday.toString())
                    dataStore.setDataStoreString(DataStoreManager.FCM_COUNT_BEST, numBest.toString())
                    dataStore.setDataStoreString(DataStoreManager.FCM_COUNT_BEST_TODAY, numBestToday.toString())
                    dataStore.setDataStoreString(DataStoreManager.FCM_COUNT_JSON, numJson.toString())
                    dataStore.setDataStoreString(DataStoreManager.FCM_COUNT_JSON_TODAY, numJsonToday.toString())
                    dataStore.setDataStoreString(DataStoreManager.FCM_COUNT_TROPHY, numTrophy.toString())
                    dataStore.setDataStoreString(DataStoreManager.FCM_COUNT_TROPHY_TODAY, numTrophyToday.toString())
                    update()
                }

            } else {
                Log.d("HIHI", "FALSE")
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    })
}




