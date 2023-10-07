package com.bigbigdw.manavarasetting.main.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.work.WorkManager
import com.bigbigdw.manavarasetting.R
import com.bigbigdw.manavarasetting.main.viewModels.ViewModelMain
import com.bigbigdw.manavarasetting.ui.theme.colorF6F6F6
import com.bigbigdw.manavarasetting.util.JoaraGenre
import com.bigbigdw.manavarasetting.util.NaverSeriesComicGenre
import com.bigbigdw.manavarasetting.util.NaverSeriesNovelGenre
import com.bigbigdw.manavarasetting.util.getJoaraGenre
import com.bigbigdw.manavarasetting.util.getJoaraGenreKor
import com.bigbigdw.manavarasetting.util.getNaverSeriesGenre
import com.bigbigdw.manavarasetting.util.getNaverSeriesGenreKor

@Composable
fun ScreenMainBest(
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

            MainHeader(image = R.drawable.icon_best, title = "베스트 리스트 현황")

        }
    }
}

@Composable
fun ContentsBestListNovel(
    setDetailPage: (Boolean) -> Unit,
    setDetailMenu: (String) -> Unit,
    setDetailPlatform: (String) -> Unit,
    setDetailGenre: (String) -> Unit,
    setDetailType: (String) -> Unit,
) {

    ItemTabletTitle(str = "네이버 시리즈 소설", isTopPadding = false)

    TabletContentWrap {
        NaverSeriesNovelGenre.forEachIndexed { index, _ ->
            ItemMainTabletContent(
                title = "네이버 시리즈 ${getNaverSeriesGenreKor(NaverSeriesNovelGenre[index])}",
                isLast = NaverSeriesNovelGenre.size - 1 == index,
                onClick = {
                    setDetailPage(true)
                    setDetailMenu("NAVER_SERIES 베스트 리스트 ${getNaverSeriesGenre(NaverSeriesNovelGenre[index])}")
                    setDetailPlatform("NAVER_SERIES")
                    setDetailGenre(getNaverSeriesGenre(NaverSeriesNovelGenre[index]))
                    setDetailType("NOVEL")
                }
            )
        }
    }

    Spacer(modifier = Modifier.size(16.dp))

    ItemTabletTitle(str = "조아라 소설", isTopPadding = false)

    TabletContentWrap {
        JoaraGenre.forEachIndexed { index, _ ->
            ItemMainTabletContent(
                title = "조아라 ${getJoaraGenreKor(JoaraGenre[index])}",
                isLast = JoaraGenre.size - 1 == index,
                onClick = {
                    setDetailPage(true)
                    setDetailMenu("JOARA 베스트 리스트 ${getJoaraGenre(JoaraGenre[index])}")
                    setDetailPlatform("JOARA")
                    setDetailGenre(getJoaraGenre(JoaraGenre[index]))
                    setDetailType("NOVEL")
                }
            )
        }
    }

    Spacer(modifier = Modifier.size(16.dp))

    ItemTabletTitle(str = "조아라 노블레스 소설", isTopPadding = false)

    TabletContentWrap {
        JoaraGenre.forEachIndexed { index, _ ->
            ItemMainTabletContent(
                title = "조아라 노블레스 ${getJoaraGenreKor(JoaraGenre[index])}",
                isLast = JoaraGenre.size - 1 == index,
                onClick = {
                    setDetailPage(true)
                    setDetailMenu("JOARA_NOBLESS 베스트 리스트 ${getJoaraGenre(JoaraGenre[index])}")
                    setDetailPlatform("JOARA_NOBLESS")
                    setDetailGenre(getJoaraGenre(JoaraGenre[index]))
                    setDetailType("NOVEL")
                }
            )
        }
    }

    Spacer(modifier = Modifier.size(16.dp))

    ItemTabletTitle(str = "조아라 프리미엄 소설", isTopPadding = false)

    TabletContentWrap {
        JoaraGenre.forEachIndexed { index, _ ->
            ItemMainTabletContent(
                title = "조아라 프리미엄 ${getJoaraGenreKor(JoaraGenre[index])}",
                isLast = JoaraGenre.size - 1 == index,
                onClick = {
                    setDetailPage(true)
                    setDetailMenu("JOARA_PREMIUM 베스트 리스트 ${getJoaraGenre(JoaraGenre[index])}")
                    setDetailPlatform("JOARA_PREMIUM")
                    setDetailGenre(getJoaraGenre(JoaraGenre[index]))
                    setDetailType("NOVEL")
                }
            )
        }
    }

    Spacer(modifier = Modifier.size(60.dp))
}

@Composable
fun ContentsBestListComic(
    setDetailPage: (Boolean) -> Unit,
    setDetailMenu: (String) -> Unit,
    setDetailPlatform: (String) -> Unit,
    setDetailGenre: (String) -> Unit,
    setDetailType: (String) -> Unit,
) {

    ItemTabletTitle(str = "네이버 시리즈 웹툰", isTopPadding = false)

    TabletContentWrap {
        NaverSeriesComicGenre.forEachIndexed { index, _ ->
            ItemMainTabletContent(
                title = "네이버 시리즈 ${getNaverSeriesGenreKor(NaverSeriesComicGenre[index])}",
                isLast = NaverSeriesComicGenre.size - 1 == index,
                onClick = {
                    setDetailPage(true)
                    setDetailMenu("NAVER_SERIES 베스트 리스트 ${getNaverSeriesGenre(NaverSeriesComicGenre[index])}")
                    setDetailPlatform("NAVER_SERIES")
                    setDetailGenre(getNaverSeriesGenre(NaverSeriesComicGenre[index]))
                    setDetailType("COMIC")
                }
            )
        }
    }

    Spacer(modifier = Modifier.size(60.dp))
}


