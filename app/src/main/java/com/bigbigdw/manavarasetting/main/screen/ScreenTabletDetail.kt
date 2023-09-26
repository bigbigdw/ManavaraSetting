package com.bigbigdw.manavarasetting.main.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bigbigdw.manavarasetting.main.viewModels.ViewModelMain
import com.bigbigdw.manavarasetting.ui.theme.color000000
import com.bigbigdw.manavarasetting.ui.theme.colorf6f6f6

@Composable
fun ScreenTabletDetail(
    setDetailPage: (Boolean) -> Unit,
    getDetailMenu: String,
    viewModelMain: ViewModelMain,
    getDetailPageType: String
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Red)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorf6f6f6)
                .verticalScroll(rememberScrollState())
                .semantics { contentDescription = "Overview Screen" },
        ) {

            Spacer(modifier = Modifier.size(16.dp))

            Text(
                modifier = Modifier
                    .padding(24.dp, 0.dp, 0.dp, 0.dp)
                    .clickable {
                        setDetailPage(false)
                    },
                text = "< $getDetailMenu",
                fontSize = 24.sp,
                color = color000000,
                fontWeight = FontWeight(weight = 700)
            )

            Spacer(modifier = Modifier.size(16.dp))

            if(getDetailMenu.contains("베스트 리스트")){
                ContentsBestListDetail(viewModelMain = viewModelMain, child = getDetailPageType)
            }
        }
    }
}

@Composable
fun ContentsBestListDetail(viewModelMain: ViewModelMain, child : String){

    viewModelMain.getBestList(child = child)

    val bestList = viewModelMain.state.collectAsState().value.setBestBookList

    TabletContentWrap(
        radius = 5,
        content = {
            Spacer(modifier = Modifier.size(8.dp))

            bestList.forEachIndexed { index, item ->
                ItemTabletBestList(
                    item = item,
                    isLast = bestList.size - 1 == index
                )
            }

            Spacer(modifier = Modifier.size(8.dp))
        }
    )

    Spacer(modifier = Modifier.size(60.dp))
}