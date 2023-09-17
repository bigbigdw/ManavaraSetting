package com.bigbigdw.manavarasetting.main.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bigbigdw.manavarasetting.R
import com.bigbigdw.manavarasetting.main.viewModels.ViewModelFCM
import com.bigbigdw.moavara.theme.color1E1E20
import com.bigbigdw.moavara.theme.color844DF3
import com.bigbigdw.moavara.theme.colorEDE6FD
import com.bigbigdw.moavara.theme.colorFFFFFF
import com.bigbigdw.moavara.theme.pretendardvariable
import com.bigbigdw.moavara.theme.textColorType2

@Composable
fun ScreenMain(viewModelFCM: ViewModelFCM) {

    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = color1E1E20)
                .verticalScroll(rememberScrollState())
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
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(8.dp))
            Text(
                text = "MANAVARASETTING",
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                color = colorFFFFFF,
                fontFamily = pretendardvariable,
                fontWeight = FontWeight(weight = 100)
            )
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(22.dp))
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = color844DF3),
                onClick = { viewModelFCM.getFCMToken(context) },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(text = "FCM 토큰 얻기", textAlign = TextAlign.Center, color = colorEDE6FD, fontSize = 16.sp, fontFamily = pretendardvariable)
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(22.dp))
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = color844DF3),
                onClick = { viewModelFCM.postFCMAlert(context) },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(text = "FCM 테스트", textAlign = TextAlign.Center, color = colorEDE6FD, fontSize = 16.sp, fontFamily = pretendardvariable)
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "By 김대우",
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = textColorType2,
                fontFamily = pretendardvariable
            )
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(8.dp))
            Text(
                text = "BIGBIGDW",
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = textColorType2,
                fontFamily = pretendardvariable
            )
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(60.dp))
        }
    }
}