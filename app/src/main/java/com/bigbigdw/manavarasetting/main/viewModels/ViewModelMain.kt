package com.bigbigdw.manavarasetting.main.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.bigbigdw.manavarasetting.firebase.FCMAlert
import com.bigbigdw.manavarasetting.main.event.EventMain
import com.bigbigdw.manavarasetting.main.event.StateMain
import com.bigbigdw.manavarasetting.main.event.UserInfo
import com.bigbigdw.manavarasetting.main.model.ItemBookInfo
import com.bigbigdw.manavarasetting.main.model.ItemBestInfo
import com.bigbigdw.manavarasetting.main.model.ItemGenre
import com.bigbigdw.manavarasetting.util.DBDate
import com.bigbigdw.manavarasetting.util.PeriodicWorker
import com.bigbigdw.manavarasetting.util.convertItemBestJson
import com.bigbigdw.manavarasetting.util.convertItemBookJson
import com.bigbigdw.manavarasetting.util.convertItemKeyword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONArray
import org.json.JSONObject
import java.nio.charset.Charset
import java.util.Collections
import javax.inject.Inject

class ViewModelMain @Inject constructor() : ViewModel() {

    private val events = Channel<EventMain>()

    val state: StateFlow<StateMain> = events.receiveAsFlow()
        .runningFold(StateMain(), ::reduceState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, StateMain())

    private val _sideEffects = Channel<String>()

    val sideEffects = _sideEffects.receiveAsFlow()

    private fun reduceState(current: StateMain, event: EventMain): StateMain {
        return when (event) {
            EventMain.Loaded -> {
                current.copy(Loaded = true)
            }

            is EventMain.SetFcmAlertList -> {
                current.copy(fcmAlertList = event.fcmAlertList)
            }

            is EventMain.SetFcmNoticeList -> {
                current.copy(fcmNoticeList = event.fcmNoticeList)
            }

            is EventMain.SetBestBookList -> {
                current.copy(
                    bestBookList = event.bestBookList
                )
            }

            is EventMain.SetFCMList -> {
                current.copy(
                    fcmBestList = event.fcmBestList,
                    fcmJsonList = event.fcmJsonList,
                    fcmTrophyList = event.fcmTrophyList,
                    fcmBestCount = event.fcmBestCount,
                    fcmJsonCount = event.fcmJsonCount,
                    fcmTrophyCount = event.fcmTrophyCount,
                )
            }

            is EventMain.SetBestBookWeekList -> {
                current.copy(
                    bestListWeek = event.bestListWeek
                )
            }

            is EventMain.SetTrophyList -> {
                current.copy(
                    trophyList = event.trophyList
                )
            }

            is EventMain.SetGenreDay -> {
                current.copy(
                    genreDay = event.genreDay
                )
            }

            is EventMain.SetGenreWeek -> {
                current.copy(
                    genreDay = event.genreDay,
                    genreDayList = event.genreDayList
                )
            }

            is EventMain.SetUserList -> {
                current.copy(
                    userList = event.userList,
                )
            }

            else -> {
                current.copy(Loaded = false)
            }
        }
    }


