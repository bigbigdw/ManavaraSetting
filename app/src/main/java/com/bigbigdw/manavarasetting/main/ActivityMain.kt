package com.bigbigdw.manavarasetting.main

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.lifecycle.lifecycleScope
import com.bigbigdw.manavarasetting.firebase.FirebaseWorkManager
import com.bigbigdw.manavarasetting.main.screen.ScreenMain
import com.bigbigdw.manavarasetting.main.viewModels.ViewModelMain
import com.bigbigdw.manavarasetting.util.DBDate
import com.bigbigdw.manavarasetting.util.findIndexInNovelList
import com.bigbigdw.manavarasetting.util.getBestListTodayStorage
import com.bigbigdw.manavarasetting.util.getBestMonthListStorage
import com.bigbigdw.manavarasetting.util.getBestWeekListStorage
import com.bigbigdw.manavarasetting.util.setBestMonthTrophyJSON
import com.bigbigdw.manavarasetting.util.setBestWeekTrophyJSON
import com.bigbigdw.manavarasetting.util.novelListEng
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ActivityMain : ComponentActivity() {

    private val viewModelMain: ViewModelMain by viewModels()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("HIHI", DBDate.dateMMDD());

        Log.d("ACTIVITYMAIN","${findIndexInNovelList("TOKSODA_FREE")}")
        Log.d("ACTIVITYMAIN","${novelListEng().size}")

        val pm = getSystemService(POWER_SERVICE) as PowerManager
        val packageName = packageName

        if (pm.isIgnoringBatteryOptimizations(packageName)) {
            Log.d("ACTIVITYMAIN","pm.isIgnoringBatteryOptimizations(packageName)")
        } else {
            val intent = Intent()
            intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
            intent.data = Uri.parse("package:$packageName")
            startActivityForResult(intent, 0)
        }

        viewModelMain.sideEffects
            .onEach { Toast.makeText(this@ActivityMain, it, Toast.LENGTH_SHORT).show() }
            .launchIn(lifecycleScope)

        setContent {
            val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass

            ScreenMain(
                viewModelMain = viewModelMain,
                widthSizeClass = widthSizeClass
            )


        }
    }
}
