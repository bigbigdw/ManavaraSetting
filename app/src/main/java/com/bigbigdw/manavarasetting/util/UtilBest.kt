package com.bigbigdw.manavarasetting.util

import android.content.Context
import android.util.Log
import com.bigbigdw.manavarasetting.main.model.ItemBestInfo
import com.bigbigdw.manavarasetting.main.model.ItemBookInfo
import com.bigbigdw.manavarasetting.main.model.ItemGenre
import com.bigbigdw.manavarasetting.retrofit.Param
import com.bigbigdw.manavarasetting.retrofit.RetrofitJoara
import com.bigbigdw.manavarasetting.retrofit.RetrofitOnestory
import com.bigbigdw.manavarasetting.retrofit.result.BestToksodaDetailResult
import com.bigbigdw.manavarasetting.retrofit.result.JoaraBestDetailResult
import com.bigbigdw.manavarasetting.retrofit.result.OnestoreBookDetail
import com.bigbigdw.manavarasetting.retrofit.RetrofitDataListener
import com.bigbigdw.manavarasetting.retrofit.RetrofitToksoda
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.google.gson.JsonArray
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONArray
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File
import java.io.FileWriter
import java.nio.charset.Charset
import java.util.Collections

fun getBestListTodayJson(
    context: Context,
    platform: String,
    type: String,
    callbacks: (ArrayList<ItemBookInfo>) -> Unit
) {
    val filePath = File(context.filesDir, "${platform}_TODAY_${type}.json").absolutePath

    try {
        val jsonString = File(filePath).readText(Charset.forName("UTF-8"))

        val json = Json { ignoreUnknownKeys = true }
        val itemList = json.decodeFromString<List<ItemBookInfo>>(jsonString)

        val todayJsonList = ArrayList<ItemBookInfo>()

        for (item in itemList) {
            todayJsonList.add(item)
        }

        callbacks.invoke(todayJsonList)
    } catch (e: Exception) {
        getBestList(platform = platform, type = type) {
            callbacks.invoke(it)
        }
    }
}

fun getBestListTodayStorage(
    context: Context,
    platform: String,
    type: String,
    callbacks: (ArrayList<ItemBookInfo>) -> Unit
) {

    try {
        val storage = Firebase.storage
        val storageRef = storage.reference
        val todayFileRef = storageRef.child("${platform}/${type}/DAY/${DBDate.dateMMDD()}.json")
        val localFile = File(context.filesDir, "${platform}_TODAY_${type}.json")

        todayFileRef.getFile(localFile).addOnSuccessListener {
            val jsonString = localFile.readText(Charset.forName("UTF-8"))
            val json = Json { ignoreUnknownKeys = true }
            val itemList = json.decodeFromString<List<ItemBookInfo>>(jsonString)

            val todayJsonList = ArrayList<ItemBookInfo>()

            for (item in itemList) {
                todayJsonList.add(item)
            }

            callbacks(todayJsonList)
        }.addOnFailureListener {
            getBestList(platform = platform, type = type) {
                callbacks.invoke(it)
            }
        }
    } catch (e: Exception) {
        getBestList(platform = platform, type = type) {
            callbacks.invoke(it)
        }
    }
}

private fun getBestList(
    platform: String,
    type: String,
    callbacks: (ArrayList<ItemBookInfo>) -> Unit
) {

    val mRootRef =
        FirebaseDatabase.getInstance().reference.child("BEST").child(type).child(platform)
            .child("DAY")

    mRootRef.addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {

                val bestList = ArrayList<ItemBookInfo>()

                for (book in dataSnapshot.children) {
                    val item: ItemBookInfo? =
                        dataSnapshot.child(book.key ?: "").getValue(ItemBookInfo::class.java)
                    if (item != null) {
                        bestList.add(item)
                    }
                }

                callbacks.invoke(bestList)

            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    })
}

