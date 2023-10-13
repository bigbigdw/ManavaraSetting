package com.bigbigdw.manavarasetting.util

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.bigbigdw.manavarasetting.main.event.EventMain
import com.bigbigdw.manavarasetting.main.model.ItemBestKeyword
import com.bigbigdw.manavarasetting.main.model.ItemBookInfo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONArray
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

            makeMonthJson(
                jsonMonthArray = monthArray,
                todayArray = todayArray,
                month = month,
                type = type
            )

        }.addOnFailureListener {

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
                month = month,
                type = type
            )
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
    type : String,
) {

    val indexNum = DBDate.getDayOfWeekAsNumber()
    val indexWeekNum = DBDate.getCurrentWeekNumber() - 1

    val weekJson = try {
        jsonMonthArray.get(indexWeekNum).asJsonArray
    } catch (e: Exception) {
        JsonArray()
    }

    if (weekJson.size() > 0) {

        if(type == "GENRE"){
            weekJson.set(indexNum, todayArray)
            jsonMonthArray.set(indexWeekNum, weekJson)
        } else {
            weekJson.set(indexNum, todayArray[0])
            jsonMonthArray.set(indexWeekNum, weekJson)
        }

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
    type: String,
    itemBestInfoList: JsonArray
) {

    val storage = Firebase.storage
    val storageRef = storage.reference
    val jsonArrayRef =
        storageRef.child("${platform}/${type}/WEEK_TROPHY/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json")

    val jsonArrayByteArray = itemBestInfoList.toString().toByteArray(Charsets.UTF_8)

    jsonArrayRef.putBytes(jsonArrayByteArray)
        .addOnSuccessListener {
            Log.d(
                "TROPHY_MINING",
                "uploadJsonArrayToStorageTrophyWeek addOnSuccessListener"
            )

        }.addOnFailureListener {
            Log.d(
                "TROPHY_MINING",
                "uploadJsonArrayToStorageTrophyWeek addOnFailureListener == $it"
            )
        }
}

fun uploadJsonTrophyMonth(
    platform: String,
    type: String,
    itemBestInfoList: JsonArray
) {

    val storage = Firebase.storage
    val storageRef = storage.reference
    val jsonArrayRef =
        storageRef.child("${platform}/${type}/MONTH_TROPHY/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json")


    try{
        val jsonArrayByteArray = itemBestInfoList.toString().toByteArray(Charsets.UTF_8)

        jsonArrayRef.putBytes(jsonArrayByteArray)
            .addOnSuccessListener {
                Log.d(
                    "TROPHY_MINING",
                    "uploadJsonArrayToStorageTrophyMonth addOnSuccessListener"
                )
            }.addOnFailureListener {
                Log.d(
                    "TROPHY_MINING",
                    "uploadJsonArrayToStorageTrophyMonth addOnFailureListener == $it"
                )
            }

    } catch (e : Exception){
        Log.d("uploadJsonTrophyMonth", "jsonArrayByteArray $e")
    }
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
            genreValuesMap[value] = genreValuesMap.getOrDefault(value, 0) + 1
        }
    }

    for ((key, value) in genreValuesMap) {
        jsonArray.add(
            convertItemKeywordJson(
                itemBestKeyword = ItemBestKeyword(
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

    val today = storageRef.child("${platform}/${type}/GENRE_DAY/${DBDate.dateMMDD()}.json")
    val week =  storageRef.child("${platform}/${type}/GENRE_WEEK/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json")
    val month = storageRef.child("${platform}/${type}/GENRE_MONTH/${DBDate.year()}_${DBDate.month()}.json")

    runBlocking {
        makeTodayJson(
            today = today,
            todayArray = jsonArray,
            week = week,
            month = month,
            type = "GENRE"
        )
    }
}

fun getGenreDay(
    platform: String,
    type: String
) {

    val mRootRef =  FirebaseDatabase.getInstance().reference.child("BEST").child(type).child(platform).child("GENRE_DAY")
    val dataMap = HashMap<String, Any>()

    mRootRef.addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if(dataSnapshot.exists()){

                for (snapshot in dataSnapshot.children) {
                    val key = snapshot.key
                    val value = snapshot.value
                    if (key != null && value != null) {
                        dataMap[key] = value
                    }
                }

                Log.d("HIHIHI", "dataMap == ${dataMap}")

            } else {
                Log.d("HIHIHI", "FAIL == NOT EXIST")
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    })
}

fun getGenreDayWeek(
    platform: String,
    type: String
) {

    val mRootRef =  FirebaseDatabase.getInstance().reference.child("BEST").child(type).child(platform).child("GENRE_WEEK")
    val dataMap = HashMap<String, Any>()

    mRootRef.addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if(dataSnapshot.exists()){

                for (i in 0..7) {

                    val item = dataSnapshot.child(i.toString())

                    if (item.exists()) {

                        for (snapshot in item.children) {
                            val key = snapshot.key
                            val value = snapshot.value

                            if (key != null && value != null) {

                                if(dataMap[key] != null){

                                    val preValue = dataMap[key] as Long
                                    val currentValue = value as Long

                                    Log.d("HIHIHI", "preValue == ${preValue} currentValue == $currentValue")

                                    dataMap[key] = preValue + currentValue
                                } else {
                                    dataMap[key] = value
                                }
                            }

                            Log.d("HIHIHI", "snapshot == ${item} dataMap == $dataMap")
                        }
                    }
                }

            } else {
                Log.d("HIHIHI", "FAIL == NOT EXIST")
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    })
}

fun getGenreDayMonth(
    platform: String,
    type: String
) {

    val mRootRef =  FirebaseDatabase.getInstance().reference.child("BEST").child(type).child(platform).child("GENRE_MONTH")
    val dataMap = HashMap<String, Any>()

    mRootRef.addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if(dataSnapshot.exists()){

                for (i in 0..31) {

                    val item = dataSnapshot.child(i.toString())

                    if (item.exists()) {

                        for (snapshot in item.children) {
                            val key = snapshot.key
                            val value = snapshot.value

                            if (key != null && value != null) {

                                if(dataMap[key] != null){

                                    val preValue = dataMap[key] as Long
                                    val currentValue = value as Long

                                    Log.d("HIHIHI", "preValue == ${preValue} currentValue == $currentValue")

                                    dataMap[key] = preValue + currentValue
                                } else {
                                    dataMap[key] = value
                                }
                            }

                            Log.d("HIHIHI", "snapshot == ${item} dataMap == $dataMap")
                        }
                    }
                }

            } else {
                Log.d("HIHIHI", "FAIL == NOT EXIST")
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    })
}

