package com.bigbigdw.manavarasetting.main.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bigbigdw.manavarasetting.main.model.ItemBestInfo
import com.bigbigdw.manavarasetting.main.model.ItemKeyword
import com.bigbigdw.manavarasetting.main.viewModels.ViewModelMain
import com.bigbigdw.manavarasetting.ui.theme.color1CE3EE
import com.bigbigdw.manavarasetting.ui.theme.color20459E
import com.bigbigdw.manavarasetting.ui.theme.color8E8E8E
import com.bigbigdw.manavarasetting.ui.theme.colorF6F6F6
import com.bigbigdw.manavarasetting.util.DBDate
import com.bigbigdw.manavarasetting.util.getJsonKeywordList
import com.bigbigdw.manavarasetting.util.getJsonKeywordMonthList
import com.bigbigdw.manavarasetting.util.getJsonKeywordWeekList
import com.bigbigdw.manavarasetting.util.getWeekDate
import com.bigbigdw.manavarasetting.util.weekListAll
import java.util.Collections

@Composable
fun KeywordDetailJson(
    viewModelMain: ViewModelMain,
    getPlatform: String,
    getType: String,
    menuType: String
) {

    LaunchedEffect(menuType, getPlatform, getType) {
        when (menuType) {
            "투데이" -> {
                getJsonKeywordList(
                    platform = getPlatform,
                    type = getType
                ) {
                    viewModelMain.setKeywordDay(it)
                }
            }

            "주간" -> {
                getJsonKeywordWeekList(
                    platform = getPlatform,
                    type = getType
                ) { keywordDayList, keywordDay ->
                    viewModelMain.setKeywordWeek(
                        keywordDayList = keywordDayList,
                        keywordDay = keywordDay
                    )
                }
            }

            else -> {
                getJsonKeywordMonthList(
                    platform = getPlatform,
                    type = getType,
                ) { keywordDayList, keywordDay ->
                    viewModelMain.setKeywordWeek(
                        keywordDayList = keywordDayList,
                        keywordDay = keywordDay
                    )
                }
            }
        }
    }

    val state = viewModelMain.state.collectAsState().value

    Spacer(modifier = Modifier.size(8.dp))

    when (menuType) {
        "투데이" -> {

            val cmpAsc: java.util.Comparator<ItemKeyword> =
                Comparator { o1, o2 ->
                    (o2.value.split("\\s+".toRegex()).count { it.isNotEmpty() })
                    .compareTo(
                        (o1.value.split("\\s+".toRegex()).count { it.isNotEmpty() })
                    )
                }
            Collections.sort(state.keywordDay, cmpAsc)

            LazyColumn {
                itemsIndexed(state.keywordDay) { index, item ->
                    ListKeywordToday(
                        itemBestKeyword = item,
                        index = index
                    )
                }
            }
        }
        "주간" -> {

            val (getDate, setDate) = remember { mutableStateOf("전체") }

            Column(modifier = Modifier.background(color = colorF6F6F6)) {
                LazyRow(
                    modifier =  Modifier.padding(16.dp, 8.dp, 0.dp, 8.dp),
                ) {
                    itemsIndexed(weekListAll()) { index, item ->
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

                if (getDate == "전체") {
                    LazyColumn(
                        modifier = Modifier
                            .background(colorF6F6F6)
                            .padding(16.dp, 0.dp, 16.dp, 0.dp)
                    ) {

                        val cmpAsc: java.util.Comparator<ItemKeyword> =
                            Comparator { o1, o2 ->
                                (o2.value.split("\\s+".toRegex()).count { it.isNotEmpty() })
                                    .compareTo(
                                        (o1.value.split("\\s+".toRegex()).count { it.isNotEmpty() })
                                    )
                            }
                        Collections.sort(state.keywordDay, cmpAsc)

                        itemsIndexed(state.keywordDay) { index, item ->
                            ListKeywordToday(
                                itemBestKeyword = item,
                                index = index
                            )
                        }
                    }
                } else {

                    if(state.keywordDayList[getWeekDate(getDate)].size > 0){
                        LazyColumn(
                            modifier = Modifier
                                .background(colorF6F6F6)
                        ) {

                            itemsIndexed(state.keywordDayList[getWeekDate(getDate)]) { index, item ->

                                val cmpAsc: java.util.Comparator<ItemKeyword> =
                                    Comparator { o1, o2 ->
                                        (o2.value.split("\\s+".toRegex()).count { it.isNotEmpty() })
                                            .compareTo(
                                                (o1.value.split("\\s+".toRegex()).count { it.isNotEmpty() })
                                            )
                                    }
                                Collections.sort(state.keywordDayList[getWeekDate(getDate)], cmpAsc)

                                ListKeywordToday(
                                    itemBestKeyword = item,
                                    index = index
                                )
                            }
                        }
                    } else {
                        ScreenEmpty(str = "데이터가 없습니다")
                    }
                }
            }
        }
        else -> {
            val (getDate, setDate) = remember { mutableStateOf("전체") }

            val arrayList = ArrayList<String>()
            arrayList.add("전체")

            var count = 0

            for(item in state.keywordDayList){
                count += 1
                arrayList.add("${count}일")
            }

            Column(modifier = Modifier.background(color = colorF6F6F6)) {
                LazyRow(
                    modifier =  Modifier.padding(16.dp, 8.dp, 0.dp, 8.dp),
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

                    item { Spacer(modifier = Modifier.size(60.dp)) }
                }

                if (getDate == "전체") {
                    LazyColumn(
                        modifier = Modifier
                            .background(colorF6F6F6)
                    ) {

                        item{
                            Button(
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                onClick = {  },
                                modifier = Modifier
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                content = {

                                    Column {

                                        Text(
                                            modifier = Modifier.fillMaxWidth(),
                                            text = "전체 키워드는 장르 누적 순위로 표시됩니다.",
                                            color = color8E8E8E,
                                            fontSize = 16.sp,
                                        )

                                        Text(
                                            modifier = Modifier.fillMaxWidth(),
                                            text = "각 일별 순위는 1위부터 5위까지 표시됩니다. 일별 탭에서 모든 순위를 확인 할 수 있습니다.",
                                            color = color8E8E8E,
                                            fontSize = 16.sp,
                                        )

                                    }
                                }
                            )
                        }

                        item { ItemTabletTitle(str = "${DBDate.month()}월 전체", isTopPadding = false) }

                        val cmpAsc: java.util.Comparator<ItemKeyword> =
                            Comparator { o1, o2 ->
                                (o2.value.split("\\s+".toRegex()).count { it.isNotEmpty() })
                                    .compareTo(
                                        (o1.value.split("\\s+".toRegex()).count { it.isNotEmpty() })
                                    )
                            }
                        Collections.sort(state.keywordDay, cmpAsc)

                        itemsIndexed(state.keywordDay) { index, item ->

                            if(index < 10){
                                ListKeywordToday(
                                    itemBestKeyword = item,
                                    index = index
                                )
                            }
                        }

                        itemsIndexed(state.keywordDayList) { index, item ->

                            val cmpAsc: java.util.Comparator<ItemKeyword> =
                                Comparator { o1, o2 ->
                                    (o2.value.split("\\s+".toRegex()).count { it.isNotEmpty() })
                                        .compareTo(
                                            (o1.value.split("\\s+".toRegex()).count { it.isNotEmpty() })
                                        )
                                }
                            Collections.sort(item, cmpAsc)

                            if(item.size > 0){

                                ItemTabletTitle(str = "${DBDate.month()}월 ${index + 1}일")

                                item.forEachIndexed{ innerIndex, innnerItem ->

                                    if(innerIndex < 10){
                                        ListKeywordToday(
                                            itemBestKeyword = innnerItem,
                                            index = innerIndex,
                                            changeColor = color8E8E8E
                                        )
                                    }
                                }
                            }
                        }

                        item { Spacer(modifier = Modifier.size(60.dp)) }
                    }
                } else {

                    if(state.keywordDayList[getDate.replace("일","").toInt() - 1].size > 0){
                        LazyColumn(
                            modifier = Modifier
                                .background(colorF6F6F6)
                        ) {

                            itemsIndexed(state.keywordDayList[getDate.replace("일","").toInt() - 1]) { index, item ->
                                ListKeywordToday(
                                    itemBestKeyword = item,
                                    index = index
                                )
                            }

                            item { Spacer(modifier = Modifier.size(60.dp)) }
                        }
                    } else {
                        ScreenEmpty(str = "데이터가 없습니다")
                    }
                }
            }
        }
    }
}

@Composable
fun ListKeywordToday(
    itemBestKeyword: ItemKeyword,
    index: Int,
    changeColor : Color? = color1CE3EE
) {

    Row(
        Modifier
            .fillMaxWidth()
            .padding(0.dp, 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Button(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp, 0.dp, 0.dp, 0.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            onClick = {},
            contentPadding = PaddingValues(
                start = 0.dp,
                top = 0.dp,
                end = 0.dp,
                bottom = 0.dp,
            ),
            content = {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "${index + 1} ",
                        modifier = Modifier
                            .wrapContentHeight()
                            .padding(16.dp, 0.dp, 0.dp, 0.dp),
                        fontSize = 24.sp,
                        textAlign = TextAlign.Left,
                        color = color20459E,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        maxLines = 1,
                        text = itemBestKeyword.key,
                        modifier = Modifier
                            .wrapContentHeight()
                            .weight(1f),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Left,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    val wordCount =
                        itemBestKeyword.value.split("\\s+".toRegex()).count { it.isNotEmpty() }

                    Text(
                        text = wordCount.toString(),
                        modifier = Modifier
                            .wrapContentHeight()
                            .padding(0.dp, 0.dp, 16.dp, 0.dp)
                            .wrapContentSize(),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Left,
                        color = changeColor ?: color1CE3EE,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            })
    }
}