fun getBookMap(
    platform: String,
    type: String,
    callbacks: (MutableMap<String, ItemBookInfo>) -> Unit
) {

    val mRootRef =
        FirebaseDatabase.getInstance().reference.child("BOOK").child(type).child(platform)

    mRootRef.addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {

                val itemMap = mutableMapOf<String, ItemBookInfo>()

                for (item in dataSnapshot.children) {

                    val book = item.getValue(ItemBookInfo::class.java)

                    if (book != null) {
                        itemMap[book.bookCode] = book
                    }
                }

                callbacks.invoke(itemMap)
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    })
}

fun getBookItemWeekTrophy(
    bookCode: String,
    platform: String,
    type: String,
    callbacks: (ArrayList<ItemBestInfo>) -> Unit
) {

    val weekArray = ArrayList<ItemBestInfo>()

    val mRootRef =
        FirebaseDatabase.getInstance().reference.child("BEST").child(type).child(platform)
            .child("TROPHY_WEEK").child(bookCode)

    mRootRef.addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {

                for (i in 0..6) {
                    weekArray.add(ItemBestInfo())
                }

                for (snapshot in dataSnapshot.children) {
                    val key = snapshot.key
                    val value = snapshot.value

                    if (key != null && value != null) {

                        val item = snapshot.getValue(ItemBestInfo::class.java)

                        if (item != null) {
                            weekArray[key.toInt()] = item
                        }
                    }
                }

                callbacks.invoke(weekArray)
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    })
}

fun getBestWeekListJson(
    context: Context,
    platform: String,
    type: String,
    callbacks: (ArrayList<ArrayList<ItemBookInfo>>) -> Unit
) {

    val filePath = File(context.filesDir, "${platform}_WEEK_${type}.json").absolutePath

    try {
        val jsonString = File(filePath).readText(Charset.forName("UTF-8"))

        val jsonArray = JSONArray(jsonString)

        val weekJsonList = ArrayList<ArrayList<ItemBookInfo>>()

        for (i in 0 until jsonArray.length()) {

            try {
                val jsonArrayItem = jsonArray.getJSONArray(i)
                val itemList = ArrayList<ItemBookInfo>()

                for (j in 0 until jsonArrayItem.length()) {

                    try {
                        val jsonObject = jsonArrayItem.getJSONObject(j)
                        itemList.add(convertItemBookJson(jsonObject))
                    } catch (e: Exception) {
                        itemList.add(ItemBookInfo())
                    }
                }

                weekJsonList.add(itemList)
            } catch (e: Exception) {
                weekJsonList.add(ArrayList())
            }
        }

        callbacks.invoke(weekJsonList)

    } catch (e: Exception) {
        getBestWeekListStorage(context = context, platform = platform, type = type) {
            callbacks.invoke(it)
        }
    }
}

fun getBestWeekListStorage(
    context: Context,
    platform: String,
    type: String,
    callbacks: (ArrayList<ArrayList<ItemBookInfo>>) -> Unit
) {

    val storage = Firebase.storage
    val storageRef = storage.reference
    val weekRef =
        storageRef.child("${platform}/${type}/WEEK/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json")
    val weekFile = File(context.filesDir, "${platform}_WEEK_${type}.json")

    weekRef.getFile(weekFile).addOnSuccessListener {
        val jsonString = weekFile.readText(Charset.forName("UTF-8"))

        val jsonArray = JSONArray(jsonString)

        val weekJsonList = ArrayList<ArrayList<ItemBookInfo>>()

        for (i in 0 until jsonArray.length()) {

            try {
                val jsonArrayItem = jsonArray.getJSONArray(i)
                val itemList = ArrayList<ItemBookInfo>()

                for (j in 0 until jsonArrayItem.length()) {

                    try {
                        val jsonObject = jsonArrayItem.getJSONObject(j)
                        itemList.add(convertItemBookJson(jsonObject))
                    } catch (e: Exception) {
                        itemList.add(ItemBookInfo())
                    }
                }

                weekJsonList.add(itemList)
            } catch (e: Exception) {
                weekJsonList.add(ArrayList())
            }
        }

        callbacks.invoke(weekJsonList)
    }
}

