package com.bigbigdw.manavarasetting.main.screen

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bigbigdw.manavarasetting.R
import com.bigbigdw.manavarasetting.firebase.FCMAlert
import com.bigbigdw.manavarasetting.main.model.ItemBookInfo
import com.bigbigdw.manavarasetting.main.model.ItemBestInfo
import com.bigbigdw.manavarasetting.ui.theme.color000000
import com.bigbigdw.manavarasetting.ui.theme.color20459E
import com.bigbigdw.manavarasetting.ui.theme.color8E8E8E
import com.bigbigdw.manavarasetting.ui.theme.colorA7ACB7
import com.bigbigdw.manavarasetting.ui.theme.colorDCDCDD
import com.bigbigdw.manavarasetting.ui.theme.colorEDE6FD
import com.bigbigdw.manavarasetting.ui.theme.colorE9E9E9
import com.bigbigdw.manavarasetting.ui.theme.colorF6F6F6
import com.bigbigdw.manavarasetting.ui.theme.colorF7F7F7
import com.bigbigdw.manavarasetting.util.DBDate

@Composable
fun BackOnPressed() {
    val context = LocalContext.current
    var backPressedState by remember { mutableStateOf(true) }
    var backPressedTime = 0L

    BackHandler(enabled = backPressedState) {
        if (System.currentTimeMillis() - backPressedTime <= 400L) {
            // 앱 종료
            (context as Activity).finish()
        } else {
            backPressedState = true
            Toast.makeText(context, "한 번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .semantics { contentDescription = "Overview Screen" },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher),
                contentDescription = null,
                modifier = Modifier
                    .width(72.dp)
                    .height(72.dp)
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )
            Text(
                text = "로딩 중...",
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                color = colorA7ACB7,
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )
            CircularProgressIndicator()
        }

    }
}

@Composable
fun ScreenTest() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Text(
            text = "SETTING",
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun ItemMainSetting(
    image: Int,
    titleWorker: String,
    valueWorker: String,
    statusTitle: String,
    valueStatus: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp, 0.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            contentScale = ContentScale.FillWidth,
            painter = painterResource(id = image),
            contentDescription = null,
            modifier = Modifier
                .height(16.dp)
                .width(16.dp)
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = titleWorker,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = color000000,
        )
        Text(
            text = valueWorker,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = color20459E,
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp, 0.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            contentScale = ContentScale.FillWidth,
            painter = painterResource(id = image),
            contentDescription = null,
            modifier = Modifier
                .height(16.dp)
                .width(16.dp)
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = statusTitle,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
        )
        Text(
            text = valueStatus,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = color20459E,
        )
    }
}

@Composable
fun BtnMobile(func: () -> Unit, btnText: String) {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(22.dp)
    )
    Button(
        colors = ButtonDefaults.buttonColors(containerColor = color20459E),
        onClick = func,
        modifier = Modifier
            .width(260.dp)
            .height(56.dp),
        shape = RoundedCornerShape(50.dp)

    ) {
        Text(
            text = btnText,
            textAlign = TextAlign.Center,
            color = colorEDE6FD,
            fontSize = 16.sp,
        )
    }
}

@Composable
fun ItemMainTabletContent(
    title: String,
    value: String = "",
    isLast: Boolean,
    onClick : () -> Unit = {}
) {

    Column {
        Button(
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            contentPadding = PaddingValues(
                start = 0.dp,
                top = 6.dp,
                end = 0.dp,
                bottom = 6.dp,
            ),
            shape = RoundedCornerShape(0.dp),
            onClick = { onClick() },
            content = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = title,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        color = color000000,
                        fontWeight = FontWeight(weight = 400)
                    )
                    Text(
                        text = value,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        color = color20459E,
                    )
                }
            })

        if(!isLast){
            Spacer(modifier = Modifier.size(2.dp))
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(color = colorE9E9E9))
            Spacer(modifier = Modifier.size(2.dp))
        }
    }
}

@Composable
fun ItemMainSettingSingleTablet(
    containerColor: Color,
    image: Int,
    title: String,
    body: String,
    setter: (String) -> Unit,
    getter: String,
    onClick: () -> Unit,
    value: String
) {

    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = if (getter == value) {
                colorE9E9E9
            } else {
                colorF7F7F7
            }
        ),
        shape = RoundedCornerShape(50.dp),
        onClick = {
            setter(value)
            onClick()
        },
        contentPadding = PaddingValues(
            start = 12.dp,
            top = 6.dp,
            end = 12.dp,
            bottom = 6.dp,
        ),
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 8.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                androidx.compose.material3.Card(
                    modifier = Modifier
                        .wrapContentSize(),
                    colors = CardDefaults.cardColors(containerColor = containerColor),
                    shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .height(36.dp)
                            .width(36.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            contentScale = ContentScale.FillWidth,
                            painter = painterResource(id = image),
                            contentDescription = null,
                            modifier = Modifier
                                .height(28.dp)
                                .width(28.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.size(16.dp))

                Column {
                    Row {
                        Text(
                            text = title,
                            fontSize = 16.sp,
                            color = color000000,

                            fontWeight = FontWeight(weight = 500)
                        )
                    }

                    Row {
                        Text(
                            text = body,
                            fontSize = 14.sp,
                            color = color8E8E8E,
                        )
                    }
                }
            }
        })
}

