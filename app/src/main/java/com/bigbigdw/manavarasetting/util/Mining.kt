package com.bigbigdw.manavarasetting.util

import android.content.Context
import android.util.Log
import com.bigbigdw.manavarasetting.main.model.ItemBestInfo
import com.bigbigdw.manavarasetting.main.model.ItemBookInfo
import com.bigbigdw.manavarasetting.util.MiningSource.miningNaverSeriesComic
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.nio.charset.Charset

fun miningValue(
    ref: MutableMap<String?, Any>,
    num: Int,
    platform: String,
    genre: String,
    type: String
) {

    BestRef.setBookCode(
        platform = platform,
        genre = genre,
        bookCode = ref["bookCode"] as String,
        type = type
    ).setValue(BestRef.setItemBookInfoRef(ref))

    BestRef.setBestTrophy(
        platform = platform,
        genre = genre,
        bookCode = ref["bookCode"] as String,
        type = type
    ).setValue(BestRef.setItemBestInfoRef(ref))

    BestRef.setBookWeeklyBest(
        platform = platform,
        genre = genre,
        bookCode = ref["bookCode"] as String,
        type = type
    ).setValue(BestRef.setItemBestInfoRef(ref))

    BestRef.setBookMonthlyBest(
        platform = platform,
        genre = genre,
        bookCode = ref["bookCode"] as String,
        type = type
    ).setValue(BestRef.setItemBestInfoRef(ref))

    BestRef.setBookDailyBest(platform = platform, num = num, genre = genre, type = type)
        .setValue(BestRef.setItemBookInfoRef(ref))

    BestRef.setBestData(
        platform = platform,
        genre = genre,
        bookCode = ref["bookCode"] as String,
        type = type
    ).setValue(BestRef.setItemBestInfoRef(ref))

    BestRef.setBookWeeklyBestTotal(
        platform = platform,
        genre = genre,
        bookCode = ref["bookCode"] as String,
        type = type
    ).setValue(BestRef.setItemBestInfoRef(ref))

    BestRef.setBookMonthlyBestTotal(
        platform = platform,
        genre = genre,
        bookCode = ref["bookCode"] as String,
        type = type
    ).setValue(BestRef.setItemBestInfoRef(ref))
}

private fun uploadJsonArrayToStorageWeek(platform: String, genre: String, type: String) {
    val storage = Firebase.storage
    val storageRef = storage.reference

    val jsonFileRef =
        storageRef.child("${platform}/${type}/${genre}/WEEK/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json") // 읽어올 JSON 파일의 경로

    // JSON 파일을 다운로드
    jsonFileRef.getBytes(1024 * 1024)
        .addOnSuccessListener { bytes ->
            val jsonString = String(bytes, Charset.forName("UTF-8"))
            val weekArray = JsonParser().parse(jsonString).asJsonArray

            makeWeekJson(platform = platform, genre = genre, jsonArray = weekArray, type = type)
        }

        .addOnFailureListener {

            val jsonArray = JsonArray()

            for (i in 0..6) {
                jsonArray.add("")
            }

            makeWeekJson(platform = platform, genre = genre, jsonArray = jsonArray, type = type)
        }
}

private fun makeWeekJson(platform: String, genre: String, jsonArray: JsonArray, type: String) {
    val storage = Firebase.storage
    val storageRef = storage.reference

    val jsonWeekRef =
        storageRef.child("${platform}/${type}/${genre}/WEEK/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json")

    val jsonFileRef = storageRef.child("${platform}/${type}/${genre}/DAY/${DBDate.dateMMDD()}.json")

    val file = jsonFileRef.getBytes(1024 * 1024)

    file.addOnSuccessListener { bytes ->
        val jsonString = String(bytes, Charset.forName("UTF-8"))
        val json = Json { ignoreUnknownKeys = true }
        val itemList = json.decodeFromString<List<ItemBookInfo>>(jsonString)
        val itemJsonArray = JsonArray()

        // JSON 배열 사용
        for (item in itemList) {
            itemJsonArray.add(convertItemBook(item))
        }

        val indexNum = DBDate.getDayOfWeekAsNumber()

        jsonArray.set(indexNum, itemJsonArray)

        val jsonBytes = jsonArray.toString().toByteArray(Charsets.UTF_8)

        jsonWeekRef.putBytes(jsonBytes)
            .addOnSuccessListener {
                Log.d("JSON_MINING $genre", "uploadJsonArrayToStorageWeek makeWeekJson")

                uploadJsonArrayToStorageMonth(
                    platform = platform,
                    genre = genre,
                    type = type
                )
            }.addOnFailureListener {
                Log.d("makeWeekJson", "jsonWeekRef 실패")
            }
    }.addOnFailureListener {
        Log.d("makeWeekJson", "file 실패 == ${it}")
    }
}

