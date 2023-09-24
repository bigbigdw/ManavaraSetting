package com.bigbigdw.manavarasetting.main.event

import com.google.firebase.storage.StorageReference

sealed interface EventMain{
    object Loaded: EventMain
    class GetDataStore(val timeTest : String, val bestTest : String, val jsonTest : String, val trophyTest : String): EventMain
}

data class StateMain(
    val Loaded: Boolean = false,
    val timeTest: String = "",
    val bestTest: String = "",
    val jsonTest: String = "",
    val trophyTest: String = "",
)