@Composable
fun TabletBorderLine(){
    Spacer(modifier = Modifier.size(8.dp))
    Spacer(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
        .background(color = color8E8E8E))
    Spacer(modifier = Modifier.size(8.dp))
}

@Composable
fun TabletContentWrap(content: @Composable () -> Unit){
    Card(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Color.White,
        shape = RoundedCornerShape(20.dp),
        elevation = 0.dp
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp, 4.dp)
        ) {
            Spacer(modifier = Modifier.size(4.dp))

            content()

            Spacer(modifier = Modifier.size(4.dp))
        }
    }
}

@Composable
fun TabletContentWrapBtn(content: @Composable () -> Unit, onClick: () -> Unit, isContinue: Boolean = true){

    Button(
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp),
        shape = RoundedCornerShape(50.dp),
        content = {
            content()
        })

    if(isContinue){
        Spacer(modifier = Modifier.size(16.dp))
    }
}

@Composable
fun ItemTabletFCMList(item : FCMAlert, isLast: Boolean){

    val today = DBDate.dateMMDD()

    val year = item.date.substring(0,4)
    val month = item.date.substring(4,6)
    val day = item.date.substring(6,8)
    val hour = item.date.substring(8,10)
    val min = item.date.substring(10,12)
    val sec = if(item.date.length > 12){
        item.date.substring(12,14)
    } else {
        ""
    }

    Column {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.background(color = if(item.date.contains(today)){
                    colorE9E9E9
                } else {
                    Color.Transparent
                }),
                text = "${year}.${month}.${day} ${hour}:${min}:${sec}",
                color = color000000,
                fontSize = 18.sp,
            )
        }

        Spacer(modifier = Modifier.size(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.title,
                color = color8E8E8E,
                fontSize = 16.sp,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.body,
                color = color8E8E8E,
                fontSize = 16.sp,
            )
        }

        if(!isLast){
            Spacer(modifier = Modifier.size(2.dp))
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(color = colorE9E9E9))
            Spacer(modifier = Modifier.size(2.dp))
        }
    }
}

@Composable
fun ItemTabletBestList(item : ItemBookInfo, isLast: Boolean){

    Column {

        Spacer(modifier = Modifier.size(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically){
            AsyncImage(
                model = item.bookImg,
                contentDescription = null,
                modifier = Modifier
                    .height(220.dp)
                    .widthIn(0.dp, 220.dp)
            )

            Spacer(modifier = Modifier.size(16.dp))

            Column {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.title,
                        color = color000000,
                        fontSize = 18.sp,
                        fontWeight = FontWeight(weight = 500)
                    )
                }

                Spacer(modifier = Modifier.size(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = spannableString(textFront = "작가명 : ", color = color000000, textEnd = item.writer),
                        color = color8E8E8E,
                        fontSize = 16.sp,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = spannableString(textFront = "북코드 : ", color = color000000, textEnd = item.bookCode),
                        color = color8E8E8E,
                        fontSize = 16.sp,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = spannableString(textFront = "헌재 스코어 : ", color = color000000, textEnd = item.number.toString()),
                        color = color8E8E8E,
                        fontSize = 16.sp,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = spannableString(textFront = "타입 : ", color = color000000, textEnd = item.type),
                        color = color8E8E8E,
                        fontSize = 16.sp,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = spannableString(textFront = "조회 수 : ", color = color000000, textEnd = item.cntPageRead),
                        color = color8E8E8E,
                        fontSize = 16.sp,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = spannableString(textFront = "선호작 수 : ", color = color000000, textEnd = item.cntFavorite),
                        color = color8E8E8E,
                        fontSize = 16.sp,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = spannableString(textFront = "추천 수 : ", color = color000000, textEnd = item.cntRecom),
                        color = color8E8E8E,
                        fontSize = 16.sp,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = spannableString(textFront = "댓글 수 : ", color = color000000, textEnd = item.cntTotalComment),
                        color = color8E8E8E,
                        fontSize = 16.sp,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = spannableString(textFront = "회차 수 : ", color = color000000, textEnd = item.cntChapter),
                        color = color8E8E8E,
                        fontSize = 16.sp,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = spannableString(textFront = "스토리 : ", color = color000000, textEnd = item.intro),
                        color = color8E8E8E,
                        fontSize = 16.sp,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = spannableString(textFront = "점수 : ", color = color000000, textEnd = item.point.toString()),
                        color = color8E8E8E,
                        fontSize = 16.sp,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = spannableString(textFront = "베스트 총합 : ", color = color000000, textEnd = item.total.toString()),
                        color = color8E8E8E,
                        fontSize = 16.sp,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = spannableString(textFront = "베스트 총합 카운트 : ", color = color000000, textEnd = item.totalCount.toString()),
                        color = color8E8E8E,
                        fontSize = 16.sp,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = spannableString(textFront = "베스트 주간 :", color = color000000, textEnd = item.totalWeek.toString()),
                        color = color8E8E8E,
                        fontSize = 16.sp,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = spannableString(textFront = "베스트 주간 카운트 : ", color = color000000, textEnd = item.totalWeekCount.toString()),
                        color = color8E8E8E,
                        fontSize = 16.sp,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = spannableString(textFront = "베스트 월간 : ", color = color000000, textEnd = item.totalMonth.toString()),
                        color = color8E8E8E,
                        fontSize = 16.sp,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = spannableString(textFront = "베스트 월간 카운트 : ", color = color000000, textEnd = item.totalMonthCount.toString()),
                        color = color8E8E8E,
                        fontSize = 16.sp,
                    )
                }
            }
        }

        if(!isLast){
            Spacer(modifier = Modifier.size(8.dp))
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(color = colorE9E9E9))
            Spacer(modifier = Modifier.size(2.dp))
        }
    }
}

