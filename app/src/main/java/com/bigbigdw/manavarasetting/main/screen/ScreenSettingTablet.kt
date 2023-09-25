package com.bigbigdw.manavarasetting.main.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bigbigdw.manavarasetting.R
import com.bigbigdw.manavarasetting.ui.theme.color000000
import com.bigbigdw.manavarasetting.ui.theme.color21c2ec
import com.bigbigdw.manavarasetting.ui.theme.color31c3ae
import com.bigbigdw.manavarasetting.ui.theme.color4ad7cf
import com.bigbigdw.manavarasetting.ui.theme.color52a9ff
import com.bigbigdw.manavarasetting.ui.theme.color5372de
import com.bigbigdw.manavarasetting.ui.theme.color64c157
import com.bigbigdw.manavarasetting.ui.theme.color7c81ff
import com.bigbigdw.manavarasetting.ui.theme.color8e8e8e
import com.bigbigdw.manavarasetting.ui.theme.color998df9
import com.bigbigdw.manavarasetting.ui.theme.colorabd436
import com.bigbigdw.manavarasetting.ui.theme.colorea927c
import com.bigbigdw.manavarasetting.ui.theme.colorf17fa0
import com.bigbigdw.manavarasetting.ui.theme.colorf6f6f6

@Composable
fun ScreenSettingTabletContents(setMenu: (String) -> Unit, getMenu: String){

    Column(
        modifier = Modifier
            .width(334.dp)
            .fillMaxHeight()
            .background(color = colorf6f6f6)
            .padding(8.dp, 0.dp)
            .verticalScroll(rememberScrollState())
            .semantics { contentDescription = "Overview Screen" },
    ) {

        Spacer(modifier = Modifier.size(8.dp))

        Text(
            modifier = Modifier.padding(16.dp, 0.dp, 0.dp, 0.dp),
            text = "세팅바라",
            fontSize = 24.sp,
            color = color000000,
            fontWeight = FontWeight(weight = 700)
        )

        Spacer(modifier = Modifier.size(8.dp))

        ItemMainSettingSingleTablet(
            containerColor = color52a9ff,
            image = R.drawable.icon_setting_wht,
            title = "세팅바라 현황",
            body = "Periodic Worker 현황",
            setMenu = setMenu,
            getMenu = getMenu
        )

        tabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = color4ad7cf,
            image = R.drawable.icon_fcm_wht,
            title = "FCM 토큰 생성",
            body = "기기별 FCM 토큰 생성",
            setMenu = setMenu,
            getMenu = getMenu
        )

        ItemMainSettingSingleTablet(
            containerColor = color5372de,
            image = R.drawable.icon_fcm_wht,
            title = "FCM 공지사항",
            body = "FCM으로 공지사항 등록",
            setMenu = setMenu,
            getMenu = getMenu
        )

        ItemMainSettingSingleTablet(
            containerColor = color998df9,
            image = R.drawable.icon_fcm_wht,
            title = "FCM Worker",
            body = "FCM 자동화 제어",
            setMenu = setMenu,
            getMenu = getMenu
        )

        ItemMainSettingSingleTablet(
            containerColor = colorea927c,
            image = R.drawable.icon_fcm_wht,
            title = "FCM 현황",
            body = "FCM 푸시 현황 확인",
            setMenu = setMenu,
            getMenu = getMenu
        )

        tabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = colorabd436,
            image = R.drawable.icon_best_wht,
            title = "베스트 리스트 갱신",
            body = "베스트 리스트 수동 갱신",
            setMenu = setMenu,
            getMenu = getMenu
        )

        ItemMainSettingSingleTablet(
            containerColor = colorf17fa0,
            image = R.drawable.icon_best_wht,
            title = "베스트 Worker",
            body = "베스트 Worker 제어",
            setMenu = setMenu,
            getMenu = getMenu
        )

        tabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = color21c2ec,
            image = R.drawable.icon_json_wht,
            title = "DAY JSON 확인",
            body = "오늘의 베스트 리스트 JSON 생성",
            setMenu = setMenu,
            getMenu = getMenu
        )

        ItemMainSettingSingleTablet(
            containerColor = color31c3ae,
            image = R.drawable.icon_json_wht,
            title = "WEEK JSON 확인",
            body = "이번주 베스트 리스트 JSON 생성",
            setMenu = setMenu,
            getMenu = getMenu
        )

        ItemMainSettingSingleTablet(
            containerColor = color7c81ff,
            image = R.drawable.icon_json_wht,
            title = "WEEK JSON 업데이트",
            body = "이번주 베스트 리스트 갱신",
            setMenu = setMenu,
            getMenu = getMenu
        )

        ItemMainSettingSingleTablet(
            containerColor = color64c157,
            image = R.drawable.icon_json_wht,
            title = "JSON Worker",
            body = "JSON Worker 제어",
            setMenu = setMenu,
            getMenu = getMenu
        )

        tabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = color52a9ff,
            image = R.drawable.icon_trophy_wht,
            title = "트로피 정산",
            body = "오늘의 트로피를 수동 정산",
            setMenu = setMenu,
            getMenu = getMenu
        )

        ItemMainSettingSingleTablet(
            containerColor = color52a9ff,
            image = R.drawable.icon_trophy_wht,
            title = "트로피 Worker",
            body = "트로피 Worker 제어",
            setMenu = setMenu,
            getMenu = getMenu
        )

    }
}

@Composable
fun ScreenTablet(title: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorf6f6f6)
            .padding(8.dp, 0.dp)
            .verticalScroll(rememberScrollState())
            .semantics { contentDescription = "Overview Screen" },
    ) {
        Spacer(modifier = Modifier.size(8.dp))

        Text(
            modifier = Modifier.padding(16.dp, 0.dp, 0.dp, 0.dp),
            text = title,
            fontSize = 24.sp,
            color = color000000,
            fontWeight = FontWeight(weight = 700)
        )

        Spacer(modifier = Modifier.size(8.dp))
    }
}

@Composable
fun tabletBorderLine(){
    Spacer(modifier = Modifier.size(8.dp))
    Spacer(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
        .background(color = color8e8e8e))
    Spacer(modifier = Modifier.size(8.dp))
}