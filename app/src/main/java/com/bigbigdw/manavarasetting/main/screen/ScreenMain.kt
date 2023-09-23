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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.bigbigdw.manavarasetting.main.viewModels.DataStoreManager
import com.bigbigdw.manavarasetting.main.viewModels.DataStoreManager.Companion.TESTKEY
import com.bigbigdw.manavarasetting.main.viewModels.DataStoreManager.Companion.TEST_TIME
import com.bigbigdw.manavarasetting.util.Mining
import com.bigbigdw.manavarasetting.util.NaverSeriesGenre
import com.bigbigdw.manavarasetting.util.getNaverSeriesGenre
import com.bigbigdw.manavarasetting.ui.theme.color1E1E20
import com.bigbigdw.manavarasetting.ui.theme.colorEDE6FD
import com.bigbigdw.manavarasetting.ui.theme.colorFFFFFF
import com.bigbigdw.manavarasetting.ui.theme.pretendardvariable
import com.bigbigdw.manavarasetting.util.FCM.getFCMToken
import com.bigbigdw.manavarasetting.util.FCM.postFCMAlert
import com.bigbigdw.manavarasetting.util.FCM.postFCMAlertTest
import com.bigbigdw.manavarasetting.util.PeriodicWorker.cancelWorker
import com.bigbigdw.manavarasetting.util.PeriodicWorker.checkWorker
import com.bigbigdw.manavarasetting.util.PeriodicWorker.doWorker
import com.bigbigdw.manavarasetting.util.calculateTrophy
import com.bigbigdw.manavarasetting.util.makeWeekJson
import com.bigbigdw.manavarasetting.util.uploadJsonArrayToStorageDay
import com.bigbigdw.manavarasetting.util.uploadJsonArrayToStorageWeek
import com.bigbigdw.manavarasetting.util.uploadJsonFile
import com.google.gson.JsonArray
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenMain(
    workManager: WorkManager
) {

    val context = LocalContext.current

    val dataStore = DataStoreManager(context)
    val test = dataStore.getDataStoreString(TESTKEY).collectAsState(initial = "")
    val testTime = dataStore.getDataStoreString(TEST_TIME).collectAsState(initial = "")

    val scope = rememberCoroutineScope()

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
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher),
                contentDescription = null,
                modifier = Modifier
                    .width(72.dp)
                    .height(72.dp)
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )
            Text(
                text = "MANAVARASETTING",
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                color = colorFFFFFF,
                fontFamily = pretendardvariable,
                fontWeight = FontWeight(weight = 100)
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                onClick = { getFCMToken(context) },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(
                    text = "FCM 토큰 얻기",
                    textAlign = TextAlign.Center,
                    color = colorFFFFFF,
                    fontSize = 16.sp,
                    fontFamily = pretendardvariable
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                onClick = { postFCMAlertTest(context) },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(
                    text = "FCM 테스트",
                    textAlign = TextAlign.Center,
                    color = colorEDE6FD,
                    fontSize = 16.sp,
                    fontFamily = pretendardvariable
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
            )

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
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
            )

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
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                onClick = { postFCMAlert(context = context, getFCM = getFCM) },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(
                    text = "공지사항 등록",
                    textAlign = TextAlign.Center,
                    color = colorEDE6FD,
                    fontSize = 16.sp,
                    fontFamily = pretendardvariable
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                onClick = {
                    for (j in NaverSeriesGenre) {
                        for (i in 1..5) {
                            Mining.miningNaverSeriesAll(pageCount = i, genre = j)
                        }
                    }
                },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(
                    text = "네이버 시리즈 크롤링",
                    textAlign = TextAlign.Center,
                    color = colorEDE6FD,
                    fontSize = 16.sp,
                    fontFamily = pretendardvariable
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                onClick = {
                    doWorker(
                        workManager = workManager,
                        repeatInterval = 3,
                        tag = "BEST",
                        timeMill = TimeUnit.HOURS
                    )
                },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(
                    text = "BEST 크롤링 시작",
                    textAlign = TextAlign.Center,
                    color = colorEDE6FD,
                    fontSize = 16.sp,
                    fontFamily = pretendardvariable
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                onClick = { cancelWorker(workManager = workManager,  tag = "BEST") },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(
                    text = "BEST 크롤링 정지",
                    textAlign = TextAlign.Center,
                    color = colorEDE6FD,
                    fontSize = 16.sp,
                    fontFamily = pretendardvariable
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                onClick = {
                    checkWorker(
                        workManager = workManager,
                        tag = "BEST"
                    )
                },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(
                    text = "BEST Worker 체크",
                    textAlign = TextAlign.Center,
                    color = colorEDE6FD,
                    fontSize = 16.sp,
                    fontFamily = pretendardvariable
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                onClick = { uploadJsonFile() },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(
                    text = "JSON 업로드",
                    textAlign = TextAlign.Center,
                    color = colorEDE6FD,
                    fontSize = 16.sp,
                    fontFamily = pretendardvariable
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                onClick = {
                    for (j in NaverSeriesGenre) {
                        uploadJsonArrayToStorageDay(
                            platform = "NAVER_SERIES",
                            genre = getNaverSeriesGenre(j)
                        )
                    }
                },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(
                    text = "DAY JSON 생성",
                    textAlign = TextAlign.Center,
                    color = colorEDE6FD,
                    fontSize = 16.sp,
                    fontFamily = pretendardvariable
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                onClick = {

                },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(
                    text = "카운트",
                    textAlign = TextAlign.Center,
                    color = colorEDE6FD,
                    fontSize = 16.sp,
                    fontFamily = pretendardvariable
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                onClick = {
                    for (j in NaverSeriesGenre) {

                        val jsonArray = JsonArray()

                        for (i in 0..6) {
                            jsonArray.add("")
                        }

                        makeWeekJson(
                            platform = "NAVER_SERIES",
                            genre = getNaverSeriesGenre(j),
                            jsonArray = jsonArray
                        )
                    }
                },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(
                    text = "WEEK JSON 생성",
                    textAlign = TextAlign.Center,
                    color = colorEDE6FD,
                    fontSize = 16.sp,
                    fontFamily = pretendardvariable
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                onClick = {
                    for (j in NaverSeriesGenre) {
                        uploadJsonArrayToStorageWeek(
                            platform = "NAVER_SERIES",
                            genre = getNaverSeriesGenre(j)
                        )
                    }
                },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(
                    text = "WEEK JSON 기존 업데이트",
                    textAlign = TextAlign.Center,
                    color = colorEDE6FD,
                    fontSize = 16.sp,
                    fontFamily = pretendardvariable
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                onClick = {
                    doWorker(
                        workManager = workManager,
                        repeatInterval = 6,
                        tag = "BEST_JSON",
                        timeMill = TimeUnit.HOURS
                    )
                },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(
                    text = "DAY JSON 자동 생성",
                    textAlign = TextAlign.Center,
                    color = colorEDE6FD,
                    fontSize = 16.sp,
                    fontFamily = pretendardvariable
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                onClick = {
                    for (j in NaverSeriesGenre) {
                        cancelWorker(
                            workManager = workManager,
                            tag = "BEST_JSON"
                        )
                    }
                },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(
                    text = "DAY JSON 자동 생성 중지",
                    textAlign = TextAlign.Center,
                    color = colorEDE6FD,
                    fontSize = 16.sp,
                    fontFamily = pretendardvariable
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                onClick = {
                    checkWorker(
                        workManager = workManager,
                        tag = "BEST_JSON"
                    )
                },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(
                    text = "JSON Worker 체크",
                    textAlign = TextAlign.Center,
                    color = colorEDE6FD,
                    fontSize = 16.sp,
                    fontFamily = pretendardvariable
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Magenta),
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
                Text(
                    text = "트로피 정산",
                    textAlign = TextAlign.Center,
                    color = colorEDE6FD,
                    fontSize = 16.sp,
                    fontFamily = pretendardvariable
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Magenta),
                onClick = {
                    doWorker(
                        workManager = workManager,
                        repeatInterval = 3,
                        tag = "BEST_TROPHY",
                        timeMill = TimeUnit.HOURS
                    )
                },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(
                    text = "트로피 정산 자동화 시작",
                    textAlign = TextAlign.Center,
                    color = colorEDE6FD,
                    fontSize = 16.sp,
                    fontFamily = pretendardvariable
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Magenta),
                onClick = {
                    cancelWorker(
                        workManager = workManager,
                        tag = "BEST_TROPHY"
                    )
                },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(
                    text = "트로피 정산 자동화 취소",
                    textAlign = TextAlign.Center,
                    color = colorEDE6FD,
                    fontSize = 16.sp,
                    fontFamily = pretendardvariable
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Magenta),
                onClick = {
                    checkWorker(
                        workManager = workManager,
                        tag = "BEST_TROPHY"
                    )
                },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(
                    text = "트로피 정산 WORKER 체크",
                    textAlign = TextAlign.Center,
                    color = colorEDE6FD,
                    fontSize = 16.sp,
                    fontFamily = pretendardvariable
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                onClick = {
                    doWorker(
                        workManager = workManager,
                        repeatInterval = 20,
                        tag = "TEST",
                        timeMill = TimeUnit.MINUTES
                    )
                },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(
                    text = "테스트 시작",
                    textAlign = TextAlign.Center,
                    color = colorEDE6FD,
                    fontSize = 16.sp,
                    fontFamily = pretendardvariable
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                onClick = {
                    cancelWorker(
                        workManager = workManager,
                        tag = "TEST"
                    )
                },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(
                    text = "테스트 취소",
                    textAlign = TextAlign.Center,
                    color = colorEDE6FD,
                    fontSize = 16.sp,
                    fontFamily = pretendardvariable
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                onClick = {
                    checkWorker(
                        workManager = workManager,
                        tag = "TEST"
                    )
                },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(
                    text = "테스트 확인",
                    textAlign = TextAlign.Center,
                    color = colorEDE6FD,
                    fontSize = 16.sp,
                    fontFamily = pretendardvariable
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                onClick = {
                    scope.launch {
                        dataStore.setDataStoreString(key = TESTKEY, str = "BBBBBB")
                    }
                },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(
                    text = "DATASTORE : ${test.value}",
                    textAlign = TextAlign.Center,
                    color = colorEDE6FD,
                    fontSize = 16.sp,
                    fontFamily = pretendardvariable
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                onClick = {
                    scope.launch {
                        dataStore.setDataStoreString(key = TESTKEY,str = "AAAAA")
                    }
                },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(
                    text = "DATASTORE : ${test.value}",
                    textAlign = TextAlign.Center,
                    color = colorEDE6FD,
                    fontSize = 16.sp,
                    fontFamily = pretendardvariable
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                onClick = {},
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(
                    text = "DATASTORE : ${testTime.value}",
                    textAlign = TextAlign.Center,
                    color = colorEDE6FD,
                    fontSize = 16.sp,
                    fontFamily = pretendardvariable
                )
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
        }
    }
}