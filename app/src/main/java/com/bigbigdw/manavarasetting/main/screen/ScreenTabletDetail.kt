package com.bigbigdw.manavarasetting.main.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun ContentsBestListDetail(
    viewModelMain: ViewModelMain,
    type: String,
    getPlatform: String,
    getType: String
) {

    if (type == "BEST") {
        viewModelMain.getBestList(platform = getPlatform, type = getType)
    } else {
        viewModelMain.getBestJsonList(platform = getPlatform, type = getType)
    }

    val bestList: ArrayList<ItemBookInfo> = viewModelMain.state.collectAsState().value.bestBookList

    Spacer(modifier = Modifier.size(8.dp))

    Column(modifier = Modifier.background(color = colorF6F6F6)) {

        LazyColumn(
            modifier = Modifier
                .background(colorF6F6F6)
                .padding(0.dp, 0.dp, 16.dp, 0.dp)
        ) {

            itemsIndexed(bestList) { index, item ->
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    contentPadding = PaddingValues(
                        start = 0.dp,
                        top = 0.dp,
                        end = 0.dp,
                        bottom = 0.dp,
                    ),
                    shape = RoundedCornerShape(20.dp),
                    onClick = {},
                    content = {
                        Column(
                            modifier = Modifier
                                .padding(24.dp, 12.dp)
                        ) {

                            ScreenItemBestCard(item = item)

                            Spacer(modifier = Modifier.size(8.dp))

                            ScreenItemBestCount(item = item)

                            if (item.intro.isNotEmpty()) {

                                Spacer(modifier = Modifier.size(8.dp))

                                Text(
                                    text = item.intro,
                                    color = color8E8E8E,
                                    fontSize = 16.sp,
                                )
                            }
                        }
                    })

                Spacer(modifier = Modifier.size(16.dp))
            }
        }
    }

    Spacer(modifier = Modifier.size(60.dp))
}

@Composable
fun ContentsBestListDetailWeek(
    viewModelMain: ViewModelMain,
    menu: String,
    getPlatform: String,
    getType: String
) {

    viewModelMain.getBestJsonWeekList(platform = getPlatform, menu = menu, type = getType)

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
    getPlatform: String,
    getType: String
) {

    viewModelMain.getBestJsonTrophyList(platform = getPlatform, menu = menu, type = getType)

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
    getPlatform: String,
    getType: String
) {

    viewModelMain.getBestTrophyList(platform = getPlatform, menu = menu, type = getType)

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