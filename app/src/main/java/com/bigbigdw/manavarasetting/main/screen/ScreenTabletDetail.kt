package com.bigbigdw.manavarasetting.main.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bigbigdw.manavarasetting.main.model.ItemBookInfo
import com.bigbigdw.manavarasetting.main.model.ItemBestInfo
import com.bigbigdw.manavarasetting.main.viewModels.ViewModelMain
import com.bigbigdw.manavarasetting.ui.theme.color000000
import com.bigbigdw.manavarasetting.ui.theme.color8E8E8E
import com.bigbigdw.manavarasetting.ui.theme.colorF6F6F6
import com.bigbigdw.manavarasetting.util.WeekKor

@Composable
fun ScreenTabletDetail(
    setDetailPage: (Boolean) -> Unit,
    getDetailMenu: String,
    viewModelMain: ViewModelMain,
    getDetailPageType: String
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 0.dp, 16.dp, 0.dp)
                .background(color = colorF6F6F6)
                .verticalScroll(rememberScrollState())
                .semantics { contentDescription = "Overview Screen" },
        ) {

            Spacer(modifier = Modifier.size(16.dp))

            Text(
                modifier = Modifier
                    .padding(24.dp, 0.dp, 0.dp, 0.dp)
                    .clickable {
                        setDetailPage(false)
                    },
                text = "< $getDetailMenu",
                fontSize = 24.sp,
                color = color000000,
                fontWeight = FontWeight(weight = 700)
            )

            if (getDetailMenu.contains("베스트 리스트")) {
                ContentsBestListDetail(
                    viewModelMain = viewModelMain,
                    child = getDetailPageType,
                    type = "BEST"
                )
            } else if (getDetailMenu.contains("투데이 베스트 JSON")) {
                ContentsBestListDetail(
                    viewModelMain = viewModelMain,
                    child = getDetailPageType,
                    type = "JSON"
                )
            }  else if (getDetailMenu.contains("주간 베스트 JSON")) {
                ContentsBestListDetailWeek(
                    viewModelMain = viewModelMain,
                    child = getDetailPageType,
                    platform = "NAVER_SERIES",
                    menu = "주간",
                    type = "COMIC"
                )
            }  else if (getDetailMenu.contains("월간 베스트 JSON")) {
                ContentsBestListDetailWeek(
                    viewModelMain = viewModelMain,
                    child = getDetailPageType,
                    platform = "NAVER_SERIES",
                    menu = "월간",
                    type = "COMIC"
                )
            } else if (getDetailMenu.contains("주간 트로피")) {
                ContentsBestListJsonTrophy(
                    viewModelMain = viewModelMain,
                    child = getDetailPageType,
                    platform = "NAVER_SERIES",
                    menu = "주간",
                    type = "COMIC"
                )
            } else if (getDetailMenu.contains("월간 트로피")) {
                ContentsBestListJsonTrophy(
                    viewModelMain = viewModelMain,
                    child = getDetailPageType,
                    platform = "NAVER_SERIES",
                    menu = "월간",
                    type = "COMIC"
                )
            } else if (getDetailMenu.contains("트로피 주간")) {
                ContentsBestListDetailTrophy(
                    viewModelMain = viewModelMain,
                    child = getDetailPageType,
                    platform = "NAVER_SERIES",
                    menu = "주간",
                    type = "COMIC"
                )
            } else if (getDetailMenu.contains("트로피 월간")) {
                ContentsBestListDetailTrophy(
                    viewModelMain = viewModelMain,
                    child = getDetailPageType,
                    platform = "NAVER_SERIES",
                    menu = "월간",
                    type = "COMIC"
                )
            }
        }
    }
}

@Composable
fun ContentsBestListDetail(viewModelMain: ViewModelMain, child: String, type: String) {

    if (type == "BEST") {
        viewModelMain.getBestList(platform = "NAVER_SERIES", child = child, type = "COMIC")
    } else {
        viewModelMain.getBestJsonList(platform = "NAVER_SERIES", genre = child, type = "COMIC")
    }

    val bestList: ArrayList<ItemBookInfo> = viewModelMain.state.collectAsState().value.setBestBookList

    TabletContentWrap {
        Spacer(modifier = Modifier.size(8.dp))

        bestList.forEachIndexed { index, item ->
            ItemTabletBestList(
                item = item,
                isLast = bestList.size - 1 == index
            )
        }

        Spacer(modifier = Modifier.size(8.dp))
    }

    Spacer(modifier = Modifier.size(60.dp))
}

@Composable
fun ContentsBestListDetailWeek(platform : String, viewModelMain: ViewModelMain, child: String, type: String, menu : String) {

    viewModelMain.getBestJsonWeekList(platform = platform, genre = child, menu = menu, type = type)

    val bestWeekList: ArrayList<ArrayList<ItemBookInfo>> =
        viewModelMain.state.collectAsState().value.bestListWeek
    bestWeekList.forEachIndexed { index, itemArray ->

        ItemTabletTitle(
            str = if (menu == "주간") {
                "${WeekKor[index]}요일"
            } else {
                "${index + 1}주차"
            }
        )

        TabletContentWrap {
            Spacer(modifier = Modifier.size(8.dp))

            if (itemArray.size == 0) {
                Text(
                    text = "없음",
                    color = color000000,
                    fontSize = 18.sp,
                    fontWeight = FontWeight(weight = 500)
                )
            } else {
                Row(
                    modifier = Modifier.horizontalScroll(
                        rememberScrollState()
                    )
                ) {
                    itemArray.forEachIndexed { index, item ->
                        ItemTabletBestListVertical(
                            item = item
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.size(8.dp))
        }
    }

    Spacer(modifier = Modifier.size(60.dp))
}

@Composable
fun ContentsBestListJsonTrophy(platform : String, viewModelMain: ViewModelMain, child: String, menu: String, type: String) {

    viewModelMain.getBestJsonTrophyList(platform = platform, menu = menu, genre = child, type = type)

    val bestWeekList: ArrayList<ItemBestInfo> = viewModelMain.state.collectAsState().value.trophyList

    TabletContentWrap {
        Spacer(modifier = Modifier.size(8.dp))

        bestWeekList.forEachIndexed { index, item ->
            ItemTabletTrophyList(
                item = item,
                isLast = bestWeekList.size - 1 == index
            )
        }

        Spacer(modifier = Modifier.size(8.dp))
    }



    Spacer(modifier = Modifier.size(60.dp))
}

@Composable
fun ContentsBestListDetailTrophy(viewModelMain: ViewModelMain, menu: String, child: String, platform: String, type: String) {

    viewModelMain.getBestTrophyList(platform = platform, menu = menu, child = child, type = type)

    val bestWeekList: ArrayList<ItemBestInfo> = viewModelMain.state.collectAsState().value.trophyList

    TabletContentWrap {
        Spacer(modifier = Modifier.size(8.dp))

        bestWeekList.forEachIndexed { index, item ->
            ItemTabletTrophyList(
                item = item,
                isLast = bestWeekList.size - 1 == index
            )
        }

        Spacer(modifier = Modifier.size(8.dp))
    }

    Spacer(modifier = Modifier.size(60.dp))
}