package com.bigbigdw.manavarasetting.main.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.bigbigdw.manavarasetting.util.geMonthDate
import com.bigbigdw.manavarasetting.util.getWeekDate
import com.bigbigdw.manavarasetting.util.weekList
import com.bigbigdw.manavarasetting.util.weekListAll

@Composable
fun ContentsBestListDetail(
    viewModelMain: ViewModelMain,
    type: String,
    getPlatform: String,
    getType: String
) {

    LaunchedEffect(type, getPlatform, getType){
        if (type == "BEST") {
            viewModelMain.getBestList(platform = getPlatform, type = getType)
        } else {
            viewModelMain.getBestJsonList(platform = getPlatform, type = getType)
        }
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
                ScreenBookItem(item = item)
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

    LaunchedEffect(getPlatform, getType) {
        viewModelMain.getBestJsonWeekList(platform = getPlatform, menu = menu, type = getType)
    }

    val bestWeekList: ArrayList<ArrayList<ItemBookInfo>> =
        viewModelMain.state.collectAsState().value.bestListWeek

    val (getDate, setDate) = remember { mutableStateOf("일요일") }

    Column(modifier = Modifier.background(color = colorF6F6F6)) {

        LazyRow(
            modifier = Modifier.padding(0.dp, 8.dp, 0.dp, 8.dp),
        ) {
            itemsIndexed(weekList()) { index, item ->
                Box(modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)) {
                    ScreenItemKeyword(
                        getter = getDate,
                        setter = setDate,
                        title = item,
                        getValue = item
                    )
                }
            }
        }

        if (bestWeekList.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .background(colorF6F6F6)
                    .padding(0.dp, 0.dp, 16.dp, 0.dp)
            ) {

                if (bestWeekList[getWeekDate(getDate)].size > 0) {
                    itemsIndexed(bestWeekList[getWeekDate(getDate)]) { index, item ->
                        ScreenBookItem(item = item)
                    }
                } else {
                    item {
                        Box(modifier = Modifier
                            .height(500.dp)
                            .fillMaxWidth()) {
                            ScreenEmpty(str = "데이터가 없습니다")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ContentsBestListDetailMonth(
    viewModelMain: ViewModelMain,
    menu: String,
    getPlatform: String,
    getType: String
) {

    LaunchedEffect(getPlatform, getType) {
        viewModelMain.getBestJsonWeekList(platform = getPlatform, menu = menu, type = getType)
    }

    val bestWeekList: ArrayList<ArrayList<ItemBookInfo>> =
        viewModelMain.state.collectAsState().value.bestListWeek

    val (getDate, setDate) = remember { mutableStateOf("1주차") }

    val arrayList = ArrayList<String>()

    var count = 0

    for (item in bestWeekList) {
        count += 1
        arrayList.add("${count}주차")
    }

    Column(modifier = Modifier.background(color = colorF6F6F6)) {

        LazyRow(
            modifier = Modifier.padding(0.dp, 8.dp, 0.dp, 8.dp),
        ) {
            itemsIndexed(arrayList) { index, item ->
                Box(modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)) {
                    ScreenItemKeyword(
                        getter = getDate,
                        setter = setDate,
                        title = item,
                        getValue = item
                    )
                }
            }
        }

        if (bestWeekList.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .background(colorF6F6F6)
                    .padding(0.dp, 0.dp, 16.dp, 0.dp)
            ) {

                if (bestWeekList[geMonthDate(getDate)].size > 0) {
                    itemsIndexed(bestWeekList[geMonthDate(getDate)]) { index, item ->

                        if (item.bookCode.isNotEmpty()) {
                            Text(
                                modifier = Modifier.padding(16.dp, 8.dp),
                                text = weekList()[index],
                                fontSize = 16.sp,
                                color = color8E8E8E,
                                fontWeight = FontWeight(weight = 700)
                            )

                            ScreenBookItem(item = item)
                        }
                    }
                } else {
                    item {
                        Box(modifier = Modifier
                            .height(500.dp)
                            .fillMaxWidth()) {
                            ScreenEmpty(str = "데이터가 없습니다")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ContentsBestListJsonTrophy(
    viewModelMain: ViewModelMain,
    menu: String,
    getPlatform: String,
    getType: String
) {

    LaunchedEffect(getPlatform, getType, menu){
        viewModelMain.getBestJsonTrophyList(platform = getPlatform, menu = menu, type = getType)
    }

    val bestWeekList: ArrayList<ItemBestInfo> =
        viewModelMain.state.collectAsState().value.trophyList

    TabletContentWrap {
        Spacer(modifier = Modifier.size(8.dp))

        LazyColumn {
            itemsIndexed(bestWeekList) { index, item ->
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

    LaunchedEffect(getPlatform, getType, menu){
        viewModelMain.getBestTrophyList(platform = getPlatform, menu = menu, type = getType)
    }

    val bestWeekList: ArrayList<ItemBestInfo> =
        viewModelMain.state.collectAsState().value.trophyList

    TabletContentWrap {
        Spacer(modifier = Modifier.size(8.dp))

        LazyColumn {
            itemsIndexed(bestWeekList) { index, item ->
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