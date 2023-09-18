package com.bigbigdw.manavarasetting.room

import androidx.room.Dao
import androidx.room.Query

@Dao
interface DaoBest {
    @Query("DELETE FROM RoomBookListDataBest")
    fun initAll()
}