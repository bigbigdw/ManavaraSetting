package com.bigbigdw.manavarasetting.main.screen

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bigbigdw.manavarasetting.R
import com.bigbigdw.manavarasetting.ui.theme.color000000
import com.bigbigdw.manavarasetting.ui.theme.color02BC77
import com.bigbigdw.manavarasetting.ui.theme.color20459e
import com.bigbigdw.manavarasetting.ui.theme.colorA7ACB7
import com.bigbigdw.manavarasetting.ui.theme.colorEDE6FD
import com.bigbigdw.manavarasetting.ui.theme.colorFDFDFD
import com.bigbigdw.manavarasetting.ui.theme.colordcdcdd
import com.bigbigdw.manavarasetting.ui.theme.colorf7f7f7

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
            fontWeight = FontWeight(weight = 100)
        )
        Text(
            text = valueWorker,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = color20459e,
            fontWeight = FontWeight(weight = 100)
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
            fontWeight = FontWeight(weight = 100)
        )
        Text(
            text = valueStatus,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = color20459e,
            fontWeight = FontWeight(weight = 100)
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
            fontWeight = FontWeight(weight = 100)
        )
        Text(
            text = valueWorker,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = color20459e,
            fontWeight = FontWeight(weight = 100)
        )
    }
}

@Composable
fun ItemMainSettingSingleTablet(
    image: Int,
    title: String,
    body: String
) {

    Button(
        colors = ButtonDefaults.buttonColors(containerColor = colorf7f7f7),
        shape = RoundedCornerShape(0),
        onClick = { /*TODO*/ },
        content = {
        Row(
            modifier = Modifier.fillMaxWidth().padding(0.dp, 8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Card(
                modifier = Modifier
                    .wrapContentSize(),
                backgroundColor = colordcdcdd,
                shape = RoundedCornerShape(50.dp, 50.dp, 50.dp, 50.dp)
            ) {
                Box(
                    modifier = Modifier
                        .height(44.dp)
                        .width(44.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        contentScale = ContentScale.FillWidth,
                        painter = painterResource(id = image),
                        contentDescription = null,
                        modifier = Modifier
                            .height(40.dp)
                            .width(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.size(16.dp))

            Column {
                Row {
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        color = color000000,

                        fontWeight = FontWeight(weight = 500)
                    )
                }

                Spacer(modifier = Modifier.size(2.dp))

                Row {
                    Text(
                        text = body,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        )
                }
            }
        }
    })
}
