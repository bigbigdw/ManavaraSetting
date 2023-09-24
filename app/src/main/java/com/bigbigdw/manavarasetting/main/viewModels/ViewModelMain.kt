package com.bigbigdw.manavarasetting.main.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigbigdw.manavarasetting.main.event.EventMain
import com.bigbigdw.manavarasetting.main.event.StateMain
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

            is EventMain.GetDataStore -> {
                current.copy(
                    timeTest = event.timeTest,
                    bestTest = event.bestTest,
                    jsonTest = event.jsonTest,
                    trophyTest = event.trophyTest
                )
            }
        }
    }

    fun getDataStoreStatus(context : Context){
        val mRootRef = FirebaseDatabase.getInstance().reference.child("WORKER")

        mRootRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){

                    val dataStore = DataStoreManager(context)

                    val TESTTIME: String? = dataSnapshot.child("WORKER_TEST").getValue(String::class.java)
                    val BESTWORKERTIME: String? = dataSnapshot.child("WORKER_BEST").getValue(String::class.java)
                    val JSONWORKERTIME: String? = dataSnapshot.child("WORKER_JSON").getValue(String::class.java)
                    val TROPHYWORKERTIME: String? = dataSnapshot.child("WORKER_TROPHY").getValue(String::class.java)

                    viewModelScope.launch {
                        dataStore.setDataStoreString(DataStoreManager.TEST_TIME, TESTTIME ?: "")
                        dataStore.setDataStoreString(DataStoreManager.BESTWORKER_TIME, BESTWORKERTIME ?: "")
                        dataStore.setDataStoreString(DataStoreManager.JSONWORKER_TIME, JSONWORKERTIME ?: "")
                        dataStore.setDataStoreString(DataStoreManager.TROPHYWORKER_TIME, TROPHYWORKERTIME ?: "")

                        events.send(
                            EventMain.GetDataStore(
                                timeTest = TESTTIME ?: "",
                                bestTest = BESTWORKERTIME ?: "",
                                jsonTest = JSONWORKERTIME ?: "",
                                trophyTest = TROPHYWORKERTIME ?: ""
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
}