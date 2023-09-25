package com.bigbigdw.manavarasetting.util

import android.annotation.SuppressLint
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.util.Log
import com.bigbigdw.manavarasetting.firebase.FCMAlert
import com.bigbigdw.manavarasetting.main.model.BestItemData
import com.bigbigdw.manavarasetting.main.model.BestListAnalyze
import com.bigbigdw.manavarasetting.main.viewModels.DataStoreManager
import com.bigbigdw.manavarasetting.util.DBDate.datedd
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

            val jsonArray = JsonArray()

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

                    jsonArray.add(convertBestItemData(bestItemData))

                } else {
                    jsonArray.add(convertBestItemData(item))
                }
            }

            val jsonArrayByteArray = jsonArray.toString().toByteArray(Charsets.UTF_8)

            todayFileRef.putBytes(jsonArrayByteArray)
                .addOnSuccessListener {
                    Log.d("HIHI-TROPHY", "성공")
                }.addOnFailureListener {
                    Log.d("HIHI-TROPHY", "실패 $it")
                }

        }
    }

}

fun miningValue(ref: MutableMap<String?, Any>, num : Int, platform: String, genre: String) {

    BestRef.setBookCode(platform, genre, ref["bookCode"] as String).setValue(BestRef.setBookListDataBest(ref))
    BestRef.setBestTrophy(platform, genre, ref["bookCode"] as String).setValue(BestRef.setBookListDataBestAnalyze(ref))
    BestRef.setBookWeeklyBest(platform, genre, ref["bookCode"] as String).setValue(BestRef.setBookListDataBestAnalyze(ref))
    BestRef.setBookMonthlyBest(platform, genre, ref["bookCode"] as String).setValue(BestRef.setBookListDataBestAnalyze(ref))

    BestRef.setBookDailyBest(platform, num, genre).setValue(BestRef.setBookListDataBest(ref))
    BestRef.setBestData(platform, num, genre).setValue(BestRef.setBookListDataBest(ref))
}

fun setDataStore(message: String, context: Context){
    val mRootRef = FirebaseDatabase.getInstance().reference.child("WORKER")

    val year = DBDate.dateMMDDHHMM().substring(0,4)
    val month = DBDate.dateMMDDHHMM().substring(4,6)
    val day = DBDate.dateMMDDHHMM().substring(6,8)
    val hour = DBDate.dateMMDDHHMM().substring(8,10)
    val min = DBDate.dateMMDDHHMM().substring(10,12)

    var currentUser :  FirebaseUser? = null
    currentUser = Firebase.auth.currentUser

    if(message.contains("테스트")){

        mRootRef.child("WORKER_TEST").setValue("${year}.${month}.${day} ${hour}:${min}")
        mRootRef.child("UID_TEST").setValue(currentUser?.uid ?: "NONE")

    } else if(message.contains("트로피 정산이 완료되었습니다")){

        mRootRef.child("WORKER_TROPHY").setValue("${year}.${month}.${day} ${hour}:${min}")
        mRootRef.child("UID_TROPHY").setValue(currentUser?.uid ?: "NONE")

    } else if(message.contains("DAY JSON 생성이 완료되었습니다")){

        mRootRef.child("WORKER_JSON").setValue("${year}.${month}.${day} ${hour}:${min}")
        mRootRef.child("UID_JSON").setValue(currentUser?.uid ?: "NONE")

    } else if(message.contains("베스트 리스트가 갱신되었습니다")){

        mRootRef.child("WORKER_BEST").setValue("${year}.${month}.${day} ${hour}:${min}")
        mRootRef.child("UID_BEST").setValue(currentUser?.uid ?: "NONE")
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
                    update
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
                    } else if (fcm?.body?.contains("베스트 리스트가 갱신되었습니다") == true) {
                        numBest += 1

                        if (fcm.body.contains("${year}.${month}.${day}")) {
                            numBestToday += 1
                        }
                    } else if (fcm?.body?.contains("DAY JSON 생성이 완료되었습니다") == true) {
                        numJson += 1

                        if (fcm.body.contains("${year}.${month}.${day}")) {
                            numJsonToday += 1
                        }
                    } else if (fcm?.body?.contains("트로피 정산이 완료되었습니다") == true) {
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
                    update
                }

            } else {
                Log.d("HIHI", "FALSE")
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    })
}