@Composable
fun ItemTabletBestListVertical(item: ItemBookInfo){

    Column(modifier = Modifier
        .padding(8.dp)
        .widthIn(0.dp, 220.dp)){

        AsyncImage(
            model = item.bookImg,
            contentDescription = null,
            modifier = Modifier
                .height(220.dp)
                .widthIn(0.dp, 220.dp)
        )

        Text(
            text = item.title,
            color = color000000,
            fontSize = 18.sp,
            fontWeight = FontWeight(weight = 500)
        )

        Text(
            text = spannableString(textFront = "작가명 : ", color = color000000, textEnd = item.writer),
            color = color8E8E8E,
            fontSize = 16.sp,
        )

        Text(
            text = spannableString(textFront = "북코드 : ", color = color000000, textEnd = item.bookCode),
            color = color8E8E8E,
            fontSize = 16.sp,
        )
    }
}

@Composable
@Preview
fun previewItem(){
    ItemTabletBestList(
        isLast = true, item = ItemBookInfo(
            bookImg = "https://cf.joara.com/literature_file/1295645_1693736606_thumb.png",
            title = "TITLE"
        )
    )
}

@Composable
fun ItemTabletTrophyList(item : ItemBestInfo, isLast: Boolean){

    Column {

        Spacer(modifier = Modifier.size(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.background(color = colorE9E9E9),
                text = item.bookCode,
                color = color000000,
                fontSize = 18.sp,
            )
        }

        Spacer(modifier = Modifier.size(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = spannableString(textFront = "현재 스코어 : ", color = color000000, textEnd = item.number.toString()),
                color = color8E8E8E,
                fontSize = 16.sp,
            )
        }

//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.Start,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                text = spannableString(textFront = "작품 정보1 : ", color = color000000, textEnd = item.info1),
//                color = color8E8E8E,
//                fontSize = 16.sp,
//            )
//        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = spannableString(textFront = "트로피 누적 점수 : ", color = color000000, textEnd = item.total.toString()),
                color = color8E8E8E,
                fontSize = 16.sp,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = spannableString(textFront = "트로피 누적 횟수 : ", color = color000000, textEnd = item.totalCount.toString()),
                color = color8E8E8E,
                fontSize = 16.sp,
            )
        }

        if(!isLast){
            Spacer(modifier = Modifier.size(2.dp))
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(color = colorE9E9E9))
            Spacer(modifier = Modifier.size(2.dp))
        }
    }
}

@Composable
fun ItemTabletTitle(str : String, isTopPadding : Boolean = true){

    if(isTopPadding){
        Spacer(modifier = Modifier.size(16.dp))
    }

    Text(
        modifier = Modifier.padding(16.dp, 8.dp),
        text = str,
        fontSize = 16.sp,
        color = color8E8E8E,
        fontWeight = FontWeight(weight = 700)
    )
}

@Composable
fun spannableString(textFront : String, color : Color, textEnd : String) : AnnotatedString {
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = color, fontWeight = FontWeight(weight = 500))) {
            append(textFront)
        }
        append(textEnd)
    }

    return annotatedString
}

@Composable
fun ScreenEmpty(str : String = "마나바라") {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorF6F6F6),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.wrapContentSize(), horizontalAlignment = Alignment.CenterHorizontally) {

            androidx.compose.material3.Card(
                modifier = Modifier
                    .wrapContentSize(),
                colors = CardDefaults.cardColors(containerColor = colorDCDCDD),
                shape = RoundedCornerShape(50.dp, 50.dp, 50.dp, 50.dp)
            ) {
                Box(
                    modifier = Modifier
                        .height(90.dp)
                        .width(90.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        contentScale = ContentScale.FillWidth,
                        painter = painterResource(id = R.drawable.ic_launcher),
                        contentDescription = null,
                        modifier = Modifier
                            .height(72.dp)
                            .width(72.dp)
                    )
                }
            }

            Spacer(
                modifier = Modifier.size(8.dp)
            )
            Text(
                text = str,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = color000000
            )
        }
    }
}