package com.bigbigdw.manavarasetting.util

import android.content.Context
import android.util.Log
import com.bigbigdw.manavarasetting.main.model.ItemBestInfo
import com.bigbigdw.manavarasetting.main.model.ItemBestKeyword
import com.bigbigdw.manavarasetting.main.model.ItemBookInfo
import com.bigbigdw.manavarasetting.util.MiningSource.miningNaverSeriesComic
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

fun miningGenre(
    ref: MutableMap<String, Int>,
    platform: String,
    type: String
) {
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
                        platform = platform,
                        type = type,
                        itemBookInfoList = itemBookInfoList,
                        itemBestInfoList = itemBestInfoList
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
                        platform = platform,
                        type = type,
                        itemBookInfoList = itemBookInfoList,
                        itemBestInfoList = itemBestInfoList
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

            doMining(
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

            doMining(
                platform = platform,
                type = type,
                itemBookInfoList = itemBookInfoList,
                itemBestInfoList = itemBestInfoList
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
                platform = platform,
                type = type,
                itemBookInfoList = itemBookInfoList,
                itemBestInfoList = itemBestInfoList
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
                platform = platform,
                type = type,
                itemBookInfoList = itemBookInfoList,
                itemBestInfoList = itemBestInfoList
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
                platform = platform,
                type = type,
                itemBookInfoList = itemBookInfoList,
                itemBestInfoList = itemBestInfoList
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
                platform = platform,
                type = type,
                itemBookInfoList = itemBookInfoList,
                itemBestInfoList = itemBestInfoList
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
                        platform = platform,
                        type = type,
                        itemBookInfoList = itemBookInfoList,
                        itemBestInfoList = itemBestInfoList
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
                        platform = platform,
                        type = type,
                        itemBookInfoList = itemBookInfoList,
                        itemBestInfoList = itemBestInfoList
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
                        platform = platform,
                        type = type,
                        itemBookInfoList = itemBookInfoList,
                        itemBestInfoList = itemBestInfoList
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
                        platform = platform,
                        type = type,
                        itemBookInfoList = itemBookInfoList,
                        itemBestInfoList = itemBestInfoList
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
                platform = platform,
                type = type,
                itemBookInfoList = itemBookInfoList,
                itemBestInfoList = itemBestInfoList
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
                    platform = platform,
                    type = type,
                    itemBookInfoList = itemBookInfoList,
                    itemBestInfoList = itemBestInfoList
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
                    platform = platform,
                    type = type,
                    itemBookInfoList = itemBookInfoList,
                    itemBestInfoList = itemBestInfoList
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
                    platform = platform,
                    type = type,
                    itemBookInfoList = itemBookInfoList,
                    itemBestInfoList = itemBestInfoList
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
                    platform = platform,
                    type = type,
                    itemBookInfoList = itemBookInfoList,
                    itemBestInfoList = itemBestInfoList
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
                platform = platform,
                type = type,
                itemBookInfoList = itemBookInfoList,
                itemBestInfoList = itemBestInfoList
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
    val storage = Firebase.storage
    val storageRef = storage.reference

    val today = storageRef.child("${platform}/${type}/DAY/${DBDate.dateMMDD()}.json")
    val week =  storageRef.child("${platform}/${type}/WEEK/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json")
    val month = storageRef.child("${platform}/${type}/MONTH/${DBDate.year()}_${DBDate.month()}.json")

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

fun doMining(
    platform: String,
    type: String,
    itemBookInfoList: JsonArray,
    itemBestInfoList: JsonArray
) {
    runBlocking {
        doResultMining(
            platform = platform,
            type = type,
            itemBookInfoList = itemBookInfoList,
            itemBestInfoList = itemBestInfoList
        )
    }

    runBlocking {
        doResultMiningGenre(
            platform = platform,
            type = type,
            itemBookInfoList = itemBookInfoList,
        )
    }
}