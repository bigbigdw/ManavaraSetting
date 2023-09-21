package com.bigbigdw.manavarasetting.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.work.WorkManager
import com.bigbigdw.manavarasetting.main.screen.ScreenMain

class ActivityMain : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val workManager = WorkManager.getInstance(applicationContext)

        setContent {
            ScreenMain(
                workManager = workManager
            )
        }
    }
}