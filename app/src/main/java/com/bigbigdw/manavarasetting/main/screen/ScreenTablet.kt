package com.bigbigdw.manavarasetting.main.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bigbigdw.manavarasetting.R
import com.bigbigdw.manavarasetting.main.viewModels.ViewModelMain
import com.bigbigdw.manavarasetting.ui.theme.color000000
import com.bigbigdw.manavarasetting.ui.theme.colorF6F6F6
import com.bigbigdw.manavarasetting.util.DataStoreManager

@Composable
fun ScreenTablet(
    title: String,
    viewModelMain: ViewModelMain,
    setDetailPage: (Boolean) -> Unit,
    setDetailMenu: (String) -> Unit,
    setDetailPlatform: (String) -> Unit,
    setDetailType: (String) -> Unit
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 0.dp, 16.dp, 0.dp)
            .background(color = colorF6F6F6)
            .semantics { contentDescription = "Overview Screen" },
    ) {

        item { Spacer(modifier = Modifier.size(16.dp)) }

        item {
            Text(
                modifier = Modifier.clickable {
                        setDetailPage(false)
                    },
                text = title,
                fontSize = 24.sp,
                color = color000000,
                fontWeight = FontWeight(weight = 700)
            )
        }

        item { Spacer(modifier = Modifier.size(16.dp)) }

        item {
            when (title) {
                "세팅바라 현황" -> {
                    ContentsSetting()
                }
                "FCM 관리" -> {
                    ContentsFCM()
                }
                "FCM 공지사항 리스트" -> {
                    ContentsFCMList(viewModelMain = viewModelMain, child = "NOTICE")
                }
                "FCM 알림 리스트" -> {
                    ContentsFCMList(viewModelMain = viewModelMain, child = "ALERT")
                }
                "웹소설 베스트 리스트" -> {
                    ContentsBestListNovel(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                    )
                }
                "웹툰 베스트 리스트" -> {
                    ContentsBestListComic(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                    )
                }
                "웹툰 JSON 투데이" -> {
                    ContentsBestJsonListComic(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                        type = "JSON 투데이",
                    )
                }
                "웹소설 JSON 투데이" -> {
                    ContentsBestJsonListNovel(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                        type = "JSON 투데이",
                    )
                }
                "웹소설 JSON 주간" -> {
                    ContentsBestJsonListNovel(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                        type = "JSON 주간",
                    )
                }
                "웹툰 JSON 주간" -> {
                    ContentsBestJsonListComic(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                        type = "JSON 주간",
                    )
                }
                "웹소설 JSON 월간" -> {
                    ContentsBestJsonListNovel(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                        type = "JSON 월간",
                    )
                }
                "웹툰 JSON 월간" -> {
                    ContentsBestJsonListComic(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                        type = "JSON 월간",
                    )
                }
                "웹소설 JSON 주간 트로피" -> {
                    ContentsBestJsonListNovel(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        type = "JSON 주간 트로피",
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                    )
                }
                "웹툰 JSON 주간 트로피" -> {
                    ContentsBestJsonListComic(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        type = "JSON 주간 트로피",
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                    )
                }
                "웹소설 JSON 월간 트로피" -> {
                    ContentsBestJsonListNovel(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        type = "JSON 월간 트로피",
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                    )
                }
                "웹툰 JSON 월간 트로피" -> {
                    ContentsBestJsonListComic(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        type = "JSON 월간 트로피",
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                    )
                }
                "웹소설 트로피 주간 토탈" -> {
                    ContentsBestJsonListNovel(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        type = "트로피 주간",
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                    )
                }
                "웹툰 트로피 주간 토탈" -> {
                    ContentsBestJsonListComic(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                        type = "트로피 주간",
                    )
                }
                "웹소설 트로피 월간 토탈" -> {
                    ContentsBestJsonListNovel(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        type = "트로피 월간",
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                    )
                }
                "웹툰 트로피 월간 토탈" -> {
                    ContentsBestJsonListComic(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                        type = "트로피 월간",
                    )
                }
                "네이버 시리즈 웹툰" -> {
                    ContentsPlatform(
                        viewModelMain = viewModelMain,
                        platform = "NAVER_SERIES",
                        type = "COMIC",
                        key = DataStoreManager.MINING_NAVER_SERIES_COMIC,
                        logo = R.drawable.logo_naver
                    )
                }
                "네이버 시리즈 웹소설" -> {
                    ContentsPlatform(
                        viewModelMain = viewModelMain,
                        platform = "NAVER_SERIES",
                        type = "NOVEL",
                        key = DataStoreManager.MINING_NAVER_SERIES_NOVEL,
                        logo = R.drawable.logo_naver
                    )
                }
                "조아라 웹소설" -> {
                    ContentsPlatform(
                        viewModelMain = viewModelMain,
                        platform = "JOARA",
                        type = "NOVEL",
                        key = DataStoreManager.MINING_JOARA_NOVEL,
                        logo = R.drawable.logo_joara
                    )
                }
                "조아라 프리미엄 웹소설" -> {
                    ContentsPlatform(
                        viewModelMain = viewModelMain,
                        platform = "JOARA_NOBLESS",
                        type = "NOVEL",
                        key = DataStoreManager.MINING_JOARA_PREMIUM_NOVEL,
                        logo = R.drawable.logo_joara_nobless
                    )
                }
                "조아라 노블레스 웹소설" -> {
                    ContentsPlatform(
                        viewModelMain = viewModelMain,
                        platform = "JOARA_PREMIUM",
                        type = "NOVEL",
                        key = DataStoreManager.MINING_JOARA_NOBLESS_NOVEL,
                        logo = R.drawable.logo_joara_premium
                    )
                }
                "챌린지 리그" -> {
                    ContentsPlatform(
                        viewModelMain = viewModelMain,
                        platform = "NAVER_CHALLENGE",
                        type = "NOVEL",
                        key = DataStoreManager.MINING_NAVER_CHALLENGE_NOVEL,
                        logo = R.drawable.logo_naver_challenge
                    )
                }
                "베스트 리그" -> {
                    ContentsPlatform(
                        viewModelMain = viewModelMain,
                        platform = "NAVER_BEST",
                        type = "NOVEL",
                        key = DataStoreManager.MINING_NAVER_BEST_NOVEL,
                        logo = R.drawable.logo_naver_challenge
                    )
                }
                "네이버 웹소설 유료" -> {
                    ContentsPlatform(
                        viewModelMain = viewModelMain,
                        platform = "NAVER_WEBNOVEL_PAY",
                        type = "NOVEL",
                        key = DataStoreManager.MINING_NAVER_WEBNOVEL_PAY_NOVEL,
                        logo = R.drawable.logo_naver
                    )
                }
                "네이버 웹소설 무료" -> {
                    ContentsPlatform(
                        viewModelMain = viewModelMain,
                        platform = "NAVER_WEBNOVEL_FREE",
                        type = "NOVEL",
                        key = DataStoreManager.MINING_NAVER_WEBNOVEL_FREE_NOVEL,
                        logo = R.drawable.logo_naver
                    )
                }
                "리디 판타지 웹소설" -> {
                    ContentsPlatform(
                        viewModelMain = viewModelMain,
                        platform = "RIDI_FANTAGY",
                        type = "NOVEL",
                        key = DataStoreManager.MINING_RIDI_FANTAGY_NOVEL,
                        logo = R.drawable.logo_ridibooks
                    )
                }
                "리디 로맨스 웹소설" -> {
                    ContentsPlatform(
                        viewModelMain = viewModelMain,
                        platform = "RIDI_ROMANCE",
                        type = "NOVEL",
                        key = DataStoreManager.MINING_RIDI_ROMANCE_NOVEL,
                        logo = R.drawable.logo_ridibooks
                    )
                }
                "원스토리 판타지 웹소설" -> {
                    ContentsPlatform(
                        viewModelMain = viewModelMain,
                        platform = "ONESTORY_FANTAGY",
                        type = "NOVEL",
                        key = DataStoreManager.MINING_ONESTORY_FANTAGY_NOVEL,
                        logo = R.drawable.logo_onestore
                    )
                }
                "원스토리 로맨스 웹소설" -> {
                    ContentsPlatform(
                        viewModelMain = viewModelMain,
                        platform = "ONESTORY_ROMANCE",
                        type = "NOVEL",
                        key = DataStoreManager.MINING_ONESTORY_ROMANCE_NOVEL,
                        logo = R.drawable.logo_onestore
                    )
                }
                "원스토리 PASS 판타지 웹소설" -> {
                    ContentsPlatform(
                        viewModelMain = viewModelMain,
                        platform = "ONESTORY_PASS_FANTAGY",
                        type = "NOVEL",
                        key = DataStoreManager.MINING_ONESTORY_PASS_FANTAGY_NOVEL,
                        logo = R.drawable.logo_onestore
                    )
                }
                "원스토리 PASS 로맨스 웹소설" -> {
                    ContentsPlatform(
                        viewModelMain = viewModelMain,
                        platform = "ONESTORY_PASS_ROMANCE",
                        type = "NOVEL",
                        key = DataStoreManager.MINING_ONESTORY_PASS_ROMANCE_NOVEL,
                        logo = R.drawable.logo_onestore
                    )
                }
                "카카오 스테이지 웹소설" -> {
                    ContentsPlatform(
                        viewModelMain = viewModelMain,
                        platform = "KAKAO_STAGE",
                        type = "NOVEL",
                        key = DataStoreManager.MINING_KAKAO_STAGE_NOVEL,
                        logo = R.drawable.logo_kakaostage
                    )
                }
                "문피아 유료 웹소설" -> {
                    ContentsPlatform(
                        viewModelMain = viewModelMain,
                        platform = "MUNPIA_PAY",
                        type = "NOVEL",
                        key = DataStoreManager.MINING_MUNPIA_PAY_NOVEL,
                        logo = R.drawable.logo_munpia
                    )
                }
                "문피아 무료 웹소설" -> {
                    ContentsPlatform(
                        viewModelMain = viewModelMain,
                        platform = "MUNPIA_FREE",
                        type = "NOVEL",
                        key = DataStoreManager.MINING_MUNPIA_FREE_NOVEL,
                        logo = R.drawable.logo_munpia
                    )
                }
                "톡소다 웹소설" -> {
                    ContentsPlatform(
                        viewModelMain = viewModelMain,
                        platform = "TOKSODA",
                        type = "NOVEL",
                        key = DataStoreManager.MINING_TOKSODA_NOVEL,
                        logo = R.drawable.logo_toksoda
                    )
                }
                "톡소다 자유연재" -> {
                    ContentsPlatform(
                        viewModelMain = viewModelMain,
                        platform = "TOKSODA_FREE",
                        type = "NOVEL",
                        key = DataStoreManager.MINING_TOKSODA_NOVEL,
                        logo = R.drawable.logo_toksoda
                    )
                }
                "투데이 장르 베스트" -> {
                    ContentsGenre(
                        menuType = "투데이",
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                    )
                }
                "주간 장르 베스트" -> {
                    ContentsGenre(
                        menuType = "주간",
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                    )
                }
                "월간 장르 베스트" -> {
                    ContentsGenre(
                        menuType = "월간",
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                    )
                }
                "위험 옵션" -> {
                    ContentsDangerOption(viewModelMain = viewModelMain)
                }
                "실험실" -> {
                    ContentsLabs()
                }
                "웹툰 현황" -> {
                    ContentsWebtoon()
                }
                "웹소설 현황" -> {
                    ContentsNovel()
                }
            }
        }
    }
}