fun getJsonGenreList(platform: String, type: String){
    val storage = Firebase.storage
    val storageRef = storage.reference
    val todayFileRef = storageRef.child("${platform}/${type}/GENRE_DAY/${DBDate.dateMMDD()}.json")

    val todayFile = todayFileRef.getBytes(1024 * 1024)

    todayFile.addOnSuccessListener { bytes ->
        val jsonString = String(bytes, Charset.forName("UTF-8"))
        val json = Json { ignoreUnknownKeys = true }
        val itemList = json.decodeFromString<List<ItemBestKeyword>>(jsonString)

        val jsonList = ArrayList<ItemBestKeyword>()

        for (item in itemList) {
            jsonList.add(item)
        }

        Log.d("HIHIHI", "getBestJsonList == $jsonList")
    }
}
fun getJsonGenreWeekList(platform: String, menu : String, type : String){
    val storage = Firebase.storage
    val storageRef = storage.reference

    val fileRef: StorageReference = if(menu == "주간"){
        storageRef.child("${platform}/${type}/GENRE_WEEK/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json")
    } else {
        storageRef.child("${platform}/${type}/GENRE_MONTH/${DBDate.year()}_${DBDate.month()}.json")
    }

    val file = fileRef.getBytes(1024 * 1024)

    file.addOnSuccessListener { bytes ->
        val jsonString = String(bytes, Charset.forName("UTF-8"))

        val jsonArray = JSONArray(jsonString)

        val weekJsonList = ArrayList<ArrayList<ItemBestKeyword>>()

        for (i in 0 until jsonArray.length()) {

            try{
                val jsonArrayItem = jsonArray.getJSONArray(i)
                val itemList = ArrayList<ItemBestKeyword>()

                for (j in 0 until jsonArrayItem.length()) {

                    try{
                        val jsonObject = jsonArrayItem.getJSONObject(j)
                        itemList.add(convertItemKeyword(jsonObject))
                    }catch (e : Exception){
                        itemList.add(ItemBestKeyword())
                    }
                }

                weekJsonList.add(itemList)
            } catch (e : Exception){
                weekJsonList.add(ArrayList())
            }
        }

        Log.d("HIHIHI", "weekJsonList == $weekJsonList")
    }
}