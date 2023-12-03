package com.bigbigdw.manavarasetting.util

import android.content.Context
import com.bigbigdw.manavarasetting.main.model.ItemBestInfo
import com.bigbigdw.manavarasetting.main.model.ItemBookInfo
import com.bigbigdw.manavarasetting.main.model.ItemGenre
import com.bigbigdw.manavarasetting.main.model.ItemKeyword
import com.bigbigdw.manavarasetting.util.MiningSource.miningNaverSeriesComic
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.runBlocking

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
    ).setValue(setItemBookInfoRef(ref))

    BestRef.setBestTrophy(
        platform = platform,
        bookCode = ref["bookCode"] as String,
        type = type
    ).setValue(setItemBestInfoRef(ref))

    BestRef.setBookWeeklyBest(
        platform = platform,
        bookCode = ref["bookCode"] as String,
        type = type
    ).setValue(setItemBestInfoRef(ref))

    BestRef.setBookMonthlyBest(
        platform = platform,
        bookCode = ref["bookCode"] as String,
        type = type
    ).setValue(setItemBestInfoRef(ref))

    BestRef.setBookDailyBest(platform = platform, num = num, type = type)
        .setValue(setItemBookInfoRef(ref))

    BestRef.setBestData(
        platform = platform,
        bookCode = ref["bookCode"] as String,
        type = type
    ).setValue(setItemBestInfoRef(ref))

    BestRef.setBookWeeklyBestTotal(
        platform = platform,
        bookCode = ref["bookCode"] as String,
        type = type
    ).setValue(setItemBestInfoRef(ref))

    BestRef.setBookMonthlyBestTotal(
        platform = platform,
        bookCode = ref["bookCode"] as String,
        type = type
    ).setValue(setItemBestInfoRef(ref))

}

fun miningGenre(
    ref: MutableMap<String, Int>,
    platform: String,
    type: String,
    genreList: ArrayList<ItemGenre>
) {

    for(item in genreList){
        BestRef.setGenreRef(
            platform = platform,
            type = type
        ).child(item.title).child(item.date).setValue(item)
    }

    BestRef.setBestGenre(
        platform = platform,
        genreDate = "GENRE_DAY",
        type = type
    ).setValue(ref)

    BestRef.setBestGenre(
        platform = platform,
        genreDate = "GENRE_WEEK",
        type = type
    ).child(DBDate.getDayOfWeekAsNumber().toString()).setValue(ref)

    BestRef.setBestGenre(
        platform = platform,
        genreDate = "GENRE_MONTH",
        type = type
    ).child(DBDate.datedd()).setValue(ref)
}