@Composable
fun ScreenTabletDetail(
    setDetailPage: (Boolean) -> Unit,
    getDetailMenu: String,
    viewModelMain: ViewModelMain,
    getDetailPlatform: String,
    getDetailType: String,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 0.dp, 16.dp, 0.dp)
            .semantics { contentDescription = "Overview Screen" },
    ) {

        Spacer(modifier = Modifier.size(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically){
            Image(
                painter = painterResource(id = R.drawable.icon_arrow_left),
                contentDescription = null,
                modifier = Modifier
                    .width(30.dp)
                    .height(30.dp)
            )

            Text(
                modifier = Modifier
                    .padding(16.dp, 0.dp, 0.dp, 0.dp)
                    .clickable {
                        setDetailPage(false)
                    },
                text = getDetailMenu,
                fontSize = 24.sp,
                color = color000000,
                fontWeight = FontWeight(weight = 700)
            )
        }

        if (getDetailMenu.contains("JSON 투데이")) {
            ContentsBestListDetail(
                viewModelMain = viewModelMain,
                type = "JSON",
                getDetailPlatform = getDetailPlatform,
                getDetailType = getDetailType,
            )
        }  else if (getDetailMenu.contains("JSON 주간")) {
            ContentsBestListDetailWeek(
                viewModelMain = viewModelMain,
                menu = "주간",
                getDetailPlatform = getDetailPlatform,
                getDetailType = getDetailType,
            )
        }  else if (getDetailMenu.contains("JSON 월간")) {
            ContentsBestListDetailWeek(
                viewModelMain = viewModelMain,
                menu = "월간",
                getDetailPlatform = getDetailPlatform,
                getDetailType = getDetailType,
            )
        } else if (getDetailMenu.contains("JSON 주간 트로피")) {
            ContentsBestListJsonTrophy(
                viewModelMain = viewModelMain,
                menu = "주간",
                getDetailPlatform = getDetailPlatform,
                getDetailType = getDetailType,
            )
        } else if (getDetailMenu.contains("JSON 월간 트로피")) {
            ContentsBestListJsonTrophy(
                viewModelMain = viewModelMain,
                menu = "월간",
                getDetailPlatform = getDetailPlatform,
                getDetailType = getDetailType,
            )
        } else if (getDetailMenu.contains("트로피 주간")) {
            ContentsBestListDetailTrophy(
                viewModelMain = viewModelMain,
                menu = "주간",
                getDetailPlatform = getDetailPlatform,
                getDetailType = getDetailType,
            )
        } else if (getDetailMenu.contains("트로피 월간")) {
            ContentsBestListDetailTrophy(
                viewModelMain = viewModelMain,
                menu = "월간",
                getDetailPlatform = getDetailPlatform,
                getDetailType = getDetailType,
            )
        } else if (getDetailMenu.contains("베스트")) {
            ContentsBestListDetail(
                viewModelMain = viewModelMain,
                type = "BEST",
                getDetailPlatform = getDetailPlatform,
                getDetailType = getDetailType,
            )
        } else if (getDetailMenu.contains("장르 투데이")) {
            GenreDetailToday(
                viewModelMain = viewModelMain,
                getDetailPlatform = getDetailPlatform,
                getDetailType = getDetailType,
            )
        } else if (getDetailMenu.contains("장르 주간")) {
            ContentsBestListDetail(
                viewModelMain = viewModelMain,
                type = "BEST",
                getDetailPlatform = getDetailPlatform,
                getDetailType = getDetailType,
            )
        } else if (getDetailMenu.contains("장르 월간")) {
            ContentsBestListDetail(
                viewModelMain = viewModelMain,
                type = "BEST",
                getDetailPlatform = getDetailPlatform,
                getDetailType = getDetailType,
            )
        }
    }
}