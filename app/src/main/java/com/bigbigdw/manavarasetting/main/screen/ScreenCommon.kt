package com.bigbigdw.manavarasetting.main.screen

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.bigbigdw.manavarasetting.ui.theme.color1E1E20
import com.bigbigdw.manavarasetting.ui.theme.colorA7ACB7
import com.bigbigdw.manavarasetting.ui.theme.colorEDE6FD
import com.bigbigdw.manavarasetting.ui.theme.colorFFFFFF
import com.bigbigdw.manavarasetting.ui.theme.pretendardvariable
import com.bigbigdw.manavarasetting.util.BestRef
import com.bigbigdw.manavarasetting.util.DBDate
import com.bigbigdw.manavarasetting.util.FCM
import com.bigbigdw.manavarasetting.util.Mining
import com.bigbigdw.manavarasetting.util.NaverSeriesGenre
import com.bigbigdw.manavarasetting.util.PeriodicWorker
import com.bigbigdw.manavarasetting.util.calculateTrophy
import com.bigbigdw.manavarasetting.util.getNaverSeriesGenre
import com.bigbigdw.manavarasetting.util.makeWeekJson
import com.bigbigdw.manavarasetting.util.uploadJsonArrayToStorageDay
import com.bigbigdw.manavarasetting.util.uploadJsonArrayToStorageWeek
import com.bigbigdw.manavarasetting.util.uploadJsonFile
import com.google.gson.JsonArray
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@Composable
fun BackOnPressed() {
    val context = LocalContext.current
    var backPressedState by remember { mutableStateOf(true) }
    var backPressedTime = 0L

    BackHandler(enabled = backPressedState) {
        if(System.currentTimeMillis() - backPressedTime <= 400L) {
            // 앱 종료
            (context as Activity).finish()
        } else {
            backPressedState = true
            Toast.makeText(context, "한 번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.background(color = Color.White).fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
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
                text = "로딩 중...",
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                color = colorA7ACB7,
                fontFamily = pretendardvariable
            )
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(8.dp))
            CircularProgressIndicator()
        }

    }
}

@Composable
fun ScreenTest() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Text(
            text = "SETTING",
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Test(
    workManager: WorkManager
) {

    val context = LocalContext.current

    val dataStore = DataStoreManager(context)
    val test = dataStore.getDataStoreString(DataStoreManager.TESTKEY).collectAsState(initial = "")
    val testTime = dataStore.getDataStoreString(DataStoreManager.TEST_TIME).collectAsState(initial = "")

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
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                onClick = {
                    for (j in NaverSeriesGenre) {

                        if (DBDate.getDayOfWeekAsNumber() == 0) {
                            BestRef.setBestRef(platform = "NAVER_SERIES", genre = j).child("TROPHY_WEEK").removeValue()
                        }

                        if (DBDate.datedd() == "01") {
                            BestRef.setBestRef(platform = "NAVER_SERIES", genre = j).child("TROPHY_MONTH").removeValue()
                        }

                        for (i in 1..5) {
                            Mining.miningNaverSeriesAll(pageCount = i, genre = j)
                        }
                    }
                    FCM.postFCMAlertTest(context = context, message = "베스트 리스트가 갱신되었습니다")
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
                    PeriodicWorker.doWorker(
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
                onClick = { PeriodicWorker.cancelWorker(workManager = workManager,  tag = "BEST") },
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
                    PeriodicWorker.checkWorker(
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
                    color = Color.Black,
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
                    FCM.postFCMAlertTest(context = context, message = "DAY JSON 생성이 완료되었습니다")
                },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(
                    text = "DAY JSON 생성",
                    textAlign = TextAlign.Center,
                    color = Color.Black,
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
                    color = Color.Black,
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
                    color = Color.Black,
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
                    PeriodicWorker.doWorker(
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
                    color = Color.Black,
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
                        PeriodicWorker.cancelWorker(
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
                    color = Color.Black,
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
                    PeriodicWorker.checkWorker(
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
                    color = Color.Black,
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
                    FCM.postFCMAlertTest(context = context, message = "트로피 정산이 완료되었습니다")
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
                    PeriodicWorker.doWorker(
                        workManager = workManager,
                        repeatInterval = 9,
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
                    PeriodicWorker.cancelWorker(
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
                    PeriodicWorker.checkWorker(
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
                    PeriodicWorker.doWorker(
                        workManager = workManager,
                        repeatInterval = 15,
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
                    PeriodicWorker.cancelWorker(
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
                    PeriodicWorker.checkWorker(
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
                        dataStore.setDataStoreString(key = DataStoreManager.TESTKEY, str = "BBBBBB")
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
                        dataStore.setDataStoreString(key = DataStoreManager.TESTKEY,str = "AAAAA")
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