private fun uploadJsonArrayToStorageMonth(platform: String, genre: String, type: String) {
    val storage = Firebase.storage
    val storageRef = storage.reference
    val jsonMonthRef =
        storageRef.child("${platform}/${type}/${genre}/MONTH/${DBDate.year()}_${DBDate.month()}.json")

    // JSON 파일을 다운로드
    jsonMonthRef.getBytes(1024 * 1024)
        .addOnSuccessListener { bytes ->
            val jsonString = String(bytes, Charset.forName("UTF-8"))
            val monthArray = JsonParser().parse(jsonString).asJsonArray

            makeMonthJson(
                platform = platform,
                genre = genre,
                jsonMonthArray = monthArray,
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
                platform = platform,
                genre = genre,
                jsonMonthArray = jsonArray,
                type = type
            )
        }
}

private fun makeMonthJson(platform: String, genre: String, jsonMonthArray: JsonArray, type: String) {
    val storage = Firebase.storage
    val storageRef = storage.reference

    val jsonMonthRef =
        storageRef.child("${platform}/${type}/${genre}/MONTH/${DBDate.year()}_${DBDate.month()}.json")

    val jsonTodayRef =
        storageRef.child("${platform}/${type}/${genre}/DAY/${DBDate.dateMMDD()}.json")

    val file = jsonTodayRef.getBytes(1024 * 1024)

    file.addOnSuccessListener { bytes ->
        val jsonString = String(bytes, Charset.forName("UTF-8"))
        val json = Json { ignoreUnknownKeys = true }
        val itemList = json.decodeFromString<List<ItemBookInfo>>(jsonString)
        val indexNum = DBDate.getDayOfWeekAsNumber()

        val indexWeekNum = DBDate.getCurrentWeekNumber() - 1
        val weekJson = try {
            jsonMonthArray.get(indexWeekNum).asJsonArray
        } catch (e: Exception) {
            JsonArray()
        }

        if (weekJson.size() > 0) {

            weekJson.set(indexNum, convertItemBook(itemList[0]))
            jsonMonthArray.set(indexWeekNum, weekJson)

        } else {

            val itemWeekJsonArray = JsonArray()

            for (i in 0..6) {
                itemWeekJsonArray.add("")
            }

            itemWeekJsonArray.set(indexNum, convertItemBook(itemList[0]))
            jsonMonthArray.set(indexWeekNum, itemWeekJsonArray)
        }

        val jsonBytes = jsonMonthArray.toString().toByteArray(Charsets.UTF_8)

        jsonMonthRef.putBytes(jsonBytes)
            .addOnSuccessListener {
                Log.d("JSON_MINING $genre", "uploadJsonArrayToStorageMonth makeMonthJson")
            }.addOnFailureListener {
                Log.d("JSON_MINING $genre", "uploadJsonArrayToStorageMonth makeMonthJson")
            }
    }.addOnFailureListener {
        Log.d("makeMonthJson", "file 실패")
    }
}

