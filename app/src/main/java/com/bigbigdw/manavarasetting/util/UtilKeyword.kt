package com.bigbigdw.manavarasetting.util

import android.content.Context
import android.util.Log
import com.bigbigdw.manavarasetting.main.model.ItemKeyword
import com.bigbigdw.manavarasetting.retrofit.Param
import com.bigbigdw.manavarasetting.retrofit.RetrofitDataListener
import com.bigbigdw.manavarasetting.retrofit.RetrofitJoara
import com.bigbigdw.manavarasetting.retrofit.RetrofitOnestory
import com.bigbigdw.manavarasetting.retrofit.RetrofitToksoda
import com.bigbigdw.manavarasetting.retrofit.result.BestToksodaDetailResult
import com.bigbigdw.manavarasetting.retrofit.result.JoaraBestDetailResult
import com.bigbigdw.manavarasetting.retrofit.result.OnestoreBookDetail
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONArray
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.nio.charset.Charset
import kotlinx.coroutines.GlobalScope

fun getKeyword(
    platform: String,
    bookCode: String,
    context: Context,
    callbacks: (MutableMap<String, String>) -> Unit
) {

    when (platform) {
        "JOARA", "JOARA_NOBLESS", "JOARA_PREMIUM" -> {
            setLayoutJoara(bookCode = bookCode, context = context, callbacks = callbacks)
        }

        "NAVER_WEBNOVEL_FREE", "NAVER_WEBNOVEL_PAY", "NAVER_BEST", "NAVER_CHALLENGE" -> {
            setLayoutNaver(bookCode = bookCode, callbacks = callbacks)
        }

        "RIDI_FANTAGY", "RIDI_ROMANCE", "RIDI_ROFAN" -> {
            setLayoutRidi(bookCode = bookCode, callbacks = callbacks)
        }

        "ONESTORY_FANTAGY", "ONESTORY_ROMANCE", "ONESTORY_PASS_FANTAGY", "ONESTORY_PASS_ROMANCE" -> {
            setLayoutOneStory(bookCode = bookCode, callbacks = callbacks)
        }

        "TOKSODA", "TOKSODA_FREE" -> {
            setLayoutToksoda(bookCode = bookCode, callbacks = callbacks)
        }
    }
}

private fun setLayoutJoara(
    bookCode: String,
    context: Context,
    callbacks: (MutableMap<String, String>) -> Unit
) {
    val apiJoara = RetrofitJoara()
    val JoaraRef = Param.getItemAPI(context)
    JoaraRef["book_code"] = bookCode
    JoaraRef["category"] = "1"

    apiJoara.getBookDetailJoa(
        JoaraRef,
        object : RetrofitDataListener<JoaraBestDetailResult> {
            override fun onSuccess(data: JoaraBestDetailResult) {

                val itemList = mutableMapOf<String, String>()

                if (data.book != null) {

                    for (keyword in data.book.keyword) {

                        itemList[keyword
                            .replace("/", " ")
                            .replace(".", " ")
                            .replace("#", " ")
                            .replace("$", " ")
                            .replace("[", " ")
                            .replace("]", " ")] = bookCode
                    }

                    callbacks(itemList)
                }
            }
        })
}

private fun setLayoutNaver(bookCode: String, callbacks: (MutableMap<String, String>) -> Unit) {
    try{

        val keywordList = mutableMapOf<String, String>()

        val doc: Document =
            Jsoup.connect("https://novel.naver.com/webnovel/list?novelId=${bookCode}").post()

        for (i in doc.select(".tag_collection").indices) {

            val wordList = doc.select(".tag_collection")[i].text().split(" ").toMutableList()

            wordList.replaceAll {
                it
                    .replace("/", " ")
                    .replace(".", " ")
                    .replace("#", " ")
                    .replace("$", " ")
                    .replace("[", " ")
                    .replace("]", " ")
            }

            for (keyword in wordList) {

                keywordList[keyword] = bookCode
                callbacks(keywordList)
            }
        }
    }catch (e : Exception){
        Log.d("MINING-TEST", "NAVER EXCEPTION = e == $e")
    }
}

private fun setLayoutRidi(bookCode: String, callbacks: (MutableMap<String, String>) -> Unit) {
    try{
        val keywordList = mutableMapOf<String, String>()

        val doc: Document =
            Jsoup.connect("https://ridibooks.com/books/${bookCode}").get()

        for (i in doc.select(".keyword_list li").indices) {

            keywordList[doc.select(".keyword_list li")[i].select(".keyword").text()
                .replace("/", " ")
                .replace(".", " ")
                .replace("#", " ")
                .replace("$", " ")
                .replace("[", " ")
                .replace("]", " ")] = bookCode
        }

        callbacks(keywordList)
    }catch (e : Exception){
        Log.d("MINING-TEST", "RIDI EXCEPTION = e == $e")
    }
}

private fun setLayoutOneStory(bookCode: String, callbacks: (MutableMap<String, String>) -> Unit) {

    val apiOnestory = RetrofitOnestory()
    val param: MutableMap<String?, Any> = HashMap()

    param["channelId"] = bookCode
    param["bookpassYn"] = "N"

    apiOnestory.getOneStoreDetail(
        bookCode,
        param,
        object : RetrofitDataListener<OnestoreBookDetail> {
            override fun onSuccess(data: OnestoreBookDetail) {

                val itemList = mutableMapOf<String, String>()

                for (keyword in data.params.tagList.indices) {
                    itemList[data.params.tagList[keyword].tagNm
                        .replace("/", " ")
                        .replace(".", " ")
                        .replace("#", " ")
                        .replace("$", " ")
                        .replace("[", " ")
                        .replace("]", " ")] = bookCode
                }

                callbacks(itemList)

            }
        })
}

