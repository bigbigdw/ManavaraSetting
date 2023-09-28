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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.bigbigdw.manavarasetting.main.model.BestItemData
import com.bigbigdw.manavarasetting.ui.theme.color000000
import com.bigbigdw.manavarasetting.ui.theme.color20459e
import com.bigbigdw.manavarasetting.ui.theme.color8e8e8e
import com.bigbigdw.manavarasetting.ui.theme.colorA7ACB7
import com.bigbigdw.manavarasetting.ui.theme.colorEDE6FD
import com.bigbigdw.manavarasetting.ui.theme.colore9e9e9
import com.bigbigdw.manavarasetting.ui.theme.colorf7f7f7
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
            color = color20459e,
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
            color = color20459e,
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
        colors = ButtonDefaults.buttonColors(containerColor = color20459e),
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
fun ItemMainSettingSingle(
    image: Int,
    titleWorker: String,
    valueWorker: String,
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
            color = color20459e,
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
                start = 12.dp,
                top = 6.dp,
                end = 12.dp,
                bottom = 6.dp,
            ),
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
                        color = color20459e,
                    )
                }
            })

        if(!isLast){
            Spacer(modifier = Modifier.size(2.dp))
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(color = colore9e9e9))
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
    setMenu: (String) -> Unit,
    getMenu: String,
    onClick : () -> Unit
) {

    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = if (getMenu == title) {
                colore9e9e9
            } else {
                colorf7f7f7
            }
        ),
        shape = RoundedCornerShape(50),
        onClick = {
            setMenu(title)
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

            Card(
                modifier = Modifier
                    .wrapContentSize(),
                backgroundColor = containerColor,
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
                        color = color8e8e8e,
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
        .background(color = color8e8e8e))
    Spacer(modifier = Modifier.size(8.dp))
}

@Composable
fun TabletContentWrap(radius : Int, content : @Composable () -> Unit){
    Card(
        modifier = Modifier
            .fillMaxSize(),
        backgroundColor = Color.White,
        shape = RoundedCornerShape(percent = radius),
        elevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp, 4.dp)
        ) {
            content()
        }
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

    Column {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.background(color = if(item.date.contains(today)){
                    colore9e9e9
                } else {
                    Color.Transparent
                }),
                text = "${year}.${month}.${day} ${hour}:${min}",
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
                color = color8e8e8e,
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
                color = color8e8e8e,
                fontSize = 16.sp,
            )
        }

        if(!isLast){
            Spacer(modifier = Modifier.size(2.dp))
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(color = colore9e9e9))
            Spacer(modifier = Modifier.size(2.dp))
        }
    }
}

@Composable
fun ItemTabletBestList(item : BestItemData, isLast: Boolean){

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
                        color = color8e8e8e,
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
                        color = color8e8e8e,
                        fontSize = 16.sp,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = spannableString(textFront = "헌재 스코어 : ", color = color000000, textEnd = item.current.toString()),
                        color = color8e8e8e,
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
                        color = color8e8e8e,
                        fontSize = 16.sp,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = spannableString(textFront = "작품 정보1 : ", color = color000000, textEnd = item.info1),
                        color = color8e8e8e,
                        fontSize = 16.sp,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = spannableString(textFront = "작품 정보2 : ", color = color000000, textEnd = item.info2),
                        color = color8e8e8e,
                        fontSize = 16.sp,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = spannableString(textFront = "작품 정보3 : ", color = color000000, textEnd = item.info3),
                        color = color8e8e8e,
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
                        color = color8e8e8e,
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
                        color = color8e8e8e,
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
                        color = color8e8e8e,
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
                        color = color8e8e8e,
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
                        color = color8e8e8e,
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
                        color = color8e8e8e,
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
                .background(color = colore9e9e9))
            Spacer(modifier = Modifier.size(2.dp))
        }
    }
}

@Composable
fun ItemTabletBestListVertical(item: BestItemData){

    Column(modifier = Modifier.padding(8.dp).widthIn(0.dp, 220.dp)){

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
            color = color8e8e8e,
            fontSize = 16.sp,
        )

        Text(
            text = spannableString(textFront = "북코드 : ", color = color000000, textEnd = item.bookCode),
            color = color8e8e8e,
            fontSize = 16.sp,
        )
    }
}

@Composable
@Preview
fun previewItem(){
    ItemTabletBestList(
        isLast = true, item = BestItemData(
            bookImg = "https://cf.joara.com/literature_file/1295645_1693736606_thumb.png",
            title = "TITLE"
        )
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