fun uploadJsonArrayToStorageDay(platform: String, genre: String, type: String) {

    val route = BestRef.setBestRef(platform = platform, genre = genre, type = type).child("DAY")
    val storage = Firebase.storage
    val storageRef = storage.reference
    val jsonArrayRef =
        storageRef.child("${platform}/${type}/${genre}/DAY/${DBDate.dateMMDD()}.json")

    Log.d("MINING_TEST", "uploadJsonArrayToStorageDay")

    route.addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {

                val jsonArray = JsonArray()

                for (postSnapshot in dataSnapshot.children) {
                    val group: ItemBookInfo? = postSnapshot.getValue(ItemBookInfo::class.java)
                    jsonArray.add(convertItemBook(group ?: ItemBookInfo()))
                }

                val jsonArrayByteArray = jsonArray.toString().toByteArray(Charsets.UTF_8)

                jsonArrayRef.putBytes(jsonArrayByteArray)
                    .addOnSuccessListener {

                        Log.d("JSON_MINING $genre", "uploadJsonArrayToStorageDay")

                        uploadJsonArrayToStorageWeek(
                            platform = platform,
                            genre = genre,
                            type = type
                        )
                    }
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    })
}

fun calculateTrophy(platform: String, genre: String, type: String) {
    val storage = Firebase.storage
    val storageRef = storage.reference
    val todayFileRef =
        storageRef.child("${platform}/${type}/${genre}/DAY/${DBDate.dateMMDD()}.json")

    val yesterdayFileRef =
        storageRef.child("${platform}/${type}/${genre}/DAY/${DBDate.dateYesterday()}.json")

    val todayFile = todayFileRef.getBytes(1024 * 1024)
    val yesterdayFile = yesterdayFileRef.getBytes(1024 * 1024)

    Log.d("MINING_TEST", "calculateTrophy")

    yesterdayFile.addOnSuccessListener { yesterdayBytes ->
        val yesterdayJson = Json { ignoreUnknownKeys = true }
        val yesterdayItemList = yesterdayJson.decodeFromString<List<ItemBookInfo>>(
            String(
                yesterdayBytes,
                Charset.forName("UTF-8")
            )
        )

        val yesterDatItemMap = mutableMapOf<String, ItemBookInfo>()

        for (item in yesterdayItemList) {
            yesterDatItemMap[item.bookCode] = item
        }

        todayFile.addOnSuccessListener { bytes ->
            val jsonString = String(bytes, Charset.forName("UTF-8"))
            val json = Json { ignoreUnknownKeys = true }
            val itemList = json.decodeFromString<List<ItemBookInfo>>(jsonString)

            val jsonArray = JsonArray()

            // JSON 배열 사용
            for (item in itemList) {
                if (yesterDatItemMap.containsKey(item.bookCode)) {

//                    val total = yesterDatItemMap[item.bookCode]?.current ?: 0
                    val totalCount = (yesterDatItemMap[item.bookCode]?.totalCount ?: 0)

                    val totalWeek = if (DBDate.getYesterdayDayOfWeek() == 7) {
                        1
                    } else {
                        yesterDatItemMap[item.bookCode]?.totalWeek ?: 0
                    }

                    val totalWeekCount = if (DBDate.getYesterdayDayOfWeek() == 7) {
                        1
                    } else {
                        (yesterDatItemMap[item.bookCode]?.totalWeekCount ?: 0)
                    }

                    val totalMonth = if (DBDate.datedd() == "01") {
                        1
                    } else {
                        yesterDatItemMap[item.bookCode]?.totalMonth ?: 0
                    }

                    val totalMonthCount = if (DBDate.getYesterdayDayOfWeek() == 7) {
                        1
                    } else {
                        (yesterDatItemMap[item.bookCode]?.totalMonthCount ?: 0)
                    }

                    val bestListAnalyze = ItemBestInfo(
//                        number = item.current,
//                        info1 = item.info1,
//                        total = total + item.current,
                        totalCount = totalCount + 1,
                        bookCode = item.bookCode
                    )

                    val bestItemData = item.copy(
//                        total = total + item.current,
                        totalCount = totalCount + 1,
//                        totalWeek = totalWeek + item.current,
                        totalWeekCount = totalWeekCount + 1,
//                        totalMonth = totalMonth + item.current,
                        totalMonthCount = totalMonthCount + 1
                    )

                    BestRef.setBookCode(
                        platform = platform,
                        genre = genre,
                        bookCode = item.bookCode,
                        type = type
                    ).setValue(bestItemData)

                    BestRef.setBestTrophy(
                        platform = platform,
                        genre = genre,
                        bookCode = item.bookCode,
                        type = type
                    ).setValue(bestListAnalyze)

                    BestRef.setBookWeeklyBest(
                        platform = platform,
                        genre = genre,
                        bookCode = item.bookCode,
                        type = type
                    ).setValue(bestListAnalyze)

                    BestRef.setBookMonthlyBest(
                        platform = platform,
                        genre = genre,
                        bookCode = item.bookCode,
                        type = type
                    ).setValue(bestListAnalyze)

                    BestRef.setBookWeeklyBestTotal(
                        platform = platform,
                        genre = genre,
                        bookCode = item.bookCode,
                        type = type
                    ).setValue(bestListAnalyze)

                    BestRef.setBookMonthlyBestTotal(
                        platform = platform,
                        genre = genre,
                        bookCode = item.bookCode,
                        type = type
                    ).setValue(bestListAnalyze)

                    jsonArray.add(convertItemBook(bestItemData))

                } else {
                    jsonArray.add(convertItemBook(item))
                }
            }

            val jsonArrayByteArray = jsonArray.toString().toByteArray(Charsets.UTF_8)

            todayFileRef.putBytes(jsonArrayByteArray)
                .addOnSuccessListener {
                    Log.d("TROPHY_MINING $genre", "calculateTrophy addOnSuccessListener")

                    uploadJsonArrayToStorageTrophyWeek(
                        platform = platform,
                        genre = genre,
                        type = type
                    )

                }.addOnFailureListener {
                    Log.d(
                        "TROPHY_MINING $genre",
                        "calculateTrophy addOnSuccessListener addOnFailureListener == $it"
                    )
                }

        }
    }.addOnFailureListener {
        todayFile.addOnSuccessListener { bytes ->
            val jsonString = String(bytes, Charset.forName("UTF-8"))
            val json = Json { ignoreUnknownKeys = true }
            val itemList = json.decodeFromString<List<ItemBookInfo>>(jsonString)

            val jsonArray = JsonArray()

            // JSON 배열 사용
            for (item in itemList) {
                val bestListAnalyze = ItemBestInfo(
//                    number = item.current,
//                    info1 = item.info1,
//                    total = item.current,
                    totalCount = 1,
                    bookCode = item.bookCode
                )

                val bestItemData = item.copy(
//                    total = item.current,
                    totalCount = 1,
//                    totalWeek = item.current,
                    totalWeekCount = 1,
//                    totalMonth = item.current,
                    totalMonthCount = 1
                )

                BestRef.setBookCode(
                    platform = platform,
                    genre = genre,
                    bookCode = item.bookCode,
                    type = type
                ).setValue(bestItemData)

                BestRef.setBestTrophy(
                    platform = platform,
                    genre = genre,
                    bookCode = item.bookCode,
                    type = type
                ).setValue(bestListAnalyze)

                BestRef.setBookWeeklyBest(
                    platform = platform,
                    genre = genre,
                    bookCode = item.bookCode,
                    type = type
                ).setValue(bestListAnalyze)

                BestRef.setBookMonthlyBest(
                    platform = platform,
                    genre = genre,
                    bookCode = item.bookCode,
                    type = type
                ).setValue(bestListAnalyze)

                BestRef.setBookWeeklyBestTotal(
                    platform = platform,
                    genre = genre,
                    bookCode = item.bookCode,
                    type = type
                ).setValue(bestListAnalyze)

                BestRef.setBookMonthlyBestTotal(
                    platform = platform,
                    genre = genre,
                    bookCode = item.bookCode,
                    type = type
                ).setValue(bestListAnalyze)

                jsonArray.add(convertItemBook(item))
            }

            val jsonArrayByteArray = jsonArray.toString().toByteArray(Charsets.UTF_8)

            todayFileRef.putBytes(jsonArrayByteArray)
                .addOnSuccessListener {
                    Log.d(
                        "TROPHY_MINING $genre",
                        "calculateTrophy addOnFailureListener addOnSuccessListener"
                    )

                    uploadJsonArrayToStorageTrophyWeek(
                        platform = platform,
                        genre = genre,
                        type = type
                    )

                }.addOnFailureListener {
                    Log.d(
                        "TROPHY_MINING $genre",
                        "calculateTrophy addOnFailureListener addOnFailureListener == $it"
                    )
                }

        }
    }

}