private fun setLayoutToksoda(bookCode: String, callbacks: (MutableMap<String, String>) -> Unit) {

    val apiToksoda = RetrofitToksoda()
    val param: MutableMap<String?, Any> = HashMap()

    param["brcd"] = bookCode
    param["_"] = "1657265744728"

    apiToksoda.getBestDetail(
        param,
        object : RetrofitDataListener<BestToksodaDetailResult> {
            override fun onSuccess(data: BestToksodaDetailResult) {

                val itemList = mutableMapOf<String, String>()

                if (data.result.hashTagList != null) {
                    for (keyword in data.result.hashTagList) {
                        itemList[keyword.hashtagNm
                            .replace("/", " ")
                            .replace(".", " ")
                            .replace("#", " ")
                            .replace("$", " ")
                            .replace("[", " ")
                            .replace("]", " ")] = bookCode
                    }

                    callbacks(itemList)
                }
            }
        })
}

fun getJsonKeywordList(platform: String, type: String, callback: (ArrayList<ItemKeyword>) -> Unit) {
    val storage = Firebase.storage
    val storageRef = storage.reference
    val todayFileRef = storageRef.child("${platform}/${type}/KEYWORD_DAY/${DBDate.dateMMDD()}.json")

    val todayFile = todayFileRef.getBytes(1024 * 1024)

    todayFile.addOnSuccessListener { bytes ->
        val jsonString = String(bytes, Charset.forName("UTF-8"))
        val json = Json { ignoreUnknownKeys = true }
        val itemList = json.decodeFromString<List<ItemKeyword>>(jsonString)

        val jsonList = ArrayList<ItemKeyword>()

        for (item in itemList) {
            jsonList.add(item)
        }

        callback(jsonList)
    }
}

fun getJsonKeywordWeekList(
    platform: String,
    type: String,
    callback: (ArrayList<ArrayList<ItemKeyword>>, ArrayList<ItemKeyword>) -> Unit
) {
    val storage = Firebase.storage
    val storageRef = storage.reference

    val fileRef: StorageReference =
        storageRef.child("${platform}/${type}/KEYWORD_WEEK/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json")

    val file = fileRef.getBytes(1024 * 1024)

    file.addOnSuccessListener { bytes ->
        val jsonString = String(bytes, Charset.forName("UTF-8"))

        val jsonArray = JSONArray(jsonString)
        val weekJsonList = ArrayList<ArrayList<ItemKeyword>>()
        val sumList = ArrayList<ItemKeyword>()

        val dataMap = HashMap<String, String>()

        for (i in 0 until jsonArray.length()) {

            try {
                val jsonArrayItem = jsonArray.getJSONArray(i)
                val itemList = ArrayList<ItemKeyword>()

                for (j in 0 until jsonArrayItem.length()) {

                    try {
                        val jsonObject = jsonArrayItem.getJSONObject(j)
                        itemList.add(convertItemKeyword(jsonObject))
                        sumList.add(convertItemKeyword(jsonObject))
                    } catch (e: Exception) {
                        itemList.add(ItemKeyword())
                    }
                }

                weekJsonList.add(itemList)
            } catch (e: Exception) {
                weekJsonList.add(ArrayList())
            }
        }

        for (item in sumList) {

            val key = item.key
            val value = item.value

            if (dataMap[key] != null) {
                dataMap[key] = "${dataMap[key]}, $value"
            } else {
                dataMap[key] = value
            }
        }

        val arrayList = ArrayList<ItemKeyword>()

        for ((key, value) in dataMap) {
            arrayList.add(
                ItemKeyword(
                    key = key,
                    value = value
                )
            )
        }

        callback(weekJsonList, arrayList)

    }
}

fun getJsonKeywordMonthList(
    platform: String,
    type: String,
    callback: (ArrayList<ArrayList<ItemKeyword>>, ArrayList<ItemKeyword>) -> Unit
) {
    val storage = Firebase.storage
    val storageRef = storage.reference

    val fileRef: StorageReference =
        storageRef.child("${platform}/${type}/KEYWORD_MONTH/${DBDate.year()}_${DBDate.month()}.json")

    val file = fileRef.getBytes(1024 * 1024)

    file.addOnSuccessListener { bytes ->
        val jsonString = String(bytes, Charset.forName("UTF-8"))

        val jsonArray = JSONArray(jsonString)
        val monthJsonList = ArrayList<ArrayList<ItemKeyword>>()
        val sumList = ArrayList<ItemKeyword>()

        val dataMap = HashMap<String, String>()

        for (i in 0 until jsonArray.length()) {

            try {
                val jsonArrayItem = jsonArray.getJSONArray(i)
                val itemList = ArrayList<ItemKeyword>()

                for (j in 0 until jsonArrayItem.length()) {

                    try {
                        val jsonObject = jsonArrayItem.getJSONObject(j)
                        itemList.add(convertItemKeyword(jsonObject))
                        sumList.add(convertItemKeyword(jsonObject))
                    } catch (e: Exception) {
                        itemList.add(ItemKeyword())
                    }
                }

                monthJsonList.add(itemList)
            } catch (e: Exception) {
                monthJsonList.add(ArrayList())
            }
        }

        for (item in sumList) {

            val key = item.key
            val value = item.value

            if (dataMap[key] != null) {

                dataMap[key] = "${dataMap[key]}, $value"
            } else {
                dataMap[key] = value
            }
        }

        val arrayList = ArrayList<ItemKeyword>()

        for ((key, value) in dataMap) {
            arrayList.add(
                ItemKeyword(
                    key = key,
                    value = value
                )
            )
        }

        callback(monthJsonList, arrayList)
    }
}