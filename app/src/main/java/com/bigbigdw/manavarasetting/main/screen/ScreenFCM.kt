package com.bigbigdw.manavarasetting.main.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.work.WorkManager
import com.bigbigdw.manavarasetting.R
import com.bigbigdw.manavarasetting.firebase.DataFCMBodyNotification
import com.bigbigdw.manavarasetting.main.viewModels.DataStoreManager
import com.bigbigdw.manavarasetting.main.viewModels.ViewModelMain
import com.bigbigdw.manavarasetting.ui.theme.color000000
import com.bigbigdw.manavarasetting.ui.theme.color898989
import com.bigbigdw.manavarasetting.ui.theme.colorf7f7f7
import com.bigbigdw.manavarasetting.util.FCM
import com.bigbigdw.manavarasetting.util.PeriodicWorker
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenMainFCM(workManager: WorkManager, viewModelMain: ViewModelMain, isExpandedScreen: Boolean) {
    val context = LocalContext.current

    val dataStore = DataStoreManager(context)
    val (getFCM, setFCM) = remember { mutableStateOf(DataFCMBodyNotification()) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorf7f7f7)
                .verticalScroll(rememberScrollState())
                .semantics { contentDescription = "Overview Screen" },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            MainHeader(image = R.drawable.icon_fcm, title = "FCM 현황")

            ItemMainSetting(
                image = R.drawable.icon_fcm_gr,
                titleWorker = "테스트 WORKER : ",
                valueWorker = viewModelMain.state.collectAsState().value.statusTest,
                statusTitle = "테스트 갱신시간 : ",
                valueStatus = viewModelMain.state.collectAsState().value.timeTest,
            )

            ItemMainSetting(
                image = R.drawable.icon_fcm_gr,
                titleWorker = "테스트 호출 횟수 : ",
                valueWorker = viewModelMain.state.collectAsState().value.countTest,
                statusTitle = "테스트 금일 호출 횟수 : ",
                valueStatus = viewModelMain.state.collectAsState().value.countTodayTest,
            )

            ItemMainSettingSingle(
                image = R.drawable.icon_fcm_gr,
                titleWorker = "테스트 호출 주기 : ",
                valueWorker = viewModelMain.state.collectAsState().value.timeMillTest,
            )

            TextField(
                value = getFCM.title,
                onValueChange = {
                    setFCM(getFCM.copy(title = it))
                },
                label = { Text("푸시 알림 제목", color = color898989) },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0),
                    textColor = color000000
                ),
                modifier = Modifier.width(260.dp)
            )

            Spacer(modifier = Modifier.size(16.dp))

            TextField(
                value = getFCM.body,
                onValueChange = {
                    setFCM(getFCM.copy(body = it))
                },
                label = { Text("푸시 알림 내용", color = color898989) },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0),
                    textColor = color000000
                ),
                modifier = Modifier.width(260.dp)
            )

            BtnMobile(func = { FCM.postFCMAlert(context = context, getFCM = getFCM) }, btnText = "공지사항 등록")

            Spacer(modifier = Modifier.size(16.dp))

            TextField(
                value = dataStore.getDataStoreString(DataStoreManager.FCM_TOKEN).collectAsState(initial = "").value ?: "",
                onValueChange = {
                    setFCM(getFCM.copy(title = it))
                },
                label = { Text("FCM 토큰", color = color898989) },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0),
                    textColor = color000000
                ),
                modifier = Modifier.width(260.dp)
            )

            BtnMobile(func = { FCM.getFCMToken(context) }, btnText = "FCM 토큰 얻기")

            BtnMobile(func = { FCM.postFCMAlertTest(context = context, message = "테스트") }, btnText = "푸시 알림 테스트")

            BtnMobile(
                func = {
                    PeriodicWorker.doWorker(
                        workManager = workManager,
                        repeatInterval = 15,
                        tag = "TEST",
                        timeMill = TimeUnit.MINUTES
                    )
                },
                btnText = "테스트 WORKER 시작"
            )

            BtnMobile(
                func = {
                    PeriodicWorker.cancelWorker(
                        workManager = workManager,
                        tag = "TEST"
                    )
                },
                btnText = "테스트 WORKER 취소"
            )

            BtnMobile(
                func = {
                    PeriodicWorker.checkWorker(
                        workManager = workManager,
                        tag = "TEST"
                    )
                },
                btnText = "테스트 WORKER 확인"
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
        }
    }
}