fun getBestWeekTrophy(
    platform: String,
    type: String,
    callbacks: (MutableMap<String, ItemBestInfo>) -> Unit
) {

    val storage = Firebase.storage
    val storageRef = storage.reference
    val weekTrophyRef =
        storageRef.child("${platform}/${type}/WEEK_TROPHY/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json")
    val weekTrophyFile = weekTrophyRef.getBytes(1024 * 1024)

    weekTrophyFile.addOnSuccessListener { bytes ->
        val jsonString = String(bytes, Charset.forName("UTF-8"))
        val json = Json { ignoreUnknownKeys = true }
        val itemList = json.decodeFromString<List<ItemBestInfo>>(jsonString)

        val cmpAsc: java.util.Comparator<ItemBestInfo> =
            Comparator { o1, o2 -> o2.total.compareTo(o1.total) }
        Collections.sort(itemList, cmpAsc)

        val weekJsonList = mutableMapOf<String, ItemBestInfo>()

        for (item in itemList) {
            weekJsonList[item.bookCode] = item
        }

        callbacks.invoke(weekJsonList)
    }.addOnFailureListener {
        Log.d("getBestWeekTrophy", "FAIL $it")
    }
}

fun getBestMonthTrophyJson(
    context: Context,
    platform: String,
    type: String,
    callbacks: (ArrayList<ArrayList<ItemBookInfo>>) -> Unit
) {

    val filePath = File(context.filesDir, "${platform}_MONTH_${type}.json").absolutePath

    try {
        val jsonString = File(filePath).readText(Charset.forName("UTF-8"))
        val jsonArray = JSONArray(jsonString)
        val monthJsonList = ArrayList<ArrayList<ItemBookInfo>>()

        for (i in 0 until jsonArray.length()) {

            try {
                val jsonArrayItem = jsonArray.getJSONArray(i)
                val itemList = ArrayList<ItemBookInfo>()

                for (j in 0 until jsonArrayItem.length()) {

                    try {
                        val jsonObject = jsonArrayItem.getJSONObject(j)
                        itemList.add(convertItemBookJson(jsonObject))
                    } catch (e: Exception) {
                        itemList.add(ItemBookInfo())
                    }
                }

                monthJsonList.add(itemList)
            } catch (e: Exception) {
                monthJsonList.add(ArrayList())
            }
        }

        callbacks.invoke(monthJsonList)

    } catch (e: Exception) {
        getBestMonthListStorage(context = context, platform = platform, type = type) {
            callbacks.invoke(it)
        }
    }
}

fun getBestMonthListStorage(
    context: Context,
    platform: String,
    type: String,
    callbacks: (ArrayList<ArrayList<ItemBookInfo>>) -> Unit
) {

    val storage = Firebase.storage
    val storageRef = storage.reference
    val monthRef =
        storageRef.child("${platform}/${type}/MONTH/${DBDate.year()}_${DBDate.month()}.json")
    val monthFile = File(context.filesDir, "${platform}_MONTH_${type}.json")

    monthRef.getFile(monthFile).addOnSuccessListener { bytes ->
        val jsonString = monthFile.readText(Charset.forName("UTF-8"))
        val jsonArray = JSONArray(jsonString)
        val monthJsonList = ArrayList<ArrayList<ItemBookInfo>>()

        for (i in 0 until jsonArray.length()) {

            try {
                val jsonArrayItem = jsonArray.getJSONArray(i)
                val itemList = ArrayList<ItemBookInfo>()

                for (j in 0 until jsonArrayItem.length()) {

                    try {
                        val jsonObject = jsonArrayItem.getJSONObject(j)
                        itemList.add(convertItemBookJson(jsonObject))
                    } catch (e: Exception) {
                        itemList.add(ItemBookInfo())
                    }
                }

                monthJsonList.add(itemList)
            } catch (e: Exception) {
                monthJsonList.add(ArrayList())
            }
        }

        callbacks.invoke(monthJsonList)
    }
}

