package com.bigbigdw.manavarasetting.main.screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ContentsBestJsonListComic(
    setDetailPage: (Boolean) -> Unit,
    setDetailMenu: (String) -> Unit,
    setDetailPlatform: (String) -> Unit,
    setDetailType: (String) -> Unit,
    type: String,
) {



    ItemTabletTitle(str = "네이버 시리즈 소설", isTopPadding = false)

    TabletContentWrap {
        ItemMainTabletContent(
            title = "네이버 시리즈",
            isLast = true,
            onClick = {
                setDetailPage(true)
                setDetailMenu("NAVER_SERIES $type ")
                setDetailPlatform("NAVER_SERIES")
                setDetailType("NOVEL")
            }
        )
    }

    Spacer(modifier = Modifier.size(60.dp))
}

@Composable
fun ContentsBestJsonListNovel(
    setDetailPage: (Boolean) -> Unit,
    setDetailMenu: (String) -> Unit,
    setDetailPlatform: (String) -> Unit,
    setDetailType: (String) -> Unit,
    type: String,
) {


    ItemTabletTitle(str = "네이버 시리즈 웹툰", isTopPadding = false)

    TabletContentWrap {
        ItemMainTabletContent(
            title = "네이버 시리즈 ",
            isLast = true,
            onClick = {
                setDetailPage(true)
                setDetailMenu("NAVER_SERIES $type")
                setDetailPlatform("NAVER_SERIES")
                setDetailType("COMIC")
            }
        )
    }

    Spacer(modifier = Modifier.size(16.dp))

    ItemTabletTitle(str = "조아라 노블레스", isTopPadding = false)

    TabletContentWrap {
        ItemMainTabletContent(
            title = "조아라 노블레스",
            isLast = true,
            onClick = {
                setDetailPage(true)
                setDetailMenu("JOARA_NOBLESS 베스트 리스트")
                setDetailPlatform("JOARA_NOBLESS")
                setDetailType("NOVEL")
            }
        )
    }

    Spacer(modifier = Modifier.size(16.dp))

    ItemTabletTitle(str = "조아라 프리미엄", isTopPadding = false)

    TabletContentWrap {
        ItemMainTabletContent(
            title = "조아라 프리미엄",
            isLast = true,
            onClick = {
                setDetailPage(true)
                setDetailMenu("JOARA_PREMIUM 베스트 리스트 ")
                setDetailPlatform("JOARA_PREMIUM")
                setDetailType("NOVEL")
            }
        )
    }

    Spacer(modifier = Modifier.size(16.dp))

    ItemTabletTitle(str = "챌린지 리그", isTopPadding = false)

    TabletContentWrap {
        ItemMainTabletContent(
            title = "챌린지 리그",
            isLast = true,
            onClick = {
                setDetailPage(true)
                setDetailMenu("NAVER_CHALLENGE 챌린지 리그")
                setDetailPlatform("NAVER_CHALLENGE")
                setDetailType("NOVEL")
            }
        )
    }

    Spacer(modifier = Modifier.size(60.dp))
}