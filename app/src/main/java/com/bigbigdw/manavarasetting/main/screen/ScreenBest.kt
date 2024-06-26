package com.bigbigdw.manavarasetting.main.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bigbigdw.manavarasetting.R
import com.bigbigdw.manavarasetting.ui.theme.color000000
import com.bigbigdw.manavarasetting.util.changePlatformNameKor
import com.bigbigdw.manavarasetting.util.getPlatformLogoEng
import com.bigbigdw.manavarasetting.util.novelListEng

@Composable
fun ContentsListNovel(
    setDetailPage: (Boolean) -> Unit,
    setMenu: (String) -> Unit,
    setPlatform: (String) -> Unit,
    setType: (String) -> Unit,
    type : String
) {

    novelListEng().forEachIndexed { index, item ->
        TabletContentWrapBtn(
            onClick = {
                setDetailPage(true)
                setMenu("${changePlatformNameKor(item)} $type")
                setPlatform(item)
                setType("NOVEL")
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
                        text = changePlatformNameKor(item),
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
fun ContentsBestListComic(
    setDetailPage: (Boolean) -> Unit,
    setMenu: (String) -> Unit,
    setPlatform: (String) -> Unit,
    setType: (String) -> Unit,
    type : String
) {

    TabletContentWrapBtn(
        onClick = {
            setDetailPage(true)
            setMenu("네이버 시리즈 $type")
            setPlatform("NAVER_SERIES")
            setType("COMIC")
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
                    text = "네이버 시리즈",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    Spacer(modifier = Modifier.size(60.dp))
}

