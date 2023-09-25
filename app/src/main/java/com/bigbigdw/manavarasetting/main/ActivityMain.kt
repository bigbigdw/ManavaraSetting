package com.bigbigdw.manavarasetting.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.work.WorkManager
import com.bigbigdw.manavarasetting.main.screen.BackOnPressed
import com.bigbigdw.manavarasetting.main.screen.ScreenMain
import com.bigbigdw.manavarasetting.main.viewModels.ViewModelMain

class ActivityMain : ComponentActivity() {

    private val viewModelMain: ViewModelMain by viewModels()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val workManager = WorkManager.getInstance(applicationContext)

        setContent {
            val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass

            ScreenMain(
                workManager = workManager,
                viewModelMain = viewModelMain,
                widthSizeClass = widthSizeClass
            )
            BackOnPressed()
        }
    }
}
