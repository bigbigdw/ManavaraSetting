package com.bigbigdw.manavarasetting.main.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.bigbigdw.manavarasetting.firebase.FCMAlert
import com.bigbigdw.manavarasetting.main.event.EventMain
import com.bigbigdw.manavarasetting.main.event.StateMain
import com.bigbigdw.manavarasetting.main.model.ItemBookInfo
import com.bigbigdw.manavarasetting.main.model.ItemBestInfo
import com.bigbigdw.manavarasetting.util.DBDate
import com.bigbigdw.manavarasetting.util.DataStoreManager
import com.bigbigdw.manavarasetting.util.PeriodicWorker
import com.bigbigdw.manavarasetting.util.convertBestItemDataAnalyzeJson
import com.bigbigdw.manavarasetting.util.convertBestItemDataJson
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
        return when(event){
            EventMain.Loaded -> {
                current.copy(Loaded = true)
            }

            is EventMain.SetFcmAlertList -> {
                current.copy(fcmAlertList = event.fcmAlertList,)
            }

            is EventMain.SetFcmNoticeList -> {
                current.copy(fcmNoticeList = event.fcmNoticeList,)
            }

            is EventMain.SetBestBookList -> {
                current.copy(
                    setBestBookList = event.setBestBookList
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

            else -> {
                current.copy(Loaded = false)
            }
        }
    }

    fun getDataStoreStatus(context: Context){
        val mRootRef = FirebaseDatabase.getInstance().reference.child("WORKER")

        mRootRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){

                    val dataStore = DataStoreManager(context)

                    viewModelScope.launch {
                        dataStore.setDataStoreString(DataStoreManager.BEST_NAVER_SERIES_COMIC, dataSnapshot.child("BEST_NAVER_SERIES_COMIC").getValue(String::class.java) ?: "")
                        dataStore.setDataStoreString(DataStoreManager.BEST_NAVER_SERIES_NOVEL, dataSnapshot.child("BEST_NAVER_SERIES_NOVEL").getValue(String::class.java) ?: "")

                        dataStore.setDataStoreString(DataStoreManager.JSON_NAVER_SERIES_COMIC, dataSnapshot.child("JSON_NAVER_SERIES_COMIC").getValue(String::class.java) ?: "")
                        dataStore.setDataStoreString(DataStoreManager.JSON_NAVER_SERIES_NOVEL, dataSnapshot.child("JSON_NAVER_SERIES_NOVEL").getValue(String::class.java) ?: "")

                        dataStore.setDataStoreString(DataStoreManager.TROPHY_NAVER_SERIES_COMIC, dataSnapshot.child("TROPHY_NAVER_SERIES_COMIC").getValue(String::class.java) ?: "")
                        dataStore.setDataStoreString(DataStoreManager.TROPHY_NAVER_SERIES_NOVEL, dataSnapshot.child("TROPHY_NAVER_SERIES_NOVEL").getValue(String::class.java) ?: "")

                    }

                } else {
                    Log.d("HIHI", "FALSE")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun getDataStoreFCMCount() {
        val mRootRef = FirebaseDatabase.getInstance().reference.child("MESSAGE").child("ALERT")

        var fcmBestList = ArrayList<FCMAlert>()
        var fcmJsonList = ArrayList<FCMAlert>()
        var fcmTrophyList = ArrayList<FCMAlert>()

        var fcmBestCount : Int = 0
        var fcmJsonCount : Int = 0
        var fcmTrophyCount : Int = 0

        val year = DBDate.dateMMDDHHMM().substring(0,4)
        val month = DBDate.dateMMDDHHMM().substring(4,6)
        val day = DBDate.dateMMDDHHMM().substring(6,8)

        mRootRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){

                    for(item in dataSnapshot.children){
                        val fcm: FCMAlert? = dataSnapshot.child(item.key ?: "").getValue(FCMAlert::class.java)

                        if (fcm?.activity?.contains("BEST") == true) {
                            fcmBestList.add(fcm)

                            if(fcm.body.contains("${year}.${month}.${day}")){
                                fcmBestCount += 1
                            }

                        } else if (fcm?.activity?.contains("JSON") == true) {
                            fcmJsonList.add(fcm)

                            if(fcm.body.contains("${year}.${month}.${day}")){
                                fcmJsonCount += 1
                            }

                        } else if (fcm?.activity?.contains("TROPHY") == true) {
                            fcmTrophyList.add(fcm)

                            if(fcm.body.contains("${year}.${month}.${day}")){
                                fcmTrophyCount += 1
                            }

                        } else {
                            Log.d("HIHIHIHI", "item = $item")
                        }

                    }

                    if(fcmBestList.size > 1){
                        fcmBestList = fcmBestList.reversed() as ArrayList<FCMAlert>
                    }

                    if(fcmJsonList.size > 1){
                        fcmJsonList = fcmJsonList.reversed() as ArrayList<FCMAlert>
                    }

                    if(fcmTrophyList.size > 1){
                        fcmTrophyList = fcmTrophyList.reversed() as ArrayList<FCMAlert>
                    }

                    viewModelScope.launch {

                        events.send(
                            EventMain.SetFCMList(
                                fcmBestList = fcmBestList,
                                fcmJsonList = fcmJsonList,
                                fcmTrophyList = fcmTrophyList,
                                fcmBestCount = fcmBestCount,
                                fcmJsonCount = fcmJsonCount,
                                fcmTrophyCount = fcmTrophyCount,
                            )
                        )

//                        _sideEffects.send("FCM 카운트 갱신이 완료되었습니다")
                    }

                } else {
                    Log.d("HIHI", "FALSE")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun getFCMList(child: String){

        val mRootRef = FirebaseDatabase.getInstance().reference.child("MESSAGE").child(child)

        mRootRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){

                    var fcmlist = ArrayList<FCMAlert>()

                    for(item in dataSnapshot.children){
                        val fcm: FCMAlert? = dataSnapshot.child(item.key ?: "").getValue(FCMAlert::class.java)
                        if (fcm != null) {
                            fcmlist.add(fcm)
                        }
                    }

                    viewModelScope.launch {

                        if(fcmlist.size > 1){
                            fcmlist = fcmlist.reversed() as ArrayList<FCMAlert>
                        }

                        if(child == "ALERT"){
                            events.send(EventMain.SetFcmAlertList(fcmAlertList = fcmlist))
                        } else if (child == "NOTICE"){
                            events.send(EventMain.SetFcmNoticeList(fcmNoticeList = fcmlist))
                        }
                    }

                } else {
                    Log.d("HIHI", "FALSE")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun getBestList(platform: String, child: String, type: String){
        val mRootRef = FirebaseDatabase.getInstance().reference.child("BEST").child(type).child(platform).child(child).child("DAY")

        mRootRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){

                    val bestList = ArrayList<ItemBookInfo>()

                    for(book in dataSnapshot.children){
                        val item: ItemBookInfo? = dataSnapshot.child(book.key ?: "").getValue(ItemBookInfo::class.java)
                        if (item != null) {
                            bestList.add(item)
                        }
                    }

                    viewModelScope.launch {
                        events.send(EventMain.SetBestBookList(setBestBookList = bestList))
                    }

                } else {
                    Log.d("HIHI", "FALSE")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun getBestJsonList(platform : String, genre : String, type: String){
        val storage = Firebase.storage
        val storageRef = storage.reference
        val todayFileRef = storageRef.child("${platform}/${type}/${genre}/DAY/${DBDate.dateMMDD()}.json")

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
                events.send(EventMain.SetBestBookList(setBestBookList = todayJsonList))
            }
        }
    }

    fun getBestJsonWeekList(platform: String, genre: String, menu : String, type : String){
        val storage = Firebase.storage
        val storageRef = storage.reference

        val fileRef: StorageReference = if(menu == "주간"){
            storageRef.child("${platform}/${type}/${genre}/WEEK/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json")
        } else {
            storageRef.child("${platform}/${type}/${genre}/MONTH/${DBDate.year()}_${DBDate.month()}.json")
        }

        val file = fileRef.getBytes(1024 * 1024)

        file.addOnSuccessListener { bytes ->
            val jsonString = String(bytes, Charset.forName("UTF-8"))

            val jsonArray = JSONArray(jsonString)

            val weekJsonList = ArrayList<ArrayList<ItemBookInfo>>()

            for (i in 0 until jsonArray.length()) {

                try{
                    val jsonArrayItem = jsonArray.getJSONArray(i)
                    val itemList = ArrayList<ItemBookInfo>()

                    for (j in 0 until jsonArrayItem.length()) {

                        try{
                            val jsonObject = jsonArrayItem.getJSONObject(j)
                            itemList.add(convertBestItemDataJson(jsonObject))
                        }catch (e : Exception){
                            itemList.add(ItemBookInfo())
                        }
                    }

                    weekJsonList.add(itemList)
                } catch (e : Exception){
                    weekJsonList.add(ArrayList())
                }
            }

            viewModelScope.launch {
                events.send(EventMain.SetBestBookWeekList(bestListWeek = weekJsonList))
            }
        }
    }

    fun getBestJsonTrophyList(platform: String, genre: String, menu : String, type: String){
        val storage = Firebase.storage
        val storageRef = storage.reference

        val jsonArrayRef = if(menu == "주간"){
            storageRef.child("${platform}/${type}/${genre}/WEEK_TROPHY/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json")
        } else {
            storageRef.child("${platform}/${type}/${genre}/MONTH_TROPHY/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json")
        }

        val file = jsonArrayRef.getBytes(1024 * 1024)

        file.addOnSuccessListener { bytes ->
            val jsonString = String(bytes, Charset.forName("UTF-8"))

            val jsonArray = JSONArray(jsonString)

            val itemList = ArrayList<ItemBestInfo>()

            for (i in 0 until jsonArray.length()) {

                val jsonObject = jsonArray.getJSONObject(i)
                itemList.add(convertBestItemDataAnalyzeJson(jsonObject))
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

    fun getBestTrophyList(platform: String, menu: String, type: String, genre: String){

        val mRootRef = if(menu == "주간"){
            FirebaseDatabase.getInstance().reference.child("BEST").child(type).child(platform).child(genre).child("TROPHY_WEEK_TOTAL")
        } else {
            FirebaseDatabase.getInstance().reference.child("BEST").child(type).child(platform).child(genre).child("TROPHY_MONTH_TOTAL")
        }

        Log.d("JSON_TROPHY_$type", "mRootRef == $mRootRef")

        mRootRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){

                    val bestList = ArrayList<ItemBestInfo>()

                    for(book in dataSnapshot.children){
                        val item: ItemBestInfo? = dataSnapshot.child(book.key ?: "").getValue(ItemBestInfo::class.java)
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

    fun resetBest(str : String){
        var currentUser :  FirebaseUser? = null
        val auth: FirebaseAuth = Firebase.auth

        val mRootRef = FirebaseDatabase.getInstance().reference

        currentUser = auth.currentUser

        if(currentUser?.uid == "A8uh2QkVQaV3Q3rE8SgBNKzV6VH2"){

            if(str == "ALERT"){
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

}