fun getBestMonthTrophy(
    platform: String,
    type: String,
    callbacks: (MutableMap<String, ItemBestInfo>) -> Unit
) {

    val storage = Firebase.storage
    val storageRef = storage.reference
    val monthTrophyRef =
        storageRef.child("${platform}/${type}/MONTH_TROPHY/${DBDate.year()}_${DBDate.month()}.json")
    val monthTrophyFile = monthTrophyRef.getBytes(1024 * 1024)

    monthTrophyFile.addOnSuccessListener { bytes ->
        val jsonString = String(bytes, Charset.forName("UTF-8"))
        val json = Json { ignoreUnknownKeys = true }
        val itemList = json.decodeFromString<List<ItemBestInfo>>(jsonString)

        val monthJsonList = mutableMapOf<String, ItemBestInfo>()

        val cmpAsc: java.util.Comparator<ItemBestInfo> =
            Comparator { o1, o2 -> o2.total.compareTo(o1.total) }
        Collections.sort(itemList, cmpAsc)

        for (item in itemList) {
            monthJsonList[item.bookCode] = item
        }

        callbacks.invoke(monthJsonList)
    }
}

fun setBestWeekTrophyJSON(context: Context, platform: String, type: String) {

    val mRootRef =
        FirebaseDatabase.getInstance().reference.child("BEST").child(type).child(platform)
            .child("TROPHY_WEEK_TOTAL")

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

                val jsonString = Gson().toJson(itemList)

                val filePath = File(context.filesDir, "${platform}_WEEK_TROPHY_${type}.json")

                try {
                    FileWriter(filePath).use { writer ->
                        writer.write(jsonString)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()

                }
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    })
}

fun getBestWeekTrophyJSON(
    context: Context,
    platform: String,
    type: String,
    callbacks: (MutableMap<String, ItemBestInfo>) -> Unit
) {
    val filePath = File(context.filesDir, "${platform}_WEEK_TROPHY_${type}.json").absolutePath

    try {
        val jsonString = File(filePath).readText(Charset.forName("UTF-8"))

        val json = Json { ignoreUnknownKeys = true }
        val itemList = json.decodeFromString<List<ItemBestInfo>>(jsonString)

        val todayJsonMap = mutableMapOf<String, ItemBestInfo>()

        for (item in itemList) {
            todayJsonMap[item.bookCode] = item
        }

        callbacks.invoke(todayJsonMap)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun setBestMonthTrophyJSON(context: Context, platform: String, type: String) {

    val mRootRef =
        FirebaseDatabase.getInstance().reference.child("BEST").child(type).child(platform)
            .child("TROPHY_MONTH_TOTAL")

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

                val jsonString = Gson().toJson(itemList)

                val filePath = File(context.filesDir, "${platform}_WEEK_TROPHY_${type}.json")

                try {
                    FileWriter(filePath).use { writer ->
                        writer.write(jsonString)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()

                }
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    })
}

fun getBestMonthTrophyJSON(
    context: Context,
    platform: String,
    type: String,
    callbacks: (MutableMap<String, ItemBestInfo>) -> Unit
) {
    val filePath = File(context.filesDir, "${platform}_MONTH_TROPHY_${type}.json").absolutePath

    try {
        val jsonString = File(filePath).readText(Charset.forName("UTF-8"))

        val json = Json { ignoreUnknownKeys = true }
        val itemList = json.decodeFromString<List<ItemBestInfo>>(jsonString)

        val todayJsonMap = mutableMapOf<String, ItemBestInfo>()

        for (item in itemList) {
            todayJsonMap[item.bookCode] = item
        }

        callbacks.invoke(todayJsonMap)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

