package com.bigbigdw.manavarasetting.main.screen

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bigbigdw.manavarasetting.main.model.ItemBookInfo
import com.bigbigdw.manavarasetting.main.model.ItemBestInfo
import com.bigbigdw.manavarasetting.main.viewModels.ViewModelMain
import com.bigbigdw.manavarasetting.ui.theme.color000000
import com.bigbigdw.manavarasetting.util.WeekKor

@Composable
fun ContentsBestListDetail(
    viewModelMain: ViewModelMain,
    type: String,
    getDetailPlatform: String,
    getDetailType: String
) {

    if (type == "BEST") {
        viewModelMain.getBestList(platform = getDetailPlatform, type = getDetailType)
    } else {
        viewModelMain.getBestJsonList(platform = getDetailPlatform, type = getDetailType)
    }

    val bestList: ArrayList<ItemBookInfo> = viewModelMain.state.collectAsState().value.setBestBookList

    Spacer(modifier = Modifier.size(16.dp))

    TabletContentWrap {
        Spacer(modifier = Modifier.size(8.dp))

        LazyColumn{
            itemsIndexed(bestList){ index, item ->
                ItemTabletBestList(
                    item = item,
                    isLast = bestList.size - 1 == index
                )
            }
        }

        Spacer(modifier = Modifier.size(8.dp))
    }

    Spacer(modifier = Modifier.size(60.dp))
}

@Composable
fun ContentsBestListDetailWeek(
    viewModelMain: ViewModelMain,
    menu: String,
    getDetailPlatform: String,
    getDetailType: String
) {

    viewModelMain.getBestJsonWeekList(platform = getDetailPlatform, menu = menu, type = getDetailType)

    val bestWeekList: ArrayList<ArrayList<ItemBookInfo>> =
        viewModelMain.state.collectAsState().value.bestListWeek


    LazyColumn{
        itemsIndexed(bestWeekList){ index, item ->
            ItemTabletTitle(
                str = if (menu == "주간") {
                    "${WeekKor[index]}요일"
                } else {
                    "${index + 1}주차"
                }
            )

            TabletContentWrap {
                Spacer(modifier = Modifier.size(8.dp))

                if (item.size == 0) {
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
                        item.forEachIndexed { index, item ->
                            ItemTabletBestListVertical(
                                item = item
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.size(8.dp))
            }
        }
    }

    Spacer(modifier = Modifier.size(60.dp))
}

@Composable
fun ContentsBestListJsonTrophy(
    viewModelMain: ViewModelMain,
    menu: String,
    getDetailPlatform: String,
    getDetailType: String
) {

    viewModelMain.getBestJsonTrophyList(platform = getDetailPlatform, menu = menu, type = getDetailType)

    val bestWeekList: ArrayList<ItemBestInfo> = viewModelMain.state.collectAsState().value.trophyList

    TabletContentWrap {
        Spacer(modifier = Modifier.size(8.dp))

        LazyColumn{
            itemsIndexed(bestWeekList){ index, item ->
                ItemTabletTrophyList(
                    item = item,
                    isLast = bestWeekList.size - 1 == index
                )
            }
        }

        Spacer(modifier = Modifier.size(8.dp))
    }



    Spacer(modifier = Modifier.size(60.dp))
}

@Composable
fun ContentsBestListDetailTrophy(
    viewModelMain: ViewModelMain,
    menu: String,
    getDetailPlatform: String,
    getDetailType: String
) {

    viewModelMain.getBestTrophyList(platform = getDetailPlatform, menu = menu, type = getDetailType)

    val bestWeekList: ArrayList<ItemBestInfo> = viewModelMain.state.collectAsState().value.trophyList

    TabletContentWrap {
        Spacer(modifier = Modifier.size(8.dp))

        LazyColumn{
            itemsIndexed(bestWeekList){ index, item ->
                ItemTabletTrophyList(
                    item = item,
                    isLast = bestWeekList.size - 1 == index
                )
            }
        }

        Spacer(modifier = Modifier.size(8.dp))
    }

    Spacer(modifier = Modifier.size(60.dp))
}