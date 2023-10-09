package com.bigbigdw.manavarasetting.util

import android.content.Context
import android.util.Log
import com.bigbigdw.manavarasetting.main.model.ItemBookInfo
import com.bigbigdw.manavarasetting.util.MiningSource.miningNaverSeriesComic
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import kotlinx.coroutines.runBlocking
import java.nio.charset.Charset

fun miningValue(
    ref: MutableMap<String?, Any>,
    num: Int,
    platform: String,
    type: String
) {

    BestRef.setBookCode(
        platform = platform,
        bookCode = ref["bookCode"] as String,
        type = type
    ).setValue(BestRef.setItemBookInfoRef(ref))

    BestRef.setBestTrophy(
        platform = platform,
        bookCode = ref["bookCode"] as String,
        type = type
    ).setValue(BestRef.setItemBestInfoRef(ref))

    BestRef.setBookWeeklyBest(
        platform = platform,
        bookCode = ref["bookCode"] as String,
        type = type
    ).setValue(BestRef.setItemBestInfoRef(ref))

    BestRef.setBookMonthlyBest(
        platform = platform,
        bookCode = ref["bookCode"] as String,
        type = type
    ).setValue(BestRef.setItemBestInfoRef(ref))

    BestRef.setBookDailyBest(platform = platform, num = num, type = type)
        .setValue(BestRef.setItemBookInfoRef(ref))

    BestRef.setBestData(
        platform = platform,
        bookCode = ref["bookCode"] as String,
        type = type
    ).setValue(BestRef.setItemBestInfoRef(ref))

    BestRef.setBookWeeklyBestTotal(
        platform = platform,
        bookCode = ref["bookCode"] as String,
        type = type
    ).setValue(BestRef.setItemBestInfoRef(ref))

    BestRef.setBookMonthlyBestTotal(
        platform = platform,
        bookCode = ref["bookCode"] as String,
        type = type
    ).setValue(BestRef.setItemBestInfoRef(ref))
}