fun doMining(
    context: Context,
    platform: String,
    type: String,
    yesterDayItemMap: MutableMap<String, ItemBookInfo>
) {

    if(platform == "NAVER_SERIES"){
        if(type == "COMIC"){

            val totalBookItem: MutableMap<Int, ItemBookInfo> = HashMap()
            val totalBestItem: MutableMap<Int, ItemBestInfo> = HashMap()

            miningNaverSeriesComic(
                platform = platform,
                type = type,
                totalBookItem = totalBookItem,
                totalBestItem = totalBestItem,
                yesterDayItemMap = yesterDayItemMap
            ) { itemBook, itemBest ->

                if(itemBook.size > 99 && itemBest.size > 99){
                    val itemBookInfoList = JsonArray()
                    val itemBestInfoList = JsonArray()

                    for(item in 0 until itemBook.size){
                        itemBookInfoList.add(convertItemBook(itemBook[item] ?: ItemBookInfo()))
                        itemBestInfoList.add(convertItemBest(itemBest[item] ?: ItemBestInfo()))
                    }

                    doResultMining(
                        context = context,
                        platform = platform,
                        type = type,
                        itemBookInfoList = itemBookInfoList
                    )
                }
            }
        } else {

            val totalBookItem: MutableMap<Int, ItemBookInfo> = HashMap()
            val totalBestItem: MutableMap<Int, ItemBestInfo> = HashMap()

            MiningSource.miningNaverSeriesNovel(
                platform = platform,
                type = type,
                totalBookItem = totalBookItem,
                totalBestItem = totalBestItem,
                yesterDayItemMap = yesterDayItemMap
            ) { itemBook, itemBest ->

                if(itemBook.size > 99 && itemBest.size > 99){
                    val itemBookInfoList = JsonArray()
                    val itemBestInfoList = JsonArray()

                    for(item in 0 until itemBook.size){
                        itemBookInfoList.add(convertItemBook(itemBook[item] ?: ItemBookInfo()))
                        itemBestInfoList.add(convertItemBest(itemBest[item] ?: ItemBestInfo()))
                    }

                    doResultMining(
                        context = context,
                        platform = platform,
                        type = type,
                        itemBookInfoList = itemBookInfoList
                    )
                }
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

            doMining(
                context = context,
                platform = platform,
                type = type,
                itemBookInfoList = itemBookInfoList
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

            doMining(
                context = context,
                platform = platform,
                type = type,
                itemBookInfoList = itemBookInfoList
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

            doMining(
                context = context,
                platform = platform,
                type = type,
                itemBookInfoList = itemBookInfoList
            )

        }
    } else if(platform == "NAVER_WEBNOVEL_FREE") {
        MiningSource.miningNaver(
            platform = platform,
            mining = "webnovel",
            type = type,
            platformType = "FREE",
            yesterDayItemMap = yesterDayItemMap
        ) { itemBookInfoList, itemBestInfoList ->
            doResultMining(
                context = context,
                platform = platform,
                type = type,
                itemBookInfoList = itemBookInfoList
            )
        }
    } else if(platform == "NAVER_WEBNOVEL_PAY") {
        MiningSource.miningNaver(
            platform = platform,
            mining = "webnovel",
            type = type,
            platformType = "PAY",
            yesterDayItemMap = yesterDayItemMap
        ) { itemBookInfoList, itemBestInfoList ->
            doResultMining(
                context = context,
                platform = platform,
                type = type,
                itemBookInfoList = itemBookInfoList
            )
        }
    }  else if(platform == "NAVER_CHALLENGE") {
        MiningSource.miningNaver(
            platform = platform,
            mining = "challenge",
            type = type,
            platformType = "FREE",
            yesterDayItemMap = yesterDayItemMap
        ) { itemBookInfoList, itemBestInfoList ->
            doResultMining(
                context = context,
                platform = platform,
                type = type,
                itemBookInfoList = itemBookInfoList
            )
        }
    } else if(platform == "NAVER_BEST") {
        MiningSource.miningNaver(
            platform = platform,
            mining = "best",
            type = type,
            platformType = "PAY",
            yesterDayItemMap = yesterDayItemMap
        ) { itemBookInfoList, itemBestInfoList ->
            doResultMining(
                context = context,
                platform = platform,
                type = type,
                itemBookInfoList = itemBookInfoList
            )
        }
    } else if(platform == "ONESTORY_FANTAGY") {

        if(type == "COMIC"){

        } else{

            val totalBookItem: MutableMap<Int, ItemBookInfo> = HashMap()
            val totalBestItem: MutableMap<Int, ItemBestInfo> = HashMap()

            MiningSource.miningOnestory(
                platform = platform,
                platformType = "DP13041|DP13042|DP13043|DP13044",
                type = type,
                totalBookItem = totalBookItem,
                totalBestItem = totalBestItem,
                yesterDayItemMap = yesterDayItemMap
            ) { itemBook, itemBest ->

                if(itemBook.size > 99 && itemBest.size > 99){
                    val itemBookInfoList = JsonArray()
                    val itemBestInfoList = JsonArray()

                    for(item in 0 until itemBook.size){
                        itemBookInfoList.add(convertItemBook(itemBook[item] ?: ItemBookInfo()))
                        itemBestInfoList.add(convertItemBest(itemBest[item] ?: ItemBestInfo()))
                    }

                    doResultMining(
                        context = context,
                        platform = platform,
                        type = type,
                        itemBookInfoList = itemBookInfoList
                    )
                }
            }
        }

    } else if(platform == "ONESTORY_ROMANCE") {

        if(type == "COMIC"){

        } else{

            val totalBookItem: MutableMap<Int, ItemBookInfo> = HashMap()
            val totalBestItem: MutableMap<Int, ItemBestInfo> = HashMap()

            MiningSource.miningOnestory(
                platform = platform,
                platformType = "DP13040|DP13045|DP13046|DP13047|DP13048|DP13031|DP13055",
                type = type,
                totalBookItem = totalBookItem,
                totalBestItem = totalBestItem,
                yesterDayItemMap = yesterDayItemMap
            ) { itemBook, itemBest ->

                if(itemBook.size > 99 && itemBest.size > 99){
                    val itemBookInfoList = JsonArray()
                    val itemBestInfoList = JsonArray()

                    for(item in 0 until itemBook.size){
                        itemBookInfoList.add(convertItemBook(itemBook[item] ?: ItemBookInfo()))
                        itemBestInfoList.add(convertItemBest(itemBest[item] ?: ItemBestInfo()))
                    }

                    doResultMining(
                        context = context,
                        platform = platform,
                        type = type,
                        itemBookInfoList = itemBookInfoList
                    )
                }
            }
        }

    }  else if(platform == "ONESTORY_PASS_FANTAGY") {

        if(type == "COMIC"){

        } else{

            val totalBookItem: MutableMap<Int, ItemBookInfo> = HashMap()
            val totalBestItem: MutableMap<Int, ItemBestInfo> = HashMap()

            MiningSource.miningOnestoryPass(
                platform = platform,
                platformType = "DP13041|DP13042|DP13043|DP13044",
                type = type,
                totalBookItem = totalBookItem,
                totalBestItem = totalBestItem,
                yesterDayItemMap = yesterDayItemMap
            ) { itemBook, itemBest ->

                if(itemBook.size > 99 && itemBest.size > 99){
                    val itemBookInfoList = JsonArray()
                    val itemBestInfoList = JsonArray()

                    for(item in 0 until itemBook.size){
                        itemBookInfoList.add(convertItemBook(itemBook[item] ?: ItemBookInfo()))
                        itemBestInfoList.add(convertItemBest(itemBest[item] ?: ItemBestInfo()))
                    }

                    doResultMining(
                        context = context,
                        platform = platform,
                        type = type,
                        itemBookInfoList = itemBookInfoList
                    )
                }
            }
        }

    } else if(platform == "ONESTORY_PASS_ROMANCE") {

        if(type == "COMIC"){

        } else{

            val totalBookItem: MutableMap<Int, ItemBookInfo> = HashMap()
            val totalBestItem: MutableMap<Int, ItemBestInfo> = HashMap()

            MiningSource.miningOnestoryPass(
                platform = platform,
                platformType = "DP13040|DP13045|DP13046|DP13047|DP13048|DP13031|DP13055",
                type = type,
                totalBookItem = totalBookItem,
                totalBestItem = totalBestItem,
                yesterDayItemMap = yesterDayItemMap
            ) { itemBook, itemBest ->

                if(itemBook.size > 99 && itemBest.size > 99){
                    val itemBookInfoList = JsonArray()
                    val itemBestInfoList = JsonArray()

                    for(item in 0 until itemBook.size){
                        itemBookInfoList.add(convertItemBook(itemBook[item] ?: ItemBookInfo()))
                        itemBestInfoList.add(convertItemBest(itemBest[item] ?: ItemBestInfo()))
                    }

                    doResultMining(
                        context = context,
                        platform = platform,
                        type = type,
                        itemBookInfoList = itemBookInfoList
                    )
                }
            }
        }

    } else if(platform == "KAKAO_STAGE") {
        MiningSource.miningKakaoStage(
            platform = platform,
            type = type,
            yesterDayItemMap = yesterDayItemMap
        ) { itemBookInfoList, itemBestInfoList ->

            doMining(
                context = context,
                platform = platform,
                type = type,
                itemBookInfoList = itemBookInfoList
            )

        }
    } else if(platform == "MUNPIA_FREE") {

        val totalBookItem: MutableMap<Int, ItemBookInfo> = HashMap()
        val totalBestItem: MutableMap<Int, ItemBestInfo> = HashMap()

        MiningSource.miningMunpia(
            platform = platform,
            platformType = "today",
            type = type,
            totalBookItem = totalBookItem,
            totalBestItem = totalBestItem,
            yesterDayItemMap = yesterDayItemMap
        ) { itemBook, itemBest ->

            if(itemBook.size > 99 && itemBest.size > 99){
                val itemBookInfoList = JsonArray()
                val itemBestInfoList = JsonArray()

                for(item in 0 until itemBook.size){
                    itemBookInfoList.add(convertItemBook(itemBook[item] ?: ItemBookInfo()))
                    itemBestInfoList.add(convertItemBest(itemBest[item] ?: ItemBestInfo()))
                }

                doMining(
                    context = context,
                    platform = platform,
                    type = type,
                    itemBookInfoList = itemBookInfoList
                )

            }
        }
    }  else if(platform == "MUNPIA_PAY") {

        val totalBookItem: MutableMap<Int, ItemBookInfo> = HashMap()
        val totalBestItem: MutableMap<Int, ItemBestInfo> = HashMap()

        MiningSource.miningMunpia(
            platform = platform,
            platformType = "plsa.eachtoday",
            type = type,
            totalBookItem = totalBookItem,
            totalBestItem = totalBestItem,
            yesterDayItemMap = yesterDayItemMap
        ) { itemBook, itemBest ->

            if(itemBook.size > 99 && itemBest.size > 99){
                val itemBookInfoList = JsonArray()
                val itemBestInfoList = JsonArray()

                for(item in 0 until itemBook.size){
                    itemBookInfoList.add(convertItemBook(itemBook[item] ?: ItemBookInfo()))
                    itemBestInfoList.add(convertItemBest(itemBest[item] ?: ItemBestInfo()))
                }

                doMining(
                    context = context,
                    platform = platform,
                    type = type,
                    itemBookInfoList = itemBookInfoList
                )
            }
        }
    } else if(platform == "TOKSODA") {

        val totalBookItem: MutableMap<Int, ItemBookInfo> = HashMap()
        val totalBestItem: MutableMap<Int, ItemBestInfo> = HashMap()

        MiningSource.miningToksoda(
            platform = platform,
            platformType = "00431",
            type = type,
            totalBookItem = totalBookItem,
            totalBestItem = totalBestItem,
            yesterDayItemMap = yesterDayItemMap
        ) { itemBook, itemBest ->

            if(itemBook.size > 99 && itemBest.size > 99){

                val itemBookInfoList = JsonArray()
                val itemBestInfoList = JsonArray()

                for(item in 0 until itemBook.size){
                    itemBookInfoList.add(convertItemBook(itemBook[item] ?: ItemBookInfo()))
                    itemBestInfoList.add(convertItemBest(itemBest[item] ?: ItemBestInfo()))
                }

                doMining(
                    context = context,
                    platform = platform,
                    type = type,
                    itemBookInfoList = itemBookInfoList
                )
            }
        }

    }  else if(platform == "TOKSODA_FREE") {

        val totalBookItem: MutableMap<Int, ItemBookInfo> = HashMap()
        val totalBestItem: MutableMap<Int, ItemBestInfo> = HashMap()

        MiningSource.miningToksoda(
            platform = platform,
            platformType = "00024",
            type = type,
            totalBookItem = totalBookItem,
            totalBestItem = totalBestItem,
            yesterDayItemMap = yesterDayItemMap
        ) { itemBook, itemBest ->

            if(itemBook.size > 99 && itemBest.size > 99){

                val itemBookInfoList = JsonArray()
                val itemBestInfoList = JsonArray()

                for(item in 0 until itemBook.size){
                    itemBookInfoList.add(convertItemBook(itemBook[item] ?: ItemBookInfo()))
                    itemBestInfoList.add(convertItemBest(itemBest[item] ?: ItemBestInfo()))
                }

                doMining(
                    context = context,
                    platform = platform,
                    type = type,
                    itemBookInfoList = itemBookInfoList
                )
            }
        }

    } else if (platform == "RIDI_FANTAGY") {
        val totalBookItem: MutableMap<Int, ItemBookInfo> = HashMap()
        val totalBestItem: MutableMap<Int, ItemBestInfo> = HashMap()

        MiningSource.miningRidi(
            mining = "1750",
            platform = platform,
            type = type,
            totalBookItem = totalBookItem,
            totalBestItem = totalBestItem,
            yesterDayItemMap = yesterDayItemMap
        ) { itemBook, itemBest ->

            val itemBookInfoList = JsonArray()
            val itemBestInfoList = JsonArray()
            for (item in 0 until itemBook.size) {
                itemBookInfoList.add(convertItemBook(itemBook[item] ?: ItemBookInfo()))
                itemBestInfoList.add(convertItemBest(itemBest[item] ?: ItemBestInfo()))
            }

            doMining(
                context = context,
                platform = platform,
                type = type,
                itemBookInfoList = itemBookInfoList
            )
        }
    }  else if (platform == "RIDI_ROFAN") {
        val totalBookItem: MutableMap<Int, ItemBookInfo> = HashMap()
        val totalBestItem: MutableMap<Int, ItemBestInfo> = HashMap()

        MiningSource.miningRidi(
            mining = "6050",
            platform = platform,
            type = type,
            totalBookItem = totalBookItem,
            totalBestItem = totalBestItem,
            yesterDayItemMap = yesterDayItemMap
        ) { itemBook, itemBest ->

            val itemBookInfoList = JsonArray()
            val itemBestInfoList = JsonArray()
            for (item in 0 until itemBook.size) {
                itemBookInfoList.add(convertItemBook(itemBook[item] ?: ItemBookInfo()))
                itemBestInfoList.add(convertItemBest(itemBest[item] ?: ItemBestInfo()))
            }

            doMining(
                context = context,
                platform = platform,
                type = type,
                itemBookInfoList = itemBookInfoList
            )
        }
    } else if (platform == "RIDI_ROMANCE") {
        val totalBookItem: MutableMap<Int, ItemBookInfo> = HashMap()
        val totalBestItem: MutableMap<Int, ItemBestInfo> = HashMap()

        MiningSource.miningRidi(
            mining = "1650",
            platform = platform,
            type = type,
            totalBookItem = totalBookItem,
            totalBestItem = totalBestItem,
            yesterDayItemMap = yesterDayItemMap
        ) { itemBook, itemBest ->

            val itemBookInfoList = JsonArray()
            val itemBestInfoList = JsonArray()
            for (item in 0 until itemBook.size) {
                itemBookInfoList.add(convertItemBook(itemBook[item] ?: ItemBookInfo()))
                itemBestInfoList.add(convertItemBest(itemBest[item] ?: ItemBestInfo()))
            }

            doMining(
                context = context,
                platform = platform,
                type = type,
                itemBookInfoList = itemBookInfoList
            )

        }
    }
}

private fun doResultMining(
    context: Context,
    platform: String,
    type: String,
    itemBookInfoList: JsonArray
) {
    val storage = Firebase.storage
    val storageRef = storage.reference

    val today = storageRef.child("${platform}/${type}/BEST_DAY/${DBDate.dateMMDD()}.json")
    val week =  storageRef.child("${platform}/${type}/BEST_WEEK/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json")
    val month = storageRef.child("${platform}/${type}/BEST_MONTH/${DBDate.year()}_${DBDate.month()}.json")

    runBlocking {
        makeTodayJson(
            today = today,
            todayArray = itemBookInfoList,
            week = week,
            month = month,
            type = "BEST"
        )
    }
    runBlocking {
        uploadJsonTrophyWeek(
            platform = platform,
            type = type,
        )
    }
    runBlocking {
        uploadJsonTrophyMonth(
            platform = platform,
            type = type,
        )
    }

    runBlocking {
        saveKeyword(
            context = context,
            platform = platform,
            type = type,
            itemBookInfoList = itemBookInfoList
        ){

            val jsonArray = JsonArray()

            val keywordList = ArrayList<ItemKeyword>()

            it.forEach { (key, value) ->
                val jsonObject = JsonObject()
                jsonObject.addProperty("key", key)
                jsonObject.addProperty("value", value)
                jsonArray.add(jsonObject)

                keywordList.add(
                    ItemKeyword(
                        key = key,
                        value = value,
                        date = DBDate.dateMMDD()
                    )
                )
            }

            for(item in keywordList){
                BestRef.setKeywordRef(
                    platform = platform,
                    type = type
                ).child(item.key).child(item.date).setValue(item)
            }

            val genreToday = storageRef.child("${platform}/${type}/KEYWORD_DAY/${DBDate.dateMMDD()}.json")
            val genreWeek =  storageRef.child("${platform}/${type}/KEYWORD_WEEK/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json")
            val genreMonth = storageRef.child("${platform}/${type}/KEYWORD_MONTH/${DBDate.year()}_${DBDate.month()}.json")

            runBlocking {
                makeTodayJson(
                    today = genreToday,
                    todayArray = jsonArray,
                    week = genreWeek,
                    month = genreMonth,
                    type = "KEYWORD"
                )
            }
        }
    }
}

fun doMining(
    context: Context,
    platform: String,
    type: String,
    itemBookInfoList: JsonArray
) {
    runBlocking {
        doResultMining(
            context = context,
            platform = platform,
            type = type,
            itemBookInfoList = itemBookInfoList
        )
    }

    runBlocking {
        doResultMiningGenre(
            platform = platform,
            type = type,
            itemBookInfoList = itemBookInfoList
        )
    }
}