private fun uploadJsonArrayToStorageTrophyWeek(
    platform: String,
    genre: String,
    type: String
) {

    val route = BestRef.setBestRef(platform = platform, genre = genre, type = type)
        .child("TROPHY_WEEK_TOTAL")

    val storage = Firebase.storage
    val storageRef = storage.reference
    val jsonArrayRef =
        storageRef.child("${platform}/${type}/${genre}/WEEK_TROPHY/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json")

    route.addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {

                val jsonArray = JsonArray()

                for (postSnapshot in dataSnapshot.children) {
                    val group: ItemBestInfo? = postSnapshot.getValue(ItemBestInfo::class.java)
                    jsonArray.add(convertItemBest(group ?: ItemBestInfo()))
                }

                val jsonArrayByteArray = jsonArray.toString().toByteArray(Charsets.UTF_8)


                jsonArrayRef.putBytes(jsonArrayByteArray)
                    .addOnSuccessListener {
                        Log.d(
                            "TROPHY_MINING $genre",
                            "uploadJsonArrayToStorageTrophyWeek addOnSuccessListener"
                        )


                        uploadJsonArrayToStorageTrophyMonth(
                            platform = platform,
                            genre = genre,
                            type = type
                        )

                    }.addOnFailureListener {
                        Log.d(
                            "TROPHY_MINING $genre",
                            "uploadJsonArrayToStorageTrophyWeek addOnFailureListener == $it"
                        )
                    }
            } else {
                Log.d("uploadJsonArrayToStorageTrophy", "route 실패")
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    })
}