    fun getFCMList(child: String, activity: String = "") {

        val mRootRef = FirebaseDatabase.getInstance().reference.child("MESSAGE").child(child)

        mRootRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {

                    var fcmlist = ArrayList<FCMAlert>()

                    for (item in dataSnapshot.children) {
                        val fcm: FCMAlert? =
                            dataSnapshot.child(item.key ?: "").getValue(FCMAlert::class.java)

                        if (fcm != null) {

                            if (activity.isNullOrEmpty()) {
                                fcmlist.add(fcm)
                            } else {
                                if (fcm.activity == activity) {
                                    fcmlist.add(fcm)
                                }
                            }
                        }
                    }

                    viewModelScope.launch {

                        if (fcmlist.size > 1) {
                            fcmlist = fcmlist.reversed() as ArrayList<FCMAlert>
                        }

                        when (child) {
                            "ALERT" -> {
                                events.send(EventMain.SetFcmAlertList(fcmAlertList = fcmlist))
                            }
                            "NOTICE" -> {
                                events.send(EventMain.SetFcmNoticeList(fcmNoticeList = fcmlist))
                            }
                            else -> {
                                events.send(EventMain.SetFcmNoticeList(fcmNoticeList = fcmlist))
                            }
                        }
                    }

                } else {
                    Log.d("HIHI", "FALSE")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun getBestList(platform: String, type: String) {
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

                    viewModelScope.launch {
                        events.send(EventMain.SetBestBookList(bestBookList = bestList))
                    }

                } else {
                    Log.d("HIHI", "FALSE")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun getBestJsonList(platform: String, type: String) {
        val storage = Firebase.storage
        val storageRef = storage.reference
        val todayFileRef = storageRef.child("${platform}/${type}/DAY/${DBDate.dateMMDD()}.json")

        val todayFile = todayFileRef.getBytes(1024 * 1024)

        todayFile.addOnSuccessListener { bytes ->
            val jsonString = String(bytes, Charset.forName("UTF-8"))
            val json = Json { ignoreUnknownKeys = true }
            val itemList = json.decodeFromString<List<ItemBookInfo>>(jsonString)

            val todayJsonList = ArrayList<ItemBookInfo>()

            for (item in itemList) {
                todayJsonList.add(item)
            }

            viewModelScope.launch {
                events.send(EventMain.SetBestBookList(bestBookList = todayJsonList))
            }
        }
    }

    fun getBestJsonWeekList(platform: String, menu: String, type: String) {
        val storage = Firebase.storage
        val storageRef = storage.reference

        val fileRef: StorageReference = if (menu == "주간") {
            storageRef.child("${platform}/${type}/WEEK/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json")
        } else {
            storageRef.child("${platform}/${type}/MONTH/${DBDate.year()}_${DBDate.month()}.json")
        }

        val file = fileRef.getBytes(1024 * 1024)

        file.addOnSuccessListener { bytes ->
            val jsonString = String(bytes, Charset.forName("UTF-8"))

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

            viewModelScope.launch {
                events.send(EventMain.SetBestBookWeekList(bestListWeek = weekJsonList))
            }
        }
    }

    fun getBestJsonTrophyList(platform: String, menu: String, type: String) {
        val storage = Firebase.storage
        val storageRef = storage.reference

        val jsonArrayRef = if (menu == "주간") {
            storageRef.child("${platform}/${type}/WEEK_TROPHY/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json")
        } else {
            storageRef.child("${platform}/${type}/MONTH_TROPHY/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json")
        }

        val file = jsonArrayRef.getBytes(1024 * 1024)

        file.addOnSuccessListener { bytes ->
            val jsonString = String(bytes, Charset.forName("UTF-8"))

            val jsonArray = JSONArray(jsonString)

            val itemList = ArrayList<ItemBestInfo>()

            for (i in 0 until jsonArray.length()) {

                val jsonObject = jsonArray.getJSONObject(i)
                itemList.add(convertItemBestJson(jsonObject))
            }

            val cmpAsc: java.util.Comparator<ItemBestInfo> =
                Comparator { o1, o2 -> o1.totalCount.compareTo(o2.totalCount) }
            Collections.sort(itemList, cmpAsc)

            viewModelScope.launch {
                events.send(EventMain.SetTrophyList(trophyList = itemList))
            }
        }.addOnFailureListener {
            Log.d("JSON_TROPHY_$type", "FAIL == $it")
        }
    }

    fun getBestTrophyList(platform: String, menu: String, type: String) {

        val mRootRef = if (menu == "주간") {
            FirebaseDatabase.getInstance().reference.child("BEST").child(type).child(platform)
                .child("TROPHY_WEEK_TOTAL")
        } else {
            FirebaseDatabase.getInstance().reference.child("BEST").child(type).child(platform)
                .child("TROPHY_MONTH_TOTAL")
        }

        mRootRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {

                    val bestList = ArrayList<ItemBestInfo>()

                    for (book in dataSnapshot.children) {
                        val item: ItemBestInfo? =
                            dataSnapshot.child(book.key ?: "").getValue(ItemBestInfo::class.java)
                        if (item != null) {
                            bestList.add(item.copy(bookCode = book.key ?: ""))
                        }
                    }

                    viewModelScope.launch {
                        events.send(EventMain.SetTrophyList(trophyList = bestList))
                    }

                } else {
                    Log.d("JSON_TROPHY_$type", "FAIL == NOT EXIST")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun resetBest(str: String) {
        var currentUser: FirebaseUser? = null
        val auth: FirebaseAuth = Firebase.auth

        val mRootRef = FirebaseDatabase.getInstance().reference

        currentUser = auth.currentUser

        if (currentUser?.uid == "A8uh2QkVQaV3Q3rE8SgBNKzV6VH2") {

            if (str == "ALERT") {
                mRootRef.child("MESSAGE").child(str).removeValue()
            } else {
                mRootRef.child(str).removeValue()
            }

        }
    }

    fun checkWorker(workManager: WorkManager, tag: String, platform: String, type: String) {
        viewModelScope.launch {
            _sideEffects.send(
                PeriodicWorker.checkWorker(
                    workManager = workManager,
                    tag = tag,
                    platform = platform,
                    type = type
                )
            )
        }
    }

    fun getGenreDay(
        platform: String,
        type: String
    ) {

        val mRootRef =
            FirebaseDatabase.getInstance().reference.child("BEST").child(type).child(platform)
                .child("GENRE_DAY")

        mRootRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {

                    val arrayList = ArrayList<ItemGenre>()

                    for (snapshot in dataSnapshot.children) {
                        val key = snapshot.key
                        val value = snapshot.value

                        if (key != null && value != null) {

                            arrayList.add(
                                ItemGenre(
                                    title = key,
                                    value = value.toString()
                                )
                            )
                        }
                    }

                    val cmpAsc: java.util.Comparator<ItemGenre> =
                        Comparator { o1, o2 -> o2.value.toInt().compareTo(o1.value.toInt()) }
                    Collections.sort(arrayList, cmpAsc)

                    viewModelScope.launch {
                        events.send(EventMain.SetGenreDay(genreDay = arrayList))
                    }
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

        mRootRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){

                    val dataMap = HashMap<String, Any>()
                    val arrayList = ArrayList<ItemGenre>()

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

                                        dataMap[key] = preValue + currentValue
                                    } else {
                                        dataMap[key] = value
                                    }
                                }
                            }
                        }
                    }

                    for ((key, value) in dataMap) {
                        arrayList.add(
                            ItemGenre(
                                title = key,
                                value = value.toString()
                            )
                        )
                    }

                    val cmpAsc: java.util.Comparator<ItemGenre> =
                        Comparator { o1, o2 -> o2.value.toInt().compareTo(o1.value.toInt()) }
                    Collections.sort(arrayList, cmpAsc)

                    viewModelScope.launch {
                        events.send(EventMain.SetGenreDay(genreDay = arrayList))
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

        mRootRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){

                    val dataMap = HashMap<String, Any>()
                    val arrayList = ArrayList<ItemGenre>()

                    for (i in 1..31) {

                        val item = dataSnapshot.child(i.toString())

                        if (item.exists()) {

                            for (snapshot in item.children) {
                                val key = snapshot.key
                                val value = snapshot.value

                                if (key != null && value != null) {

                                    if(dataMap[key] != null){

                                        val preValue = dataMap[key] as Long
                                        val currentValue = value as Long

                                        dataMap[key] = preValue + currentValue
                                    } else {
                                        dataMap[key] = value
                                    }
                                }
                            }
                        }
                    }

                    for ((key, value) in dataMap) {
                        arrayList.add(
                            ItemGenre(
                                title = key,
                                value = value.toString()
                            )
                        )
                    }

                    val cmpAsc: java.util.Comparator<ItemGenre> =
                        Comparator { o1, o2 -> o2.value.toInt().compareTo(o1.value.toInt()) }
                    Collections.sort(arrayList, cmpAsc)

                    viewModelScope.launch {
                        events.send(EventMain.SetGenreDay(genreDay = arrayList))
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
            val itemList = json.decodeFromString<List<ItemGenre>>(jsonString)

            val jsonList = ArrayList<ItemGenre>()

            for (item in itemList) {
                jsonList.add(item)
            }

            val cmpAsc: java.util.Comparator<ItemGenre> =
                Comparator { o1, o2 -> o2.value.toInt().compareTo(o1.value.toInt()) }
            Collections.sort(jsonList, cmpAsc)

            viewModelScope.launch {
                events.send(EventMain.SetGenreDay(genreDay = jsonList))
            }
        }
    }

    fun getJsonGenreWeekList(platform: String, type: String) {
        val storage = Firebase.storage
        val storageRef = storage.reference

        val fileRef: StorageReference =  storageRef.child("${platform}/${type}/GENRE_WEEK/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json")

        val file = fileRef.getBytes(1024 * 1024)

        file.addOnSuccessListener { bytes ->
            val jsonString = String(bytes, Charset.forName("UTF-8"))

            val jsonArray = JSONArray(jsonString)
            val weekJsonList = ArrayList<ArrayList<ItemGenre>>()
            val sumList = ArrayList<ItemGenre>()

            val dataMap = HashMap<String, Long>()

            for (i in 0 until jsonArray.length()) {

                try {
                    val jsonArrayItem = jsonArray.getJSONArray(i)
                    val itemList = ArrayList<ItemGenre>()

                    for (j in 0 until jsonArrayItem.length()) {

                        try {
                            val jsonObject = jsonArrayItem.getJSONObject(j)
                            itemList.add(convertItemKeyword(jsonObject))
                            sumList.add(convertItemKeyword(jsonObject))
                        } catch (e: Exception) {
                            itemList.add(ItemGenre())
                        }
                    }

                    val cmpAsc: java.util.Comparator<ItemGenre> =
                        Comparator { o1, o2 -> o2.value.toInt().compareTo(o1.value.toInt()) }
                    Collections.sort(itemList, cmpAsc)

                    weekJsonList.add(itemList)
                } catch (e: Exception) {
                    weekJsonList.add(ArrayList())
                }
            }

            for(item in sumList){

                val key = item.title
                val value = item.value

                if(dataMap[key] != null){

                    val preValue = dataMap[key] as Long
                    val currentValue = value.toLong()

                    dataMap[key] = preValue + currentValue
                } else {
                    dataMap[key] = value.toLong()
                }
            }

            val arrayList = ArrayList<ItemGenre>()

            for ((key, value) in dataMap) {
                arrayList.add(
                    ItemGenre(
                        title = key,
                        value = value.toString()
                    )
                )
            }

            val cmpAsc: java.util.Comparator<ItemGenre> =
                Comparator { o1, o2 -> o2.value.toInt().compareTo(o1.value.toInt()) }
            Collections.sort(arrayList, cmpAsc)

            viewModelScope.launch {
                events.send(EventMain.SetGenreWeek(genreDayList = weekJsonList, genreDay = arrayList))
            }
        }
    }

    fun getJsonGenreMonthList(platform: String, type: String) {
        val storage = Firebase.storage
        val storageRef = storage.reference

        val fileRef: StorageReference = storageRef.child("${platform}/${type}/GENRE_MONTH/${DBDate.year()}_${DBDate.month()}.json")

        val file = fileRef.getBytes(1024 * 1024)

        file.addOnSuccessListener { bytes ->
            val jsonString = String(bytes, Charset.forName("UTF-8"))

            val jsonArray = JSONArray(jsonString)
            val weekJsonList = ArrayList<ArrayList<ItemGenre>>()
            val sumList = ArrayList<ItemGenre>()

            val dataMap = HashMap<String, Long>()

            for (i in 0 until jsonArray.length()) {

                try {
                    val jsonArrayItem = jsonArray.getJSONArray(i)
                    val itemList = ArrayList<ItemGenre>()

                    for (j in 0 until jsonArrayItem.length()) {

                        try {
                            val jsonObject = jsonArrayItem.getJSONObject(j)
                            itemList.add(convertItemKeyword(jsonObject))
                            sumList.add(convertItemKeyword(jsonObject))
                        } catch (e: Exception) {
                            itemList.add(ItemGenre())
                        }
                    }

                    val cmpAsc: java.util.Comparator<ItemGenre> =
                        Comparator { o1, o2 -> o2.value.toInt().compareTo(o1.value.toInt()) }
                    Collections.sort(itemList, cmpAsc)

                    weekJsonList.add(itemList)
                } catch (e: Exception) {
                    weekJsonList.add(ArrayList())
                }
            }

            for(item in sumList){

                val key = item.title
                val value = item.value

                if(dataMap[key] != null){

                    val preValue = dataMap[key] as Long
                    val currentValue = value.toLong()

                    dataMap[key] = preValue + currentValue
                } else {
                    dataMap[key] = value.toLong()
                }
            }

            val arrayList = ArrayList<ItemGenre>()

            for ((key, value) in dataMap) {
                arrayList.add(
                    ItemGenre(
                        title = key,
                        value = value.toString()
                    )
                )
            }

            val cmpAsc: java.util.Comparator<ItemGenre> =
                Comparator { o1, o2 -> o2.value.toInt().compareTo(o1.value.toInt()) }
            Collections.sort(arrayList, cmpAsc)

            viewModelScope.launch {
                events.send(EventMain.SetGenreWeek(genreDayList = weekJsonList, genreDay = arrayList))
            }
        }
    }

    fun getBook(platform: String, type: String){
        val storage = Firebase.storage
        val storageRef = storage.reference
        val book = storageRef.child("${platform}/${type}/BOOK/${platform}.json")

        book.getBytes(1024 * 1024)
            .addOnSuccessListener {bytes ->
                val jsonString = String(bytes, Charset.forName("UTF-8"))

                val arrayList = ArrayList<ItemBookInfo>()

                try {
                    val jsonObject = JSONObject(jsonString)

                    val keys = jsonObject.keys()
                    while (keys.hasNext()) {
                        val key = keys.next()
                        val value = jsonObject[key].toString()
                        arrayList.add(convertItemBookJson( JSONObject(value)))
                    }

                    viewModelScope.launch {
                        events.send(EventMain.SetBestBookList(bestBookList = arrayList))
                    }

                } catch (e: Exception) {

                }
            }
    }

    fun getUserList() {
        val mRootRef = FirebaseDatabase.getInstance().reference.child("USER")

        val userList = ArrayList<UserInfo>()

        mRootRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){

                    for(child in dataSnapshot.children){
                        val userInfo = child.child("USERINFO").getValue(UserInfo::class.java)

                        if (userInfo != null) {
                            userList.add(userInfo)
                        }
                    }

                    viewModelScope.launch {
                        events.send(EventMain.SetUserList(userList = userList))
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

}

