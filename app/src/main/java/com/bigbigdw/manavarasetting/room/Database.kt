package com.bigbigdw.manavarasetting.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RoomBookListDataBest::class], version = 4)
abstract class DBBest: RoomDatabase() {
    abstract fun bestDao(): DaoBest
}