private fun uploadJsonArrayToStorageTrophyMonth(
    platform: String,
    genre: String,
    type: String
) {

    val route = BestRef.setBestRef(platform = platform, genre = genre, type = type)
        .child("TROPHY_MONTH_TOTAL")

    val storage = Firebase.storage
    val storageRef = storage.reference
    val jsonArrayRef =
        storageRef.child("${platform}/${type}/${genre}/MONTH_TROPHY/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json")

    route.addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {

                val jsonArray = JsonArray()

                for (postSnapshot in dataSnapshot.children) {
                    val group: ItemBestInfo? = postSnapshot.getValue(ItemBestInfo::class.java)
                    jsonArray.add(convertItemBest(group ?: ItemBestInfo()))
                }

                val jsonArrayByteArray = jsonArray.toString().toByteArray(Charsets.UTF_8)


                jsonArrayRef.putBytes(jsonArrayByteArray)
                    .addOnSuccessListener {
                        Log.d(
                            "TROPHY_MINING $genre",
                            "uploadJsonArrayToStorageTrophyMonth addOnSuccessListener"
                        )
                    }.addOnFailureListener {
                        Log.d(
                            "TROPHY_MINING $genre",
                            "uploadJsonArrayToStorageTrophyMonth addOnFailureListener == $it"
                        )
                    }
            } else {
                Log.d("uploadJsonArrayToStorageTrophy", "route 실패")
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    })
}

