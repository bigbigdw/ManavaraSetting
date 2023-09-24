package com.bigbigdw.manavarasetting.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.work.WorkManager
import com.bigbigdw.manavarasetting.main.screen.BackOnPressed
import com.bigbigdw.manavarasetting.main.screen.ScreenMain
import com.bigbigdw.manavarasetting.main.viewModels.ViewModelMain
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ActivityMain : ComponentActivity() {

    private val viewModelMain: ViewModelMain by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val workManager = WorkManager.getInstance(applicationContext)

        viewModelMain.sideEffects
            .onEach { Toast.makeText(this@ActivityMain, it, Toast.LENGTH_SHORT).show() }
            .launchIn(lifecycleScope)

        setContent {
            ScreenMain(workManager = workManager, viewModelMain = viewModelMain)
            BackOnPressed()
        }
    }
}