package com.bigbigdw.manavarasetting.main.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bigbigdw.manavarasetting.R
import com.bigbigdw.manavarasetting.main.model.ItemBestKeyword
import com.bigbigdw.manavarasetting.main.viewModels.ViewModelMain
import com.bigbigdw.manavarasetting.ui.theme.color000000
import com.bigbigdw.manavarasetting.ui.theme.color1CE3EE
import com.bigbigdw.manavarasetting.ui.theme.color20459E

@Composable
fun ContentsGenre(
    menuType: String,
    setDetailPage: (Boolean) -> Unit,
    setDetailMenu: (String) -> Unit,
    setDetailPlatform: (String) -> Unit,
    setDetailType: (String) -> Unit,
) {

    TabletContentWrapBtn(
        onClick = {
            setDetailPage(true)
            setDetailMenu("조아라 $menuType")
            setDetailPlatform("JOARA")
            setDetailType("NOVEL")
        },
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_joara),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "조아라",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {
            setDetailPage(true)
            setDetailMenu("조아라 프리미엄 장르 $menuType")
            setDetailPlatform("JOARA_PREMIUM")
            setDetailType("NOVEL")
        },
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_joara_premium),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "조아라 프리미엄",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {
            setDetailPage(true)
            setDetailMenu("조아라 노블레스 장르 $menuType")
            setDetailPlatform("JOARA_NOBLESS")
            setDetailType("NOVEL")
        },
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_joara_nobless),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "조아라 노블레스",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {
            setDetailPage(true)
            setDetailMenu("리디 판타지 장르 $menuType")
            setDetailPlatform("RIDI_FANTAGY")
            setDetailType("NOVEL")
        },
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_ridibooks),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "리디 판타지",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )
    TabletContentWrapBtn(
        onClick = {
            setDetailPage(true)
            setDetailMenu("리디 로맨스 장르 $menuType")
            setDetailPlatform("RIDI_ROMANCE")
            setDetailType("NOVEL")
        },
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_ridibooks),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "리디 로맨스",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {
            setDetailPage(true)
            setDetailMenu("카카오 스테이지 장르 $menuType")
            setDetailPlatform("KAKAO_STAGE")
            setDetailType("NOVEL")
        },
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_kakaostage),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "카카오 스테이지",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {
            setDetailPage(true)
            setDetailMenu("문피아 유료 장르 $menuType")
            setDetailPlatform("MUNPIA_PAY")
            setDetailType("NOVEL")
        },
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_munpia),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "문피아 유료",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {
            setDetailPage(true)
            setDetailMenu("문피아 무료 장르 $menuType")
            setDetailPlatform("MUNPIA_FREE")
            setDetailType("NOVEL")
        },
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_munpia),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "문피아 무료",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {
            setDetailPage(true)
            setDetailMenu("톡소다 장르 $menuType")
            setDetailPlatform("TOKSODA")
            setDetailType("NOVEL")
        },
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_toksoda),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "톡소다",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {
            setDetailPage(true)
            setDetailMenu("톡소다 자유연재 장르 $menuType")
            setDetailPlatform("TOKSODA_FREE")
            setDetailType("NOVEL")
        },
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_toksoda),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "톡소다 자유연재",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    Spacer(modifier = Modifier.size(60.dp))
}

@Composable
fun GenreDetail(
    viewModelMain: ViewModelMain,
    getDetailPlatform: String,
    getDetailType: String,
    menuType: String
) {

    val state = viewModelMain.state.collectAsState().value

    when (menuType) {
        "투데이" -> {
            viewModelMain.getGenreDay(platform = getDetailPlatform, type = getDetailType)
        }

        "주간" -> {
            viewModelMain.getGenreDayWeek(platform = getDetailPlatform, type = getDetailType)
        }

        else -> {
            viewModelMain.getGenreDayWeek(platform = getDetailPlatform, type = getDetailType)
        }
    }

    val bestList: ArrayList<ItemBestKeyword> = state.genreDay

    Spacer(modifier = Modifier.size(8.dp))

    LazyColumn {
        itemsIndexed(bestList) { index, item ->
            ListGenreToday(
                itemBestKeyword = item,
                index = index
            )
        }
    }

    Spacer(modifier = Modifier.size(60.dp))
}

@Composable
fun GenreDetailJson(
    viewModelMain: ViewModelMain,
    getDetailPlatform: String,
    getDetailType: String,
    menuType: String
) {

    Log.d("HIHI", "REPEAT")
    viewModelMain.getJsonGenreList(platform = getDetailPlatform, type = getDetailType)

//    when (menuType) {
//        "투데이" -> {
//            viewModelMain.getJsonGenreList(platform = getDetailPlatform, type = getDetailType)
//        }
//
//        "주간" -> {
//            viewModelMain.getJsonGenreWeekList(platform = getDetailPlatform, type = getDetailType, menu = menuType)
//        }
//
//        else -> {
//            viewModelMain.getJsonGenreWeekList(platform = getDetailPlatform, type = getDetailType, menu = menuType)
//        }
//    }

    val bestList: ArrayList<ItemBestKeyword> = viewModelMain.state.collectAsState().value.genreDay

    Spacer(modifier = Modifier.size(8.dp))

    LazyColumn {
        itemsIndexed(bestList) { index, item ->
            ListGenreToday(
                itemBestKeyword = item,
                index = index
            )
        }
    }

    Spacer(modifier = Modifier.size(60.dp))
}



@Composable
fun ListGenreToday(
    itemBestKeyword: ItemBestKeyword,
    index: Int,
) {

    Row(
        Modifier
            .fillMaxWidth()
            .padding(12.dp, 4.dp),
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
                        text = itemBestKeyword.title,
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

                    Text(
                        text = itemBestKeyword.value,
                        modifier = Modifier
                            .wrapContentHeight()
                            .padding(0.dp, 0.dp, 16.dp, 0.dp)
                            .wrapContentSize(),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Left,
                        color = color1CE3EE,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            })
    }
}