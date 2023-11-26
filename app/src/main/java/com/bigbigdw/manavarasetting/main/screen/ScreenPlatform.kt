package com.bigbigdw.manavarasetting.main.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.work.WorkManager
import com.bigbigdw.manavarasetting.R
import com.bigbigdw.manavarasetting.main.model.ItemBookInfo
import com.bigbigdw.manavarasetting.main.viewModels.ViewModelMain
import com.bigbigdw.manavarasetting.ui.theme.color000000
import com.bigbigdw.manavarasetting.ui.theme.color20459E
import com.bigbigdw.manavarasetting.ui.theme.color8E8E8E
import com.bigbigdw.manavarasetting.ui.theme.colorF6F6F6
import com.bigbigdw.manavarasetting.util.DataStoreManager
import com.bigbigdw.manavarasetting.util.PeriodicWorker
import com.bigbigdw.manavarasetting.util.changePlatformNameKor
import com.bigbigdw.manavarasetting.util.comicListEng
import com.bigbigdw.manavarasetting.util.doMining
import com.bigbigdw.manavarasetting.util.genreListEng
import com.bigbigdw.manavarasetting.util.getBookCount
import com.bigbigdw.manavarasetting.util.getPlatformDataKeyComic
import com.bigbigdw.manavarasetting.util.getPlatformDataKeyNovel
import com.bigbigdw.manavarasetting.util.getPlatformLogoEng
import com.bigbigdw.manavarasetting.util.novelListEng
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit


@Composable
fun ScreenMainWebtoon() {

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

            MainHeader(image = R.drawable.icon_novel, title = "웹툰 관리")

            ContentsWebtoon()

        }
    }
}

@Composable
fun ScreenMainNovel() {

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

            MainHeader(image = R.drawable.icon_webtoon, title = "웹소설 관리")

            ContentsNovel()

        }
    }
}

@Composable
fun ContentsNovel() {

    val context = LocalContext.current
    val workManager = WorkManager.getInstance(context)

    TabletContentWrapBtn(
        onClick = {
            PeriodicWorker.doWorker(
                workManager = workManager,
                time = 6,
                tag = "NOVEL",
                platform = "ALL",
                type = "NOVEL"
            )
        },
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = R.drawable.logo_transparent),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "마나바라 NOVEL 전체",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {
            PeriodicWorker.doWorker(
                workManager = workManager,
                time = 6,
                timeUnit = TimeUnit.DAYS,
                tag = "BOOK",
                platform = "ALL",
                type = "NOVEL"
            )
        },
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = R.drawable.logo_transparent),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "마나바라 NOVEL BOOK 전체",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {
            PeriodicWorker.doWorker(
                workManager = workManager,
                time = 12,
                timeUnit = TimeUnit.DAYS,
                tag = "KEYWORD",
                platform = "ALL",
                type = "NOVEL"
            )
        },
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = R.drawable.logo_transparent),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "마나바라 NOVEL 키워드",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    novelListEng().forEachIndexed { index, item ->
        TabletContentWrapBtn(
            onClick = {
                PeriodicWorker.doWorker(
                    workManager = workManager,
                    time = 3,
                    tag = "MINING",
                    platform = item,
                    type = "NOVEL"
                )
            },
            content = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Image(
                        painter = painterResource(id = getPlatformLogoEng(item)),
                        contentDescription = null,
                        modifier = Modifier
                            .width(20.dp)
                            .height(20.dp)
                    )

                    Spacer(modifier = Modifier.size(8.dp))

                    Text(
                        text = "${changePlatformNameKor(item)} NOVEL",
                        color = color000000,
                        fontSize = 18.sp,
                    )
                }
            }
        )
    }

    ContentsLabs()

    Spacer(modifier = Modifier.size(60.dp))
}

@Composable
fun ContentsWebtoon() {

    val context = LocalContext.current
    val workManager = WorkManager.getInstance(context)

    TabletContentWrapBtn(
        onClick = {
            PeriodicWorker.doWorker(
                workManager = workManager,
                time = 6,
                tag = "MINING",
                platform = "NAVER_SERIES",
                type = "COMIC"
            )
        },
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_naver),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "네이버 시리즈 COMIC",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    Spacer(modifier = Modifier.size(60.dp))
}