fun doMining(
    context: Context,
    platform: String,
    type: String,
    yesterDayItemMap: MutableMap<String, ItemBookInfo>
) {

    if(platform == "NAVER_SERIES"){
        if(type == "COMIC"){
            miningNaverSeriesComic(
                platform = platform,
                type = type,
                yesterDayItemMap = yesterDayItemMap
            ) { itemBookInfoList, itemBestInfoList ->
                doResultMining(
                    platform = platform,
                    type = type,
                    itemBookInfoList = itemBookInfoList,
                    itemBestInfoList = itemBestInfoList
                )
            }
        } else {
            MiningSource.miningNaverSeriesNovel(
                platform = platform,
                type = type,
                yesterDayItemMap = yesterDayItemMap
            ) { itemBookInfoList, itemBestInfoList ->
                doResultMining(
                    platform = platform,
                    type = type,
                    itemBookInfoList = itemBookInfoList,
                    itemBestInfoList = itemBestInfoList
                )
            }
        }
    } else if(platform == "JOARA") {
        MiningSource.miningJoara(
            context = context,
            mining = "",
            platform = platform,
            type = type,
            yesterDayItemMap = yesterDayItemMap
        ) { itemBookInfoList, itemBestInfoList ->
            doResultMining(
                platform = platform,
                type = type,
                itemBookInfoList = itemBookInfoList,
                itemBestInfoList = itemBestInfoList
            )
        }
    } else if(platform == "JOARA_PREMIUM") {
        MiningSource.miningJoara(
            context = context,
            mining = "premium",
            platform = platform,
            type = type,
            yesterDayItemMap = yesterDayItemMap
        ) { itemBookInfoList, itemBestInfoList ->
            doResultMining(
                platform = platform,
                type = type,
                itemBookInfoList = itemBookInfoList,
                itemBestInfoList = itemBestInfoList
            )
        }
    } else if(platform == "JOARA_NOBLESS") {
        MiningSource.miningJoara(
            context = context,
            mining = "nobless",
            platform = platform,
            type = type,
            yesterDayItemMap = yesterDayItemMap
        ) { itemBookInfoList, itemBestInfoList ->
            doResultMining(
                platform = platform,
                type = type,
                itemBookInfoList = itemBookInfoList,
                itemBestInfoList = itemBestInfoList
            )
        }
    } else if(platform == "NAVER_CHALLENGE") {
        MiningSource.miningNaver(
            platform = platform,
            type = type,
            mining = "challenge",
            yesterDayItemMap = yesterDayItemMap
        ) { itemBookInfoList, itemBestInfoList ->
            doResultMining(
                platform = platform,
                type = type,
                itemBookInfoList = itemBookInfoList,
                itemBestInfoList = itemBestInfoList
            )
        }
    } else if(platform == "NAVER_BEST") {
        MiningSource.miningNaver(
            platform = platform,
            type = type,
            mining = "best",
            yesterDayItemMap = yesterDayItemMap
        ) { itemBookInfoList, itemBestInfoList ->
            doResultMining(
                platform = platform,
                type = type,
                itemBookInfoList = itemBookInfoList,
                itemBestInfoList = itemBestInfoList
            )
        }
    } else if(platform == "RIDI_FANTAGY") {
        MiningSource.miningRidi(
            mining = "1750",
            platform = platform,
            type = type,
            yesterDayItemMap = yesterDayItemMap
        ) { itemBookInfoList, itemBestInfoList ->
            doResultMining(
                platform = platform,
                type = type,
                itemBookInfoList = itemBookInfoList,
                itemBestInfoList = itemBestInfoList
            )
        }
    } else if(platform == "RIDI_ROMANCE") {
        MiningSource.miningRidi(
            mining = "1650",
            platform = platform,
            type = type,
            yesterDayItemMap = yesterDayItemMap
        ) { itemBookInfoList, itemBestInfoList ->
            doResultMining(
                platform = platform,
                type = type,
                itemBookInfoList = itemBookInfoList,
                itemBestInfoList = itemBestInfoList
            )
        }
    } else if(platform == "ONESTORY") {

        if(type == "COMIC"){

        } else{
            MiningSource.miningOnestory(
                platform = platform,
                type = type,
                yesterDayItemMap = yesterDayItemMap
            ) { itemBookInfoList, itemBestInfoList ->
                doResultMining(
                    platform = platform,
                    type = type,
                    itemBookInfoList = itemBookInfoList,
                    itemBestInfoList = itemBestInfoList
                )
            }
        }

    } else if(platform == "KAKAO_STAGE") {
        MiningSource.miningKakaoStage(
            platform = platform,
            type = type,
            yesterDayItemMap = yesterDayItemMap
        ) { itemBookInfoList, itemBestInfoList ->
            doResultMining(
                platform = platform,
                type = type,
                itemBookInfoList = itemBookInfoList,
                itemBestInfoList = itemBestInfoList
            )
        }
    } else if(platform == "MUNPIA") {
        MiningSource.miningMunpia(
            platform = platform,
            type = type,
            yesterDayItemMap = yesterDayItemMap
        ) { itemBookInfoList, itemBestInfoList ->
            doResultMining(
                platform = platform,
                type = type,
                itemBookInfoList = itemBookInfoList,
                itemBestInfoList = itemBestInfoList
            )
        }
    } else if(platform == "TOKSODA") {
        MiningSource.miningToksoda(
            platform = platform,
            type = type,
            yesterDayItemMap = yesterDayItemMap
        ) { itemBookInfoList, itemBestInfoList ->
            doResultMining(
                platform = platform,
                type = type,
                itemBookInfoList = itemBookInfoList,
                itemBestInfoList = itemBestInfoList
            )
        }
    }
}

private fun doResultMining(
    platform: String,
    type: String,
    itemBookInfoList: JsonArray,
    itemBestInfoList: JsonArray
) {

    runBlocking {
        makeTodayJson(
            platform = platform,
            type = type,
            todayArray = itemBookInfoList
        )
    }
    runBlocking {
        uploadJsonTrophyWeek(
            platform = platform,
            type = type,
            itemBestInfoList = itemBestInfoList
        )
    }
    runBlocking {
        uploadJsonTrophyMonth(
            platform = platform,
            type = type,
            itemBestInfoList = itemBestInfoList
        )
    }
}

fun makeTodayJson(
    platform: String,
    type: String,
    todayArray: JsonArray
) {

    val storage = Firebase.storage
    val storageRef = storage.reference
    val jsonArrayRef =
        storageRef.child("${platform}/${type}/DAY/${DBDate.dateMMDD()}.json")

    val jsonArrayByteArray = todayArray.toString().toByteArray(Charsets.UTF_8)

    jsonArrayRef.putBytes(jsonArrayByteArray)
        .addOnSuccessListener {
            Log.d("MANANVARA_MINING", "makeTodayJson addOnSuccessListener == $it")

            uploadWeekJson(
                platform = platform,
                type = type,
                todayArray = todayArray
            )
        }.addOnFailureListener {
            Log.d("MANANVARA_MINING", "makeTodayJson addOnFailureListener == $it")
        }
}

private fun uploadWeekJson(
    platform: String,
    type: String,
    todayArray: JsonArray
) {
    val storage = Firebase.storage
    val storageRef = storage.reference

    val jsonFileRef =
        storageRef.child("${platform}/${type}/WEEK/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json") // 읽어올 JSON 파일의 경로

    val jsonMonthRef =
        storageRef.child("${platform}/${type}/MONTH/${DBDate.year()}_${DBDate.month()}.json")

    jsonFileRef.getBytes(1024 * 1024)
        .addOnSuccessListener { bytes ->
            val jsonString = String(bytes, Charset.forName("UTF-8"))
            val weekArray = JsonParser().parse(jsonString).asJsonArray

            makeWeekJson(
                platform = platform,
                weekArray = weekArray,
                type = type,
                todayArray = todayArray
            )

        }

        .addOnFailureListener {

            val jsonArray = JsonArray()

            for (i in 0..6) {
                jsonArray.add("")
            }

            makeWeekJson(
                platform = platform,
                weekArray = jsonArray,
                type = type,
                todayArray = todayArray
            )
        }

    jsonMonthRef.getBytes(1024 * 1024)
        .addOnSuccessListener { bytes ->
            val jsonString = String(bytes, Charset.forName("UTF-8"))
            val monthArray = JsonParser().parse(jsonString).asJsonArray

            makeMonthJson(
                platform = platform,
                jsonMonthArray = monthArray,
                type = type,
                todayArray = todayArray
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
                platform = platform,
                jsonMonthArray = jsonArray,
                type = type,
                todayArray = todayArray
            )
        }
}

private fun makeWeekJson(
    platform: String,
    weekArray: JsonArray,
    type: String,
    todayArray: JsonArray
) {
    val storage = Firebase.storage
    val storageRef = storage.reference

    val jsonWeekRef =
        storageRef.child("${platform}/${type}/WEEK/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json")

    val indexNum = DBDate.getDayOfWeekAsNumber()

    val clonedWeekArray = weekArray.deepCopy()

    clonedWeekArray.set(indexNum, todayArray)

    try{
        val jsonBytes = clonedWeekArray.toString().toByteArray(Charsets.UTF_8)

        jsonWeekRef.putBytes(jsonBytes)
            .addOnSuccessListener {
                Log.d("makeWeekJson", "jsonWeekRef 성공")
            }.addOnFailureListener {
                Log.d("makeWeekJson", "jsonWeekRef 실패")
            }


    } catch (e : Exception){

    }
}

private fun makeMonthJson(
    platform: String,
    jsonMonthArray: JsonArray,
    type: String,
    todayArray: JsonArray
) {
    val storage = Firebase.storage
    val storageRef = storage.reference

    val jsonMonthRef =
        storageRef.child("${platform}/${type}/MONTH/${DBDate.year()}_${DBDate.month()}.json")

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

    val jsonBytes = jsonMonthArray.toString().toByteArray(Charsets.UTF_8)

    jsonMonthRef.putBytes(jsonBytes)
        .addOnSuccessListener {
            Log.d("JSON_MINING", "uploadJsonArrayToStorageMonth makeMonthJson")
        }.addOnFailureListener {
            Log.d("JSON_MINING", "uploadJsonArrayToStorageMonth makeMonthJson")
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
}