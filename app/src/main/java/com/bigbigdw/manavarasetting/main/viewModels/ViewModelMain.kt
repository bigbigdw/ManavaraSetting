package com.bigbigdw.manavarasetting.main.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.bigbigdw.manavarasetting.firebase.FCMAlert
import com.bigbigdw.manavarasetting.main.event.EventMain
import com.bigbigdw.manavarasetting.main.event.StateMain
import com.bigbigdw.manavarasetting.util.DBDate
import com.bigbigdw.manavarasetting.util.PeriodicWorker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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

            is EventMain.GetDataStoreWorker -> {
                current.copy(
                    timeTest = event.timeTest,
                    timeBest = event.timeBest,
                    timeJson = event.timeJson,
                    timeTrophy = event.timeTrophy,
                    statusTest = event.statusTest,
                    statusBest = event.statusBet,
                    statusJson = event.statusJson,
                    statusTrophy = event.statusTrophy,
                    timeMillTest = event.timeMillTest,
                    timeMillBest = event.timeMillBest,
                    timeMillJson = event.timeMillJson,
                    timeMillTrophy = event.timeMillTrophy,
                )
            }

            is EventMain.GetDataStoreFCM -> {
                current.copy(
                    countTest = event.countTest,
                    countTodayTest = event.countTodayTest,
                    countBest = event.countBest,
                    countTodayBest = event.countTodayBest,
                    countJson = event.countJson,
                    countTodayJson = event.countTodayJson,
                    countTrophy = event.countTrophy,
                    countTodayTrophy = event.countTodayTrophy,
                )
            }

            else -> {
                current.copy(Loaded = false)
            }
        }
    }

    fun getDataStoreStatus(context : Context, workManager: WorkManager){
        val mRootRef = FirebaseDatabase.getInstance().reference.child("WORKER")

        mRootRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){

                    val dataStore = DataStoreManager(context)

                    val workerTest: String? = dataSnapshot.child("WORKER_TEST").getValue(String::class.java)
                    val workerBest: String? = dataSnapshot.child("WORKER_BEST").getValue(String::class.java)
                    val workerJson: String? = dataSnapshot.child("WORKER_JSON").getValue(String::class.java)
                    val workerTrophy: String? = dataSnapshot.child("WORKER_TROPHY").getValue(String::class.java)

                    val timeMillTest: String? = dataSnapshot.child("TIMEMILL_TEST").getValue(String::class.java)
                    val timeMillBest: String? = dataSnapshot.child("TIMEMILL_BEST").getValue(String::class.java)
                    val timeMillJson: String? = dataSnapshot.child("TIMEMILL_BEST_JSON").getValue(String::class.java)
                    val timeMillTrophy: String? = dataSnapshot.child("TIMEMILL_BEST_TROPHY").getValue(String::class.java)

                    viewModelScope.launch {
                        dataStore.setDataStoreString(DataStoreManager.TEST_TIME, workerTest ?: "")
                        dataStore.setDataStoreString(DataStoreManager.BESTWORKER_TIME, workerBest ?: "")
                        dataStore.setDataStoreString(DataStoreManager.JSONWORKER_TIME, workerJson ?: "")
                        dataStore.setDataStoreString(DataStoreManager.TROPHYWORKER_TIME, workerTrophy ?: "")

                        _sideEffects.send("Worker 최신화가 완료되었습니다")

                        events.send(
                            EventMain.GetDataStoreWorker(
                                timeTest = workerTest ?: "",
                                timeBest = workerBest ?: "",
                                timeJson = workerJson ?: "",
                                timeTrophy = workerTrophy ?: "",
                                statusTest = PeriodicWorker.checkWorker(workManager = workManager, tag = "TEST"),
                                statusBet = PeriodicWorker.checkWorker(workManager = workManager, tag = "BEST"),
                                statusJson = PeriodicWorker.checkWorker(workManager = workManager, tag = "BEST_JSON"),
                                statusTrophy = PeriodicWorker.checkWorker(workManager = workManager, tag = "BEST_TROPHY"),
                                timeMillTest = timeMillTest ?: "",
                                timeMillBest = timeMillBest ?: "",
                                timeMillJson = timeMillJson ?: "",
                                timeMillTrophy = timeMillTrophy ?: "",
                            )
                        )

                    }

                } else {
                    Log.d("HIHI", "FALSE")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun getDataStoreFCMCount(context: Context){
        val mRootRef = FirebaseDatabase.getInstance().reference.child("MESSAGE").child("ALERT")

        val year = DBDate.dateMMDDHHMM().substring(0,4)
        val month = DBDate.dateMMDDHHMM().substring(4,6)
        val day = DBDate.dateMMDDHHMM().substring(6,8)

        var numFcm = 0
        var numFcmToday = 0
        var numBest = 0
        var numBestToday = 0
        var numJson = 0
        var numJsonToday = 0
        var numTrophy = 0
        var numTrophyToday = 0

        val dataStore = DataStoreManager(context)

        mRootRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){

                    for(item in dataSnapshot.children){
                        val fcm: FCMAlert? = dataSnapshot.child(item.key ?: "").getValue(FCMAlert::class.java)

                        if(fcm?.body?.contains("테스트") == true){
                            numFcm += 1

                            if(fcm.body.contains("${year}.${month}.${day}")){
                                numFcmToday += 1
                            }
                        } else if (fcm?.body?.contains("베스트 리스트가 갱신되었습니다") == true) {
                            numBest += 1

                            if (fcm.body.contains("${year}.${month}.${day}")) {
                                numBestToday += 1
                            }
                        } else if (fcm?.body?.contains("DAY JSON 생성이 완료되었습니다") == true) {
                            numJson += 1

                            if (fcm.body.contains("${year}.${month}.${day}")) {
                                numJsonToday += 1
                            }
                        } else if (fcm?.body?.contains("트로피 정산이 완료되었습니다") == true) {
                            numTrophy += 1

                            if (fcm.body.contains("${year}.${month}.${day}")) {
                                numTrophyToday += 1
                            }
                        } else {
                            Log.d("HIHIHIHI", "item = $item")
                        }

                    }

                    viewModelScope.launch {
                        dataStore.setDataStoreString(DataStoreManager.FCM_COUNT_TEST, numFcm.toString())
                        dataStore.setDataStoreString(DataStoreManager.FCM_COUNT_TEST_TODAY, numFcmToday.toString())
                        dataStore.setDataStoreString(DataStoreManager.FCM_COUNT_BEST, numBest.toString())
                        dataStore.setDataStoreString(DataStoreManager.FCM_COUNT_BEST_TODAY, numBestToday.toString())
                        dataStore.setDataStoreString(DataStoreManager.FCM_COUNT_JSON, numJson.toString())
                        dataStore.setDataStoreString(DataStoreManager.FCM_COUNT_JSON_TODAY, numJsonToday.toString())
                        dataStore.setDataStoreString(DataStoreManager.FCM_COUNT_TROPHY, numTrophy.toString())
                        dataStore.setDataStoreString(DataStoreManager.FCM_COUNT_TROPHY_TODAY, numTrophyToday.toString())

                        events.send(
                            EventMain.GetDataStoreFCM(
                                countTest = numFcm.toString(),
                                countTodayTest = numFcmToday.toString(),
                                countBest = numBest.toString(),
                                countTodayBest = numBestToday.toString(),
                                countJson = numJson.toString(),
                                countTodayJson = numJsonToday.toString(),
                                countTrophy = numTrophy.toString(),
                                countTodayTrophy = numTrophyToday.toString(),
                            )
                        )

                        _sideEffects.send("FCM 카운트 갱신이 완료되었습니다")
                    }

                } else {
                    Log.d("HIHI", "FALSE")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun test(){

    }
}