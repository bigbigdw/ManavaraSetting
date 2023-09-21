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
import com.bigbigdw.manavarasetting.util.Mining
import com.bigbigdw.manavarasetting.util.NaverSeriesGenre
import com.bigbigdw.manavarasetting.util.getNaverSeriesGenre
import com.bigbigdw.manavarasetting.main.viewModels.ViewModelFCM
import com.bigbigdw.manavarasetting.main.viewModels.ViewModelMining
import com.bigbigdw.manavarasetting.ui.theme.color1E1E20
import com.bigbigdw.manavarasetting.ui.theme.color844DF3
import com.bigbigdw.manavarasetting.ui.theme.colorEDE6FD
import com.bigbigdw.manavarasetting.ui.theme.colorFFFFFF
import com.bigbigdw.manavarasetting.ui.theme.pretendardvariable
import com.bigbigdw.manavarasetting.ui.theme.textColorType2
import com.bigbigdw.manavarasetting.util.calculateTrophy
import com.bigbigdw.manavarasetting.util.makeWeekJson
import com.bigbigdw.manavarasetting.util.uploadJsonArrayToStorageDay
import com.bigbigdw.manavarasetting.util.uploadJsonArrayToStorageWeek
import com.bigbigdw.manavarasetting.util.uploadJsonFile
import com.google.gson.JsonArray

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
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(120.dp))
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
                    for(j in NaverSeriesGenre){
                        for(i in 1..5){
                            Mining.miningNaverSeriesAll(pageCount = i, genre = j)
                        }
                    }
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
                onClick = { viewModelMining.doAutoMiningBest(workManager = workManager) },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(text = "BEST 크롤링 시작", textAlign = TextAlign.Center, color = colorEDE6FD, fontSize = 16.sp, fontFamily = pretendardvariable)
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(22.dp))
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = color844DF3),
                onClick = { viewModelMining.cancelAutoMiningBest(workManager = workManager) },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(text = "BEST 크롤링 정지", textAlign = TextAlign.Center, color = colorEDE6FD, fontSize = 16.sp, fontFamily = pretendardvariable)
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(22.dp))
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = color844DF3),
                onClick = { viewModelMining.checkWorkerStatusBest(workManager = workManager) },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(text = "BEST Worker 체크", textAlign = TextAlign.Center, color = colorEDE6FD, fontSize = 16.sp, fontFamily = pretendardvariable)
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(22.dp))
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = color844DF3),
                onClick = { uploadJsonFile() },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(text = "JSON 업로드", textAlign = TextAlign.Center, color = colorEDE6FD, fontSize = 16.sp, fontFamily = pretendardvariable)
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(22.dp))
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = color844DF3),
                onClick = {
                    for (j in NaverSeriesGenre) {
                        uploadJsonArrayToStorageDay(platform = "NAVER_SERIES", genre = getNaverSeriesGenre(j))
                    }
                },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(text = "DAY JSON 생성", textAlign = TextAlign.Center, color = colorEDE6FD, fontSize = 16.sp, fontFamily = pretendardvariable)
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(22.dp))
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = color844DF3),
                onClick = {
                    viewModelMining.incrementCounter()

                },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(text = "카운트", textAlign = TextAlign.Center, color = colorEDE6FD, fontSize = 16.sp, fontFamily = pretendardvariable)
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(22.dp))
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = color844DF3),
                onClick = {
                    for (j in NaverSeriesGenre) {

                        val jsonArray = JsonArray()

                        for(i in 0..6){
                            jsonArray.add("")
                        }

                        makeWeekJson(platform = "NAVER_SERIES", genre = getNaverSeriesGenre(j), jsonArray = jsonArray)
                    }
                },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(text = "WEEK JSON 생성", textAlign = TextAlign.Center, color = colorEDE6FD, fontSize = 16.sp, fontFamily = pretendardvariable)
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(22.dp))
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = color844DF3),
                onClick = {
                    for (j in NaverSeriesGenre) {
                        uploadJsonArrayToStorageWeek(platform = "NAVER_SERIES", genre = getNaverSeriesGenre(j))
                    }
                },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(text = "WEEK JSON 기존 업데이트", textAlign = TextAlign.Center, color = colorEDE6FD, fontSize = 16.sp, fontFamily = pretendardvariable)
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(22.dp))
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = color844DF3),
                onClick = {
                    for (j in NaverSeriesGenre) {
                        viewModelMining.doAutoMiningBestJSON(workManager = workManager)
                    }
                },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(text = "DAY JSON 자동 생성", textAlign = TextAlign.Center, color = colorEDE6FD, fontSize = 16.sp, fontFamily = pretendardvariable)
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(22.dp))
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = color844DF3),
                onClick = {
                    for (j in NaverSeriesGenre) {
                        viewModelMining.cancelAutoMiningBestJSON(workManager = workManager)
                    }
                },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(text = "DAY JSON 자동 생성 중지", textAlign = TextAlign.Center, color = colorEDE6FD, fontSize = 16.sp, fontFamily = pretendardvariable)
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(22.dp))
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = color844DF3),
                onClick = { viewModelMining.checkWorkerStatusBestJSON(workManager = workManager) },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(text = "JSON Worker 체크", textAlign = TextAlign.Center, color = colorEDE6FD, fontSize = 16.sp, fontFamily = pretendardvariable)
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(22.dp))
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = color844DF3),
                onClick = {
                    for (j in NaverSeriesGenre) {
                        calculateTrophy(platform = "NAVER_SERIES", genre = getNaverSeriesGenre(j))
                    }
                },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(text = "트로피 정산", textAlign = TextAlign.Center, color = colorEDE6FD, fontSize = 16.sp, fontFamily = pretendardvariable)
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(22.dp))
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = color844DF3),
                onClick = {
                    viewModelMining.doAutoMiningBestTrophy(workManager)
                },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(text = "트로피 정산 자동화 시작", textAlign = TextAlign.Center, color = colorEDE6FD, fontSize = 16.sp, fontFamily = pretendardvariable)
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(22.dp))
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = color844DF3),
                onClick = {
                    viewModelMining.cancelAutoMiningBestTrophy(workManager)
                },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(text = "트로피 정산 자동화 취소", textAlign = TextAlign.Center, color = colorEDE6FD, fontSize = 16.sp, fontFamily = pretendardvariable)
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(22.dp))
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = color844DF3),
                onClick = {
                    viewModelMining.cancelAutoMiningBestTrophy(workManager)
                },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(text = "트로피 정산 WORKER 체크", textAlign = TextAlign.Center, color = colorEDE6FD, fontSize = 16.sp, fontFamily = pretendardvariable)
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(120.dp))
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