fun doMining(
    context : Context,
    genre: String,
    platform: String,
    type: String,
    yesterDayItemMap: MutableMap<String, ItemBookInfo>
) {

    if(platform == "NAVER_SERIES"){
        if(type == "COMIC"){
            miningNaverSeriesComic(
                genre = genre,
                platform = platform,
                type = type,
                yesterDatItemMap = yesterDayItemMap
            ) { itemBookInfoList, itemBestInfoList ->
                doResultMining(
                    genre = getNaverSeriesGenre(genre),
                    platform = platform,
                    type = type,
                    itemBookInfoList = itemBookInfoList,
                    itemBestInfoList = itemBestInfoList
                )
            }
        } else {
            MiningSource.miningNaverSeriesNovel(
                genre = genre,
                platform = platform,
                type = type,
                yesterDatItemMap = yesterDayItemMap
            ) { itemBookInfoList, itemBestInfoList ->
                doResultMining(
                    genre = getNaverSeriesGenre(genre),
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
            genre = genre,
            platform = platform,
            type = type,
            yesterDatItemMap = yesterDayItemMap
        ) { itemBookInfoList, itemBestInfoList ->
            doResultMining(
                genre = getJoaraGenre(genre),
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
            genre = genre,
            platform = platform,
            type = type,
            yesterDatItemMap = yesterDayItemMap
        ) { itemBookInfoList, itemBestInfoList ->
            doResultMining(
                genre = getJoaraGenre(genre),
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
            genre = genre,
            platform = platform,
            type = type,
            yesterDatItemMap = yesterDayItemMap
        ) { itemBookInfoList, itemBestInfoList ->
            doResultMining(
                genre = getJoaraGenre(genre),
                platform = platform,
                type = type,
                itemBookInfoList = itemBookInfoList,
                itemBestInfoList = itemBestInfoList
            )
        }
    } else if(platform == "NAVER_CHALLENGE") {
        MiningSource.miningNaverChallenge(
            genre = genre,
            platform = platform,
            type = type,
            yesterDatItemMap = yesterDayItemMap
        ) { itemBookInfoList, itemBestInfoList ->
            doResultMining(
                genre = getChallengeGenre(genre),
                platform = platform,
                type = type,
                itemBookInfoList = itemBookInfoList,
                itemBestInfoList = itemBestInfoList
            )
        }
    }
}

private fun doResultMining(
    genre: String,
    platform: String,
    type: String,
    itemBookInfoList: JsonArray,
    itemBestInfoList: JsonArray
) {

    runBlocking {
        makeTodayJson(
            platform = platform,
            genre = genre,
            type = type,
            todayArray = itemBookInfoList
        )
    }
    runBlocking {
        uploadJsonTrophyWeek(
            platform = platform,
            genre = genre,
            type = type,
            itemBestInfoList = itemBestInfoList
        )
    }
    runBlocking {
        uploadJsonTrophyMonth(
            platform = platform,
            genre = genre,
            type = type,
            itemBestInfoList = itemBestInfoList
        )
    }
}

fun makeTodayJson(
    platform: String,
    genre: String,
    type: String,
    todayArray: JsonArray
) {

    val storage = Firebase.storage
    val storageRef = storage.reference
    val jsonArrayRef =
        storageRef.child("${platform}/${type}/${genre}/DAY/${DBDate.dateMMDD()}.json")

    val jsonArrayByteArray = todayArray.toString().toByteArray(Charsets.UTF_8)

    jsonArrayRef.putBytes(jsonArrayByteArray)
        .addOnSuccessListener {
            Log.d("MANANVARA_MINING", "makeTodayJson addOnSuccessListener == $it")

            uploadWeekJson(
                platform = platform,
                genre = genre,
                type = type,
                todayArray = todayArray
            )
        }.addOnFailureListener {
            Log.d("MANANVARA_MINING", "makeTodayJson addOnFailureListener == $it")
        }
}

private fun uploadWeekJson(
    platform: String,
    genre: String,
    type: String,
    todayArray: JsonArray
) {
    val storage = Firebase.storage
    val storageRef = storage.reference

    val jsonFileRef =
        storageRef.child("${platform}/${type}/${genre}/WEEK/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json") // 읽어올 JSON 파일의 경로

    val jsonMonthRef =
        storageRef.child("${platform}/${type}/${genre}/MONTH/${DBDate.year()}_${DBDate.month()}.json")

    jsonFileRef.getBytes(1024 * 1024)
        .addOnSuccessListener { bytes ->
            val jsonString = String(bytes, Charset.forName("UTF-8"))
            val weekArray = JsonParser().parse(jsonString).asJsonArray

            makeWeekJson(
                platform = platform,
                genre = genre,
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
                genre = genre,
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
                genre = genre,
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
                genre = genre,
                jsonMonthArray = jsonArray,
                type = type,
                todayArray = todayArray
            )
        }
}

private fun makeWeekJson(
    platform: String,
    genre: String,
    weekArray: JsonArray,
    type: String,
    todayArray: JsonArray
) {
    val storage = Firebase.storage
    val storageRef = storage.reference

    val jsonWeekRef =
        storageRef.child("${platform}/${type}/${genre}/WEEK/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json")

    val indexNum = DBDate.getDayOfWeekAsNumber()

    val clonedWeekArray = weekArray.deepCopy()

    clonedWeekArray.set(indexNum, todayArray)

    val mRootRef = FirebaseDatabase.getInstance().reference.child("WORKER")

    val year = DBDate.dateMMDDHHMM().substring(0,4)
    val month = DBDate.dateMMDDHHMM().substring(4,6)
    val day = DBDate.dateMMDDHHMM().substring(6,8)
    val hour = DBDate.dateMMDDHHMM().substring(8,10)
    val min = DBDate.dateMMDDHHMM().substring(10,12)

    try{
        val jsonBytes = clonedWeekArray.toString().toByteArray(Charsets.UTF_8)

        jsonWeekRef.putBytes(jsonBytes)
            .addOnSuccessListener {
                Log.d("makeWeekJson", "jsonWeekRef 성공")
                mRootRef.child("STATUS_${platform}_${type}_${genre}").setValue("성공 ${year}.${month}.${day} ${hour}:${min}")
            }.addOnFailureListener {
                Log.d("makeWeekJson", "jsonWeekRef 실패")
                mRootRef.child("STATUS_${platform}_${type}_${genre}").setValue("실패 $it")
            }


    } catch (e : Exception){

        mRootRef.child("STATUS_${platform}_${type}_${genre}").setValue("실패 ${year}.${month}.${day} ${hour}:${min}")
    }
}

private fun makeMonthJson(
    platform: String,
    genre: String,
    jsonMonthArray: JsonArray,
    type: String,
    todayArray: JsonArray
) {
    val storage = Firebase.storage
    val storageRef = storage.reference

    val jsonMonthRef =
        storageRef.child("${platform}/${type}/${genre}/MONTH/${DBDate.year()}_${DBDate.month()}.json")

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
            Log.d("JSON_MINING $genre", "uploadJsonArrayToStorageMonth makeMonthJson")
        }.addOnFailureListener {
            Log.d("JSON_MINING $genre", "uploadJsonArrayToStorageMonth makeMonthJson")
        }
}

fun uploadJsonTrophyWeek(
    platform: String,
    genre: String,
    type: String,
    itemBestInfoList: JsonArray
) {

    val storage = Firebase.storage
    val storageRef = storage.reference
    val jsonArrayRef =
        storageRef.child("${platform}/${type}/${genre}/WEEK_TROPHY/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json")

    val jsonArrayByteArray = itemBestInfoList.toString().toByteArray(Charsets.UTF_8)

    jsonArrayRef.putBytes(jsonArrayByteArray)
        .addOnSuccessListener {
            Log.d(
                "TROPHY_MINING $genre",
                "uploadJsonArrayToStorageTrophyWeek addOnSuccessListener"
            )

        }.addOnFailureListener {
            Log.d(
                "TROPHY_MINING $genre",
                "uploadJsonArrayToStorageTrophyWeek addOnFailureListener == $it"
            )
        }
}

fun uploadJsonTrophyMonth(
    platform: String,
    genre: String,
    type: String,
    itemBestInfoList: JsonArray
) {

    val storage = Firebase.storage
    val storageRef = storage.reference
    val jsonArrayRef =
        storageRef.child("${platform}/${type}/${genre}/MONTH_TROPHY/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json")

    val jsonArrayByteArray = itemBestInfoList.toString().toByteArray(Charsets.UTF_8)


    jsonArrayRef.putBytes(jsonArrayByteArray)
        .addOnSuccessListener {
            Log.d(
                "TROPHY_MINING $genre",
                "uploadJsonArrayToStorageTrophyMonth addOnSuccessListener"
            )
        }.addOnFailureListener {
            Log.d(
                "TROPHY_MINING $genre",
                "uploadJsonArrayToStorageTrophyMonth addOnFailureListener == $it"
            )
        }
}