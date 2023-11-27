package com.bigbigdw.manavarasetting.util

import android.content.Context
import android.util.Log
import com.bigbigdw.manavarasetting.main.model.ItemBestInfo
import com.bigbigdw.manavarasetting.main.model.ItemGenre
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.nio.charset.Charset

fun makeTodayJson(
    today: StorageReference,
    todayArray: JsonArray,
    week: StorageReference,
    month: StorageReference,
    type : String,
) {

    val jsonArrayByteArray = todayArray.toString().toByteArray(Charsets.UTF_8)

    today.putBytes(jsonArrayByteArray)
        .addOnSuccessListener {
            Log.d("MANANVARA_MINING", "makeTodayJson addOnSuccessListener == $it")

            uploadWeekJson(
                todayArray = todayArray,
                week = week,
                month = month,
                type = type
            )
        }.addOnFailureListener {
            Log.d("MANANVARA_MINING", "makeTodayJson addOnFailureListener == $it")
        }
}

private fun uploadWeekJson(
    todayArray: JsonArray,
    week: StorageReference,
    month: StorageReference,
    type: String
) {

    week.getBytes(1024 * 1024)
        .addOnSuccessListener { bytes ->
            val jsonString = String(bytes, Charset.forName("UTF-8"))
            val weekArray = JsonParser().parse(jsonString).asJsonArray

            makeWeekJson(
                weekArray = weekArray,
                todayArray = todayArray,
                week = week
            )

        }

        .addOnFailureListener {

            val jsonArray = JsonArray()

            for (i in 0..6) {
                jsonArray.add("")
            }

            makeWeekJson(
                weekArray = jsonArray,
                todayArray = todayArray,
                week = week
            )
        }

    month.getBytes(1024 * 1024)
        .addOnSuccessListener { bytes ->
            val jsonString = String(bytes, Charset.forName("UTF-8"))
            val monthArray = JsonParser().parse(jsonString).asJsonArray

            if(type == "GENRE" || type == "KEYWORD"){
                makeMonthGenreJson(
                    jsonMonthArray = monthArray,
                    todayArray = todayArray,
                    month = month
                )
            } else {
                makeMonthJson(
                    jsonMonthArray = monthArray,
                    todayArray = todayArray,
                    month = month
                )
            }


        }.addOnFailureListener {

            if(type == "GENRE"  || type == "KEYWORD"){
                val jsonArray = JsonArray()

                val totalWeekCount = DBDate.getDaysInMonth()

                for (i in 0 until totalWeekCount) {
                    jsonArray.add("")
                }

                makeMonthGenreJson(
                    jsonMonthArray = jsonArray,
                    todayArray = todayArray,
                    month = month
                )
            } else {
                val jsonArray = JsonArray()

                val totalWeekCount = DBDate.getNumberOfWeeksInMonth(
                    year = DBDate.year().toInt(), month = DBDate.month().toInt()
                )

                for (i in 0 until totalWeekCount) {
                    jsonArray.add("")
                }

                makeMonthJson(
                    jsonMonthArray = jsonArray,
                    todayArray = todayArray,
                    month = month
                )
            }
        }
}

private fun makeWeekJson(
    weekArray: JsonArray,
    todayArray: JsonArray,
    week: StorageReference
) {

    val indexNum = DBDate.getDayOfWeekAsNumber()

    val clonedWeekArray = weekArray.deepCopy()

    clonedWeekArray.set(indexNum, todayArray)

    try{
        val jsonBytes = clonedWeekArray.toString().toByteArray(Charsets.UTF_8)

        week.putBytes(jsonBytes)
            .addOnSuccessListener {
                Log.d("makeWeekJson", "jsonWeekRef 성공")
            }.addOnFailureListener {
                Log.d("makeWeekJson", "jsonWeekRef 실패")
            }


    } catch (e : Exception){
        Log.d("makeWeekJson", "jsonWeekRef $e")
    }
}

private fun makeMonthJson(
    jsonMonthArray: JsonArray,
    todayArray: JsonArray,
    month: StorageReference,
) {

    val indexNum = DBDate.getDayOfWeekAsNumber()
    val indexWeekNum = DBDate.getCurrentWeekNumber() - 1

    val weekJson = try {
        jsonMonthArray.get(indexWeekNum).asJsonArray
    } catch (e: Exception) {
        JsonArray()
    }

    if (weekJson.size() > 0) {
        weekJson.set(indexNum, todayArray[0])
        jsonMonthArray.set(indexWeekNum, weekJson)
    } else {

        val itemWeekJsonArray = JsonArray()

        for (i in 0..6) {
            itemWeekJsonArray.add("")
        }

        itemWeekJsonArray.set(indexNum,todayArray[0])
        jsonMonthArray.set(indexWeekNum, itemWeekJsonArray)
    }

    try{
        val jsonBytes = jsonMonthArray.toString().toByteArray(Charsets.UTF_8)

        month.putBytes(jsonBytes)
            .addOnSuccessListener {
                Log.d("JSON_MINING", "uploadJsonArrayToStorageMonth makeMonthJson")
            }.addOnFailureListener {
                Log.d("JSON_MINING", "uploadJsonArrayToStorageMonth makeMonthJson")
            }

    } catch (e : Exception){
        Log.d("makeMonthJson", "jsonBytes $e")
    }
}

