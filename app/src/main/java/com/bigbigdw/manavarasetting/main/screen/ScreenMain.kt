package com.bigbigdw.manavarasetting.main.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.work.WorkManager
import com.bigbigdw.manavarasetting.R
import com.bigbigdw.manavarasetting.firebase.DataFCMBodyNotification
import com.bigbigdw.manavarasetting.Util.Mining
import com.bigbigdw.manavarasetting.main.viewModels.ViewModelFCM
import com.bigbigdw.manavarasetting.main.viewModels.ViewModelMining
import com.bigbigdw.manavarasetting.ui.theme.color1E1E20
import com.bigbigdw.manavarasetting.ui.theme.color844DF3
import com.bigbigdw.manavarasetting.ui.theme.colorEDE6FD
import com.bigbigdw.manavarasetting.ui.theme.colorFFFFFF
import com.bigbigdw.manavarasetting.ui.theme.pretendardvariable
import com.bigbigdw.manavarasetting.ui.theme.textColorType2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenMain(
    viewModelFCM: ViewModelFCM,
    viewModelMining: ViewModelMining,
    workManager: WorkManager
) {

    val context = LocalContext.current

    val (getFCM, setFCM) = remember { mutableStateOf(DataFCMBodyNotification()) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = color1E1E20)
                .verticalScroll(rememberScrollState())
                .semantics { contentDescription = "Overview Screen" },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher),
                contentDescription = null,
                modifier = Modifier
                    .width(72.dp)
                    .height(72.dp)
            )
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(8.dp))
            Text(
                text = "MANAVARASETTING",
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                color = colorFFFFFF,
                fontFamily = pretendardvariable,
                fontWeight = FontWeight(weight = 100)
            )
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(22.dp))
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = color844DF3),
                onClick = { viewModelFCM.getFCMToken(context) },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(text = "FCM 토큰 얻기", textAlign = TextAlign.Center, color = colorFFFFFF, fontSize = 16.sp, fontFamily = pretendardvariable)
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(22.dp))
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = color844DF3),
                onClick = { viewModelFCM.postFCMAlertTest(context) },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(text = "FCM 테스트", textAlign = TextAlign.Center, color = colorEDE6FD, fontSize = 16.sp, fontFamily = pretendardvariable)
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(16.dp))

            TextField(
                value = getFCM.title,
                onValueChange = {
                    setFCM(getFCM.copy(title = it))
                },
                label = { Text("FCM 제목 입력", color = colorFFFFFF) },
                singleLine = true,
                placeholder = { Text("FCM 제목 입력", color = colorFFFFFF) },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0),
                    textColor = colorFFFFFF
                ),
                modifier = Modifier.width(260.dp)
            )
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(16.dp))

            TextField(
                value = getFCM.body,
                onValueChange = {
                    setFCM(getFCM.copy(body = it))
                },
                label = { Text("FCM 바디 입력", color = colorFFFFFF) },
                singleLine = true,
                placeholder = { Text("FCM 바디 입력", color = colorFFFFFF) },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0),
                    textColor = colorFFFFFF
                ),
                modifier = Modifier.width(260.dp)
            )
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(22.dp))
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = color844DF3),
                onClick = { viewModelFCM.postFCMAlert(context = context, getFCM = getFCM) },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(text = "공지사항 등록", textAlign = TextAlign.Center, color = colorEDE6FD, fontSize = 16.sp, fontFamily = pretendardvariable)
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(22.dp))
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = color844DF3),
                onClick = {
                    Mining.miningNaverSeriesAll(context)
                },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(text = "네이버 시리즈 크롤링", textAlign = TextAlign.Center, color = colorEDE6FD, fontSize = 16.sp, fontFamily = pretendardvariable)
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(22.dp))
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = color844DF3),
                onClick = { viewModelMining.doAutoMining(workManager = workManager) },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(text = "자동 크롤링 시작", textAlign = TextAlign.Center, color = colorEDE6FD, fontSize = 16.sp, fontFamily = pretendardvariable)
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(22.dp))
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = color844DF3),
                onClick = { viewModelMining.cancelAutoMining(workManager = workManager) },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(text = "자동 크롤링 정지", textAlign = TextAlign.Center, color = colorEDE6FD, fontSize = 16.sp, fontFamily = pretendardvariable)
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(22.dp))
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = color844DF3),
                onClick = { viewModelMining.checkWorkerStatus(workManager = workManager) },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(text = "Worker 체크", textAlign = TextAlign.Center, color = colorEDE6FD, fontSize = 16.sp, fontFamily = pretendardvariable)
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "By 김대우",
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = textColorType2,
                fontFamily = pretendardvariable
            )
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(8.dp))
            Text(
                text = "BIGBIGDW",
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = textColorType2,
                fontFamily = pretendardvariable
            )
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(60.dp))
        }
    }
}