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

@Composable
fun ContentsBestJsonListComic(
    setDetailPage: (Boolean) -> Unit,
    setDetailMenu: (String) -> Unit,
    setDetailPlatform: (String) -> Unit,
    setDetailType: (String) -> Unit,
    type: String,
) {

    TabletContentWrapBtn(
        onClick = {
            setDetailPage(true)
            setDetailMenu("네이버 시리즈 $type")
            setDetailPlatform("NAVER_SERIES")
            setDetailType("COMIC")
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

@Composable
fun ContentsBestJsonListNovel(
    setDetailPage: (Boolean) -> Unit,
    setDetailMenu: (String) -> Unit,
    setDetailPlatform: (String) -> Unit,
    setDetailType: (String) -> Unit,
    type: String,
) {

    TabletContentWrapBtn(
        onClick = {
            setDetailPage(true)
            setDetailMenu("조아라 $type")
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
            setDetailMenu("조아라 노블레스 $type")
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
            setDetailMenu("조아라 프리미엄 $type")
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
            setDetailMenu("네이버 시리즈 $type")
            setDetailPlatform("NAVER_SERIES")
            setDetailType("NOVEL")
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

    TabletContentWrapBtn(
        onClick = {
            setDetailPage(true)
            setDetailMenu("네이버 챌린지 $type")
            setDetailPlatform("NAVER_CHALLENGE")
            setDetailType("NOVEL")
        },
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = R.drawable.logo_naver_challenge),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "네이버 챌린지",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {
            setDetailPage(true)
            setDetailMenu("네이버 베스트 $type")
            setDetailPlatform("NAVER_BEST")
            setDetailType("NOVEL")
        },
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = R.drawable.logo_naver_challenge),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "네이버 베스트",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {
            setDetailPage(true)
            setDetailMenu("리디북스 판타지 $type")
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
                    text = "리디북스 판타지",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {
            setDetailPage(true)
            setDetailMenu("리디북스 로맨스 $type")
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
                    text = "리디북스 로맨스",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {
            setDetailPage(true)
            setDetailMenu("원스토리 $type")
            setDetailPlatform("ONESTORY")
            setDetailType("NOVEL")
        },
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = R.drawable.logo_onestore),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "원스토리 로맨스",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {
            setDetailPage(true)
            setDetailMenu("카카오 스테이지 $type")
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
                    text = "카카오 스테이지 로맨스",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {
            setDetailPage(true)
            setDetailMenu("문피아 $type")
            setDetailPlatform("MUNPIA")
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
                    text = "문피아 로맨스",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {
            setDetailPage(true)
            setDetailMenu("톡소다 $type")
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
                    text = "톡소다 로맨스",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    Spacer(modifier = Modifier.size(60.dp))
}