@Composable
fun ContentsPlatform(
    platform: String,
    type: String,
    logo: Int,
    setDetailPage: (Boolean) -> Unit,
    setMenu: (String) -> Unit,
    setPlatform: (String) -> Unit,
    setType: (String) -> Unit,
) {

    val context = LocalContext.current

    TabletContentWrapBtn(
        onClick = {
            runBlocking {
                doMining(
                    platform = platform,
                    type = type,
                    yesterDayItemMap = mutableMapOf(),
                    context = context
                )
            }
        },
        content = {
            Row(

                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = logo),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "$platform $type",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {
            setDetailPage(true)
            setMenu("${changePlatformNameKor(platform)} JSON 투데이 베스트")
            setPlatform(platform)
            setType(type)
        },
        content = {
            Row(

                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_best_gr),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "베스트 투데이",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {
            setDetailPage(true)
            setMenu("${changePlatformNameKor(platform)} JSON 주간 베스트")
            setPlatform(platform)
            setType(type)
        },
        content = {
            Row(

                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_best_gr),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "베스트 주간",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {
            setDetailPage(true)
            setMenu("${changePlatformNameKor(platform)} JSON 월간 베스트")
            setPlatform(platform)
            setType(type)
        },
        content = {
            Row(

                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_best_gr),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "베스트 월간",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {
            setDetailPage(true)
            setMenu("${changePlatformNameKor(platform)} JSON 주간 트로피")
            setPlatform(platform)
            setType(type)
        },
        content = {
            Row(

                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_trophy_gr),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "트로피 주간 토탈",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {
            setDetailPage(true)
            setMenu("${changePlatformNameKor(platform)} JSON 월간 트로피")
            setPlatform(platform)
            setType(type)
        },
        content = {
            Row(

                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_trophy_gr),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "트로피 월간 토탈",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    if(genreListEng().contains(platform)){
        TabletContentWrapBtn(
            onClick = {
                setDetailPage(true)
                setMenu("${changePlatformNameKor(platform)} 투데이 장르 JSON")
                setPlatform(platform)
                setType(type)
            },
            content = {
                Row(

                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_transparent),
                        contentDescription = null,
                        modifier = Modifier
                            .width(20.dp)
                            .height(20.dp)
                    )

                    Spacer(modifier = Modifier.size(8.dp))

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "투데이 장르",
                        color = color000000,
                        fontSize = 18.sp,
                    )
                }
            }
        )

        TabletContentWrapBtn(
            onClick = {
                setDetailPage(true)
                setMenu("${changePlatformNameKor(platform)} 주간 장르 JSON")
                setPlatform(platform)
                setType(type)
            },
            content = {
                Row(

                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_transparent),
                        contentDescription = null,
                        modifier = Modifier
                            .width(20.dp)
                            .height(20.dp)
                    )

                    Spacer(modifier = Modifier.size(8.dp))

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "주간 장르",
                        color = color000000,
                        fontSize = 18.sp,
                    )
                }
            }
        )

        TabletContentWrapBtn(
            onClick = {
                setDetailPage(true)
                setMenu("${changePlatformNameKor(platform)} 월간 장르 JSON")
                setPlatform(platform)
                setType(type)
            },
            content = {
                Row(

                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_transparent),
                        contentDescription = null,
                        modifier = Modifier
                            .width(20.dp)
                            .height(20.dp)
                    )

                    Spacer(modifier = Modifier.size(8.dp))

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "월간 장르",
                        color = color000000,
                        fontSize = 18.sp,
                    )
                }
            }
        )
    }

    Spacer(modifier = Modifier.size(60.dp))
}

@Composable
fun ScreenManavaraRecord(type: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorF6F6F6),
        contentAlignment = Alignment.TopStart
    ) {
        val context = LocalContext.current
        val dataStore = DataStoreManager(context)

        Column(
            modifier = Modifier.wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if(type == "NOVEL"){

                novelListEng().forEachIndexed { index, item ->
                    TabletContentWrapBtn(
                        onClick = {},
                        content = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Image(
                                    painter = painterResource(id = getPlatformLogoEng(item)),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .width(20.dp)
                                        .height(20.dp)
                                )

                                Spacer(modifier = Modifier.size(8.dp))

                                getBookCount(context = context, type = type, platform = item)

                                Text(
                                    text = spannableString(
                                        textFront = "${changePlatformNameKor(item)} : ",
                                        color = color000000,
                                        textEnd = "${dataStore.getDataStoreString(
                                            getPlatformDataKeyNovel(item)
                                        ).collectAsState(initial = "").value ?: "0"} 작품"
                                    ),
                                    color = color20459E,
                                    fontSize = 18.sp,
                                )
                            }
                        }
                    )
                }


            } else {
                comicListEng().forEachIndexed { index, item ->
                    TabletContentWrapBtn(
                        onClick = {},
                        content = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Image(
                                    painter = painterResource(id = getPlatformLogoEng(item)),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .width(20.dp)
                                        .height(20.dp)
                                )

                                Spacer(modifier = Modifier.size(8.dp))

                                getBookCount(context = context, type = type, platform = item)

                                Text(
                                    text = spannableString(
                                        textFront = "${changePlatformNameKor(item)} : ",
                                        color = color000000,
                                        textEnd = "${dataStore.getDataStoreString(
                                            getPlatformDataKeyComic(item)
                                        ).collectAsState(initial = "").value ?: "0"} 작품"
                                    ),
                                    color = color20459E,
                                    fontSize = 18.sp,
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun BookDetail(
    viewModelMain: ViewModelMain,
    getPlatform: String,
    getType: String
) {

    LaunchedEffect(getPlatform, getType) {
        viewModelMain.getBook(platform = getPlatform, type = getType)
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
}

@Composable
fun ScreenBookItem(item : ItemBookInfo){
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