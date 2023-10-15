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
import com.bigbigdw.manavarasetting.util.changePlatformNameEng
import com.bigbigdw.manavarasetting.util.getPlatformLogo
import com.bigbigdw.manavarasetting.util.novelListKor

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

        item{
            if(novelListKor().contains(title)){
                ContentsPlatform(
                    platform = changePlatformNameEng(title),
                    type = "NOVEL",
                    logo = getPlatformLogo(title),
                    setDetailPage = setDetailPage,
                    setDetailMenu = setDetailMenu,
                    setDetailPlatform = setDetailPlatform,
                    setDetailType = setDetailType,
                )
            }
        }

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
                    ContentsListNovel(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                        type = "베스트 리스트"
                    )
                }
                "투데이 장르 베스트" -> {
                    ContentsGenre(
                        menuType = "투데이 장르 베스트",
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                    )
                }
                "주간 장르 베스트" -> {
                    ContentsGenre(
                        menuType = "주간 장르 베스트",
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                    )
                }
                "월간 장르 베스트" -> {
                    ContentsGenre(
                        menuType = "월간 장르 베스트",
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                    )
                }
                "웹소설 JSON 투데이" -> {
                    ContentsListNovel(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                        type = "JSON 투데이 베스트",
                    )
                }
                "웹소설 JSON 주간" -> {
                    ContentsListNovel(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                        type = "JSON 주간 베스트",
                    )
                }
                "웹소설 JSON 월간" -> {
                    ContentsListNovel(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                        type = "JSON 월간 베스트",
                    )
                }
                "웹소설 JSON 주간 트로피" -> {
                    ContentsListNovel(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        type = "JSON 주간 트로피",
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                    )
                }
                "웹소설 JSON 월간 트로피" -> {
                    ContentsListNovel(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        type = "JSON 월간 트로피",
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                    )
                }
                "웹소설 트로피 주간 토탈" -> {
                    ContentsListNovel(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                        type = "트로피 주간 토탈",
                    )
                }
                "웹소설 트로피 월간 토탈" -> {
                    ContentsListNovel(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                        type = "트로피 월간 토탈",
                    )
                }
                "웹소설 투데이 장르 JSON" -> {
                    ContentsGenre(
                        menuType = "투데이 장르 JSON",
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                    )
                }
                "웹소설 주간 장르 JSON" -> {
                    ContentsGenre(
                        menuType = "주간 장르 JSON",
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                    )
                }
                "웹소설 월간 장르 JSON" -> {
                    ContentsGenre(
                        menuType = "월간 장르 JSON",
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
                        type = "베스트 리스트"
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
                "웹툰 JSON 주간" -> {
                    ContentsBestJsonListComic(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                        type = "JSON 주간 베스트",
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
                "웹툰 JSON 주간 트로피" -> {
                    ContentsBestJsonListComic(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        type = "JSON 주간 트로피",
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
                "웹툰 트로피 주간 토탈" -> {
                    ContentsBestJsonListComic(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                        type = "트로피 주간",
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

        if (getDetailMenu.contains("베스트 리스트")) {
            ContentsBestListDetail(
                viewModelMain = viewModelMain,
                type = "BEST",
                getDetailPlatform = getDetailPlatform,
                getDetailType = getDetailType,
            )
        } else if (getDetailMenu.contains("투데이 장르 베스트")) {
            GenreDetail(
                viewModelMain = viewModelMain,
                getDetailPlatform = getDetailPlatform,
                getDetailType = getDetailType,
                menuType = "투데이 장르 베스트"
            )
        } else if (getDetailMenu.contains("주간 장르 베스트")) {
            GenreDetail(
                viewModelMain = viewModelMain,
                getDetailPlatform = getDetailPlatform,
                getDetailType = getDetailType,
                menuType = "주간"
            )
        } else if (getDetailMenu.contains("월간 장르 베스트")) {
            GenreDetail(
                viewModelMain = viewModelMain,
                getDetailPlatform = getDetailPlatform,
                getDetailType = getDetailType,
                menuType = "월간"
            )
        } else if (getDetailMenu.contains("JSON 투데이 베스트")) {
            ContentsBestListDetail(
                viewModelMain = viewModelMain,
                type = "JSON",
                getDetailPlatform = getDetailPlatform,
                getDetailType = getDetailType,
            )
        } else if (getDetailMenu.contains("JSON 주간 베스트")) {
            ContentsBestListDetailWeek(
                viewModelMain = viewModelMain,
                menu = "주간",
                getDetailPlatform = getDetailPlatform,
                getDetailType = getDetailType,
            )
        } else if (getDetailMenu.contains("JSON 월간 베스트")) {
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
        } else if (getDetailMenu.contains("트로피 주간 토탈")) {
            ContentsBestListDetailTrophy(
                viewModelMain = viewModelMain,
                menu = "주간",
                getDetailPlatform = getDetailPlatform,
                getDetailType = getDetailType,
            )
        } else if (getDetailMenu.contains("트로피 월간 토탈")) {
            ContentsBestListDetailTrophy(
                viewModelMain = viewModelMain,
                menu = "월간",
                getDetailPlatform = getDetailPlatform,
                getDetailType = getDetailType,
            )
        } else if (getDetailMenu.contains("투데이 장르 JSON")) {
            GenreDetailJson(
                viewModelMain = viewModelMain,
                getDetailPlatform = getDetailPlatform,
                getDetailType = getDetailType,
                menuType = "투데이"
            )
        } else if (getDetailMenu.contains("주간 장르 JSON")) {
            GenreDetailJson(
                viewModelMain = viewModelMain,
                getDetailPlatform = getDetailPlatform,
                getDetailType = getDetailType,
                menuType = "주간"
            )
        } else if (getDetailMenu.contains("월간 장르 JSON")) {
            GenreDetailJson(
                viewModelMain = viewModelMain,
                getDetailPlatform = getDetailPlatform,
                getDetailType = getDetailType,
                menuType = "월간"
            )
        }
    }
}