package com.bigbigdw.manavarasetting.main.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.sp
import androidx.work.WorkManager
import com.bigbigdw.manavarasetting.R
import com.bigbigdw.manavarasetting.firebase.DataFCMBodyNotification
import com.bigbigdw.manavarasetting.main.model.MainSettingLine
import com.bigbigdw.manavarasetting.util.DataStoreManager
import com.bigbigdw.manavarasetting.main.viewModels.ViewModelMain
import com.bigbigdw.manavarasetting.ui.theme.color000000
import com.bigbigdw.manavarasetting.ui.theme.color898989
import com.bigbigdw.manavarasetting.ui.theme.color8E8E8E
import com.bigbigdw.manavarasetting.ui.theme.colorF6F6F6
import com.bigbigdw.manavarasetting.util.FCM
import com.bigbigdw.manavarasetting.util.PeriodicWorker

@Composable
fun ScreenMainFCM() {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorF6F6F6)
                .padding(16.dp, 0.dp)
                .verticalScroll(rememberScrollState())
                .semantics { contentDescription = "Overview Screen" },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            )

            MainHeader(image = R.drawable.icon_fcm, title = "FCM 현황")

            ContentsFCM()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentsFCM() {

    val context = LocalContext.current
    val dataStore = DataStoreManager(context)
    val workManager = WorkManager.getInstance(context)
    val (getFCM, setFCM) = remember { mutableStateOf(DataFCMBodyNotification()) }

    val itemFcmWorker = listOf(
        MainSettingLine(title = "WORKER 시작", onClick = {
            PeriodicWorker.doWorker(
                workManager = workManager,
                time = 15,
                tag = "TEST",
                platform = "",
                type = ""
            )
        }),
        MainSettingLine(title = "WORKER 취소", onClick = {
            PeriodicWorker.cancelWorker(
                workManager = workManager,
                tag = "TEST",
                platform = "",
                type = ""
            )
        })
    )

    Button(
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        onClick = { FCM.getFCMToken(context = context) },
        modifier = Modifier
            .padding(8.dp, 0.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        content = {

            Column {

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "FCM 토큰",
                    color = color000000,
                    fontSize = 18.sp,
                )

                if(dataStore.getDataStoreString(DataStoreManager.FCM_TOKEN).collectAsState(initial = "").value?.isNotBlank() == true){
                    Spacer(modifier = Modifier.size(4.dp))

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = dataStore.getDataStoreString(DataStoreManager.FCM_TOKEN).collectAsState(initial = "").value ?: "",
                        color = color8E8E8E,
                        fontSize = 16.sp,
                    )

                    Spacer(modifier = Modifier.size(16.dp))
                }
            }
        }
    )

    Spacer(modifier = Modifier.size(32.dp))

    Button(
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        onClick = {
            FCM.postFCMAlertTest(context = context, message = "테스트")
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp),
        shape = RoundedCornerShape(50.dp),
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "푸시 알림 테스트",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    ItemTabletTitle(str = "FCM ALERT 등록")

    TabletContentWrap {
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
            modifier = Modifier.fillMaxWidth()
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
            modifier = Modifier.fillMaxWidth()
        )

        Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
            BtnMobile(
                func = { FCM.postFCMAlert(context = context, getFCM = getFCM) },
                btnText = "공지사항 등록"
            )
        }
    }

    ItemTabletTitle(str = "FCM 매니저")

    TabletContentWrap {
        itemFcmWorker.forEachIndexed { index, item ->
            ItemMainTabletContent(
                title = item.title,
                isLast = itemFcmWorker.size - 1 == index,
                onClick = item.onClick
            )
        }
    }

    Spacer(modifier = Modifier.size(60.dp))
}

@Composable
fun ContentsFCMList(viewModelMain: ViewModelMain, child : String){

    LaunchedEffect(child){
        viewModelMain.getFCMList(child = child)
    }

    val fcmAlertList = when (child) {
        "ALERT" -> {
            viewModelMain.state.collectAsState().value.fcmAlertList
        }
        "NOTICE" -> {
            viewModelMain.state.collectAsState().value.fcmNoticeList
        }

        else -> {
            viewModelMain.state.collectAsState().value.fcmNoticeList
        }
    }

    Log.d("HIHI", "fcmAlertList == $fcmAlertList")

    TabletContentWrap {
        Spacer(modifier = Modifier.size(8.dp))

        fcmAlertList.forEachIndexed { index, item ->
            ItemTabletFCMList(
                item = item,
                isLast = fcmAlertList.size - 1 == index
            )
        }

        Spacer(modifier = Modifier.size(8.dp))
    }

    Spacer(modifier = Modifier.size(60.dp))
}