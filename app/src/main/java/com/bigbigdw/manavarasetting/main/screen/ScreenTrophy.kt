package com.bigbigdw.manavarasetting.main.screen

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.work.WorkManager
import com.bigbigdw.manavarasetting.R
import com.bigbigdw.manavarasetting.main.viewModels.ViewModelMain
import com.bigbigdw.manavarasetting.ui.theme.color000000
import com.bigbigdw.manavarasetting.ui.theme.colorF6F6F6
import com.bigbigdw.manavarasetting.util.FCM
import com.bigbigdw.manavarasetting.util.NaverSeriesComicGenre
import com.bigbigdw.manavarasetting.util.calculateTrophy
import com.bigbigdw.manavarasetting.util.getNaverSeriesGenre
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Composable
fun ScreenMainTrophy(
    viewModelMain: ViewModelMain
) {

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

            MainHeader(image = R.drawable.icon_trophy, title = "트로피 최신화 현황")

            ContentsTrophy(viewModelMain = viewModelMain)
        }
    }
}

@Composable
fun ContentsTrophy( viewModelMain: ViewModelMain) {

    val context = LocalContext.current
    val workManager = WorkManager.getInstance(context)


    ItemTabletTitle(str = "네이버 시리즈 웹툰", isTopPadding = false)

    ItemTabletTitle(str = "네이버 시리즈 웹소설")

    Spacer(modifier = Modifier.size(16.dp))

    TabletContentWrapBtn(
        onClick = {
            runBlocking {
                for (j in NaverSeriesComicGenre) {
                    launch {
                        calculateTrophy(
                            platform = "NAVER_SERIES",
                            genre = getNaverSeriesGenre(j),
                            type = "COMIC"
                        )
                    }
                }
            }

            FCM.postFCMAlertTest(context = context, message = "트로피 정산이 완료되었습니다")
        },
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text =  "트로피 정산",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        },
        isContinue = false
    )

    ItemTabletTitle(str = "트로피 정산 현황")

    Spacer(modifier = Modifier.size(60.dp))
}