package com.bigbigdw.manavarasetting.util

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import com.bigbigdw.manavarasetting.main.model.BestItemData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
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
    val storage = Firebase.storage
    val storageRef = storage.reference
    val jsonArrayRef = storageRef.child("${platform}/${genre}/DAY/${DBDate.dateMMDD()}.json")

    BestRef.setBestRef(platform, genre).child("DAY").addListenerForSingleValueEvent(object :
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
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    })
}

fun miningValue(ref: MutableMap<String?, Any>, num: Int, platform: String, genre: String) {

    BestRef.setBookCode(platform, genre, ref["bookCode"] as String).setValue(BestRef.setBookListDataBest(ref))
    BestRef.setBookCodeData(platform, genre, ref["bookCode"] as String).setValue(BestRef.setBookListDataBestAnalyze(ref))

    BestRef.setBestData(platform, num, genre).setValue(BestRef.setBookListDataBest(ref))
    BestRef.setBookWeeklyBest(platform, genre, ref["bookCode"] as String).setValue(BestRef.setBookListDataBestAnalyze(ref))

    BestRef.setBookMonthlyBest(platform, genre, ref["bookCode"] as String).setValue(BestRef.setBookListDataBestAnalyze(ref))

}