fun uploadJsonTrophyWeek(
    platform: String,
    type: String
) {

    val storage = Firebase.storage
    val storageRef = storage.reference
    val jsonArrayRef = storageRef.child("${platform}/${type}/WEEK_TROPHY/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json")

    val mRootRef = FirebaseDatabase.getInstance().reference.child("BEST").child(type).child(platform).child("TROPHY_WEEK_TOTAL")

    mRootRef.addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {

                val itemList = JsonArray()

                for (item in dataSnapshot.children) {

                    val book = item.getValue(ItemBestInfo::class.java)

                    if (book != null) {
                        itemList.add(convertItemBest(book))
                    }
                }

                val jsonArrayByteArray = itemList.toString().toByteArray(Charsets.UTF_8)

                jsonArrayRef.putBytes(jsonArrayByteArray)
                    .addOnSuccessListener {}
                    .addOnFailureListener {}
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    })
}

fun uploadJsonTrophyMonth(
    platform: String,
    type: String
) {

    val storage = Firebase.storage
    val storageRef = storage.reference
    val jsonArrayRef = storageRef.child("${platform}/${type}/MONTH_TROPHY/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json")

    val mRootRef = FirebaseDatabase.getInstance().reference.child("BEST").child(type).child(platform).child("TROPHY_MONTH_TOTAL")

    mRootRef.addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {

                val itemList = JsonArray()

                for (item in dataSnapshot.children) {

                    val book = item.getValue(ItemBestInfo::class.java)

                    if (book != null) {
                        itemList.add(convertItemBest(book))
                    }
                }

                val jsonArrayByteArray = itemList.toString().toByteArray(Charsets.UTF_8)

                jsonArrayRef.putBytes(jsonArrayByteArray)
                    .addOnSuccessListener {}
                    .addOnFailureListener {}
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    })
}

fun doResultMiningGenre(
    platform: String,
    type: String,
    itemBookInfoList: JsonArray
) {
    val genreValuesMap = HashMap<String, Int>()
    val jsonArray = JsonArray()

    for (i in 0 until itemBookInfoList.size()) {
        val item = JSONObject(itemBookInfoList[i].asJsonObject.toString())
        if (item.optString("genre").isNotEmpty()) {

            val value = item.optString("genre")

            genreValuesMap[value.replace("/", ",")
                .replace(".", " ")
                .replace("#", " ")
                .replace("$", " ")
                .replace("[", " ")
                .replace("]", " ")] = genreValuesMap.getOrDefault(value, 0) + 1
        }
    }

    for ((key, value) in genreValuesMap) {
        jsonArray.add(
            convertItemKeywordJson(
                itemBestKeyword = ItemGenre(
                    title = key,
                    value = value.toString()
                )
            )
        )
    }

    miningGenre(
        ref = genreValuesMap,
        platform = platform,
        type = type,
    )

    val storage = Firebase.storage
    val storageRef = storage.reference

    val genreToday = storageRef.child("${platform}/${type}/GENRE_DAY/${DBDate.dateMMDD()}.json")
    val genreWeek =  storageRef.child("${platform}/${type}/GENRE_WEEK/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json")
    val genreMonth = storageRef.child("${platform}/${type}/GENRE_MONTH/${DBDate.year()}_${DBDate.month()}.json")

    runBlocking {
        makeTodayJson(
            today = genreToday,
            todayArray = jsonArray,
            week = genreWeek,
            month = genreMonth,
            type = "GENRE"
        )
    }
}

private fun makeMonthGenreJson(
    jsonMonthArray: JsonArray,
    todayArray: JsonArray,
    month: StorageReference,
) {

    val indexDayNum = DBDate.getCurrentDayOfMonth() - 1

    if(jsonMonthArray.size() > 0){
        jsonMonthArray.set(indexDayNum, todayArray)
    } else {
        val totalWeekCount = DBDate.getNumberOfWeeksInMonth(
            year = DBDate.year().toInt(), month = DBDate.month().toInt()
        )

        for (i in 0 until totalWeekCount) {
            jsonMonthArray.add("")
        }

        jsonMonthArray.set(indexDayNum, todayArray)
    }

    try{
        val jsonBytes = jsonMonthArray.toString().toByteArray(Charsets.UTF_8)

        month.putBytes(jsonBytes)
            .addOnSuccessListener {
                Log.d("JSON_MINING", "uploadJsonArrayToStorageMonth makeMonthJson")
            }.addOnFailureListener {
                Log.d("JSON_MINING", "uploadJsonArrayToStorageMonth makeMonthJson")
            }

    } catch (e : Exception){
        Log.d("makeMonthJson", "jsonBytes $e")
    }
}





