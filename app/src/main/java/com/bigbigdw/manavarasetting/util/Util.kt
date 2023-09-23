package com.bigbigdw.manavarasetting.util

import android.annotation.SuppressLint
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.util.Log
import com.bigbigdw.manavarasetting.main.model.BestItemData
import com.bigbigdw.manavarasetting.main.model.BestListAnalyze
import com.bigbigdw.manavarasetting.main.viewModels.DataStoreManager
import com.bigbigdw.manavarasetting.util.DBDate.datedd
import com.bigbigdw.manavarasetting.util.DBDate.getDayOfWeekAsNumber
import com.bigbigdw.manavarasetting.util.DBDate.getYesterdayDayOfWeek
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.ByteArrayInputStream
import java.nio.charset.Charset
import java.util.Calendar
import java.util.Date


@SuppressLint("SimpleDateFormat")
fun dateMMDDHHMM(): String {
    val currentTime: Date = Calendar.getInstance().time
    val format = SimpleDateFormat("YYYYMMddHHmm")
    return format.format(currentTime).toString()
}

val NaverSeriesGenre = arrayListOf(
    "ALL",
    "99",
    "93",
    "90",
    "88",
    "107",
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

@SuppressLint("SuspiciousIndentation")
fun convertBestItemData(bestItemData : BestItemData) : JsonObject {
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

fun convertBestItemDataAnalyze(bestItemData : BestItemData) : JsonObject {
    val jsonObject = JsonObject()
    jsonObject.addProperty("number", bestItemData.current)
    jsonObject.addProperty("info1", bestItemData.info1)
    jsonObject.addProperty("total", bestItemData.total)
    jsonObject.addProperty("totalCount", bestItemData.totalCount)
    return jsonObject
}

fun uploadJsonFile() {
    val storage = Firebase.storage
    val storageRef = storage.reference
    val jsonFileRef = storageRef.child("your_folder/your_json_file.json") // 저장할 경로 및 파일 이름 지정

    val json = Gson().toJson(BestItemData()) // Gson 라이브러리를 사용하여 객체를 JSON으로 변환

    // JSON 문자열을 바이트 배열로 변환
    val jsonBytes = ByteArrayInputStream(json.toByteArray(Charsets.UTF_8))

    val uploadTask = jsonFileRef.putStream(jsonBytes)

    uploadTask.addOnSuccessListener {
        // 업로드 성공 시 처리
    }.addOnFailureListener {
        // 업로드 실패 시 처리
    }
}

fun uploadJsonArrayToStorageWeek(platform : String, genre: String) {
    val storage = Firebase.storage
    val storageRef = storage.reference

    val jsonFileRef = storageRef.child("${platform}/${genre}/WEEK/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json") // 읽어올 JSON 파일의 경로

    // JSON 파일을 다운로드
    jsonFileRef.getBytes(1024 * 1024)
        .addOnSuccessListener { bytes ->
            val jsonString = String(bytes, Charset.forName("UTF-8"))
            val weekArray = JsonParser().parse(jsonString).asJsonArray

            makeWeekJson(platform = platform, genre = genre, jsonArray = weekArray)
        }

        .addOnFailureListener {

            val jsonArray = JsonArray()

            for(i in 0..6){
                jsonArray.add("")
            }

            makeWeekJson(platform = platform, genre = genre, jsonArray = jsonArray)
        }
}

fun makeWeekJson(platform : String, genre: String, jsonArray : JsonArray)  {
    val storage = Firebase.storage
    val storageRef = storage.reference

    val jsonArrayRef = storageRef.child("${platform}/${genre}/WEEK/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json")

    val jsonFileRef = storageRef.child("${platform}/${genre}/DAY/${DBDate.dateMMDD()}.json")

    val file = jsonFileRef.getBytes(1024 * 1024)

    file.addOnSuccessListener { bytes ->
        val jsonString = String(bytes, Charset.forName("UTF-8"))
        val json = Json { ignoreUnknownKeys = true }
        val itemList = json.decodeFromString<List<BestItemData>>(jsonString)
        val itemJsonArray = JsonArray()

        // JSON 배열 사용
        for (item in itemList) {
            itemJsonArray.add(convertBestItemData(item))
        }

        jsonArray.set(DBDate.getDayOfWeekAsNumber(), itemJsonArray)

        val jsonBytes = jsonArray.toString().toByteArray(Charsets.UTF_8)

        // Firebase Storage에 JSON 배열 업로드
        jsonArrayRef.putBytes(jsonBytes)
            .addOnSuccessListener {
                // 업로드 성공 시 처리

            }
    }
}

fun uploadJsonArrayToStorageDay(platform : String, genre: String) {

    val route = BestRef.setBestRef(platform, genre).child("DAY")
    val storage = Firebase.storage
    val storageRef = storage.reference
    val jsonArrayRef = storageRef.child("${platform}/${genre}/DAY/${DBDate.dateMMDD()}.json")
//    val jsonArrayRef = storageRef.child("${platform}/${genre}/DAY/${DBDate.dateYesterday()}.json")

    route.addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if(dataSnapshot.exists()){

                val jsonArray = JsonArray()

                for (postSnapshot in dataSnapshot.children) {
                    val group: BestItemData? = postSnapshot.getValue(BestItemData::class.java)
                    jsonArray.add(convertBestItemData(group ?: BestItemData()))
                }

                val jsonArrayByteArray = jsonArray.toString().toByteArray(Charsets.UTF_8)


                jsonArrayRef.putBytes(jsonArrayByteArray)
                    .addOnSuccessListener {
                        // 업로드 성공 시 처리
                    }
            } else {
                Log.d("HIHI", "FALSE")
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    })
}

fun calculateTrophy(platform : String, genre: String) {
    val storage = Firebase.storage
    val storageRef = storage.reference
    val todayFileRef = storageRef.child("${platform}/${genre}/DAY/${DBDate.dateMMDD()}.json")
    val yesterdayFileRef = storageRef.child("${platform}/${genre}/DAY/${DBDate.dateYesterday()}.json")

    val todayFile = todayFileRef.getBytes(1024 * 1024)
    val yesterdayFile = yesterdayFileRef.getBytes(1024 * 1024)

    yesterdayFile.addOnSuccessListener { yesterdayBytes ->
        val yesterdayJson = Json { ignoreUnknownKeys = true }
        val yesterdayItemList = yesterdayJson.decodeFromString<List<BestItemData>>(String(yesterdayBytes, Charset.forName("UTF-8")))

        val yesterDatItemMap = mutableMapOf<String, BestItemData>()

        for (item in yesterdayItemList) {
            yesterDatItemMap[item.bookCode] = item
        }

        todayFile.addOnSuccessListener { bytes ->
            val jsonString = String(bytes, Charset.forName("UTF-8"))
            val json = Json { ignoreUnknownKeys = true }
            val itemList = json.decodeFromString<List<BestItemData>>(jsonString)

            // JSON 배열 사용
            for (item in itemList) {
                if(yesterDatItemMap.containsKey(item.bookCode)){

                    val total = yesterDatItemMap[item.bookCode]?.current ?: 0
                    val totalCount = (yesterDatItemMap[item.bookCode]?.totalCount ?: 0)

                    val totalWeek = if (getYesterdayDayOfWeek() == 7) {
                        1
                    } else {
                        yesterDatItemMap[item.bookCode]?.totalWeek ?: 0
                    }

                    val totalWeekCount = if (getYesterdayDayOfWeek() == 7) {
                        1
                    } else {
                        (yesterDatItemMap[item.bookCode]?.totalWeekCount ?: 0)
                    }

                    val totalMonth = if (datedd() == "01") {
                        1
                    } else {
                        yesterDatItemMap[item.bookCode]?.totalMonth ?: 0
                    }

                    val totalMonthCount = if (getYesterdayDayOfWeek() == 7) {
                        1
                    } else {
                        (yesterDatItemMap[item.bookCode]?.totalMonthCount ?: 0)
                    }

                    val bestListAnalyze = BestListAnalyze(
                        number = item.current,
                        info1 = item.info1,
                        total = total + item.current,
                        totalCount = totalCount + 1
                    )

                    val bestItemData = item.copy(
                        total = total + item.current,
                        totalCount = totalCount + 1,
                        totalWeek = totalWeek + item.current,
                        totalWeekCount = totalWeekCount + 1,
                        totalMonth = totalMonth + item.current,
                        totalMonthCount = totalMonthCount + 1
                    )

                    BestRef.setBookCode(platform, genre, item.bookCode).setValue(bestItemData)

                    BestRef.setBestTrophy(platform, genre, item.bookCode).setValue(bestListAnalyze)
                    BestRef.setBookWeeklyBest(platform, genre, item.bookCode).setValue(bestListAnalyze)
                    BestRef.setBookMonthlyBest(platform, genre, item.bookCode).setValue(bestListAnalyze)
                    BestRef.setBookWeeklyBestTotal(platform, genre, item.bookCode).setValue(bestListAnalyze)
                    BestRef.setBookMonthlyBestTotal(platform, genre, item.bookCode).setValue(bestListAnalyze)
                } else {
                    Log.d("HIHIHI", "NOT HAS")
                }
            }
        }
    }

}

fun miningValue(ref: MutableMap<String?, Any>, num : Int, platform: String, genre: String) {

    BestRef.setBookCode(platform, genre, ref["bookCode"] as String).setValue(BestRef.setBookListDataBest(ref))
    BestRef.setBestTrophy(platform, genre, ref["bookCode"] as String).setValue(BestRef.setBookListDataBestAnalyze(ref))

    BestRef.setBookDailyBest(platform, num, genre).setValue(BestRef.setBookListDataBest(ref))

    if (getDayOfWeekAsNumber() == 0) {
        BestRef.setBestRef(platform = platform, genre = genre).child("TROPHY_WEEK").removeValue()
    }

    BestRef.setBookWeeklyBest(platform, genre, ref["bookCode"] as String).setValue(BestRef.setBookListDataBestAnalyze(ref))
//    BestRef.setBookWeeklyBestYesterday(platform, genre, ref["bookCode"] as String).setValue(BestRef.setBookListDataBestAnalyze(ref))

    if (datedd() == "01") {
        BestRef.setBestRef(platform = platform, genre = genre).child("TROPHY_MONTH").removeValue()
    }

    BestRef.setBookMonthlyBest(platform, genre, ref["bookCode"] as String).setValue(BestRef.setBookListDataBestAnalyze(ref))

}

fun setDataStore(message: String, context: Context){
    val dataStore = DataStoreManager(context)
    val mRootRef = FirebaseDatabase.getInstance().reference.child("WORKER")

    var currentUser :  FirebaseUser? = null
    currentUser = Firebase.auth.currentUser

    if(message.contains("테스트")){
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.setDataStoreString(key = DataStoreManager.TEST_TIME, str = "${FCM.year}.${FCM.month}.${FCM.day} ${FCM.hour}:${FCM.min}")
        }

        mRootRef.child("TEST_TIME").setValue("${FCM.year}.${FCM.month}.${FCM.day} ${FCM.hour}:${FCM.min}")
        mRootRef.child("TEST_UID").setValue(currentUser?.uid ?: "NONE")

    } else if(message.contains("트로피 정산이 완료되었습니다")){
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.setDataStoreString(key = DataStoreManager.TROPHYWORKER_TIME, str = "${FCM.year}.${FCM.month}.${FCM.day} ${FCM.hour}:${FCM.min}")
        }

        mRootRef.child("TROPHYWORKER_TIME").setValue("${FCM.year}.${FCM.month}.${FCM.day} ${FCM.hour}:${FCM.min}")
        mRootRef.child("TROPHYWORKER_UID").setValue(currentUser?.uid ?: "NONE")

    } else if(message.contains("DAY JSON 생성이 완료되었습니다")){
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.setDataStoreString(key = DataStoreManager.JSONWORKER_TIME, str = "${FCM.year}.${FCM.month}.${FCM.day} ${FCM.hour}:${FCM.min}")
        }

        mRootRef.child("JSONWORKER_TIME").setValue("${FCM.year}.${FCM.month}.${FCM.day} ${FCM.hour}:${FCM.min}")
        mRootRef.child("JSONWORKER_UID").setValue(currentUser?.uid ?: "NONE")

    } else if(message.contains("베스트 리스트가 갱신되었습니다")){
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.setDataStoreString(key = DataStoreManager.BESTWORKER_TIME, str = "${FCM.year}.${FCM.month}.${FCM.day} ${FCM.hour}:${FCM.min}")
        }

        mRootRef.child("BESTWORKER_TIME").setValue("${FCM.year}.${FCM.month}.${FCM.day} ${FCM.hour}:${FCM.min}")
        mRootRef.child("BESTWORKER_UID").setValue(currentUser?.uid ?: "NONE")
    }
}


