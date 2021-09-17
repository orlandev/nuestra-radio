package cu.ondev.nuestraradio.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import cu.ondev.nuestraradio.R
import cu.ondev.nuestraradio.ui.theme.*


@Composable
fun NuestraRadio() {
    Box {
        Banner(
            modifier = Modifier
                .height(300.dp)
                .padding(bottom = 35.dp)
        )
        RadioControls(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun RadioControls(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(start = 30.dp, top = 8.dp, bottom = 8.dp, end = 30.dp)
            .height(70.dp)
    ) {
        Card(
            elevation = 8.dp,
            shape = CircleShape,
            modifier = modifier
                .fillMaxSize()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                Image(
                    painter = rememberImagePainter(R.drawable.radio_test),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape), contentDescription = "Nuestra Radio"
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    verticalArrangement = Arrangement.SpaceAround, modifier = Modifier
                        .fillMaxSize()
                        .weight(0.3f)
                ) {
                    Text(text = "Radio Name")
                    Text(text = "Visitantes")
                }

                IconButton(
                    onClick = {

                    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(color = OrangeDark)
                ) {
                    Icon(
                        Icons.Filled.PlayArrow,
                        tint = Color.White,
                        contentDescription = "favorites"
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                IconButton(
                    onClick = {

                    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(color = Gray)
                ) {
                    Icon(
                        Icons.Filled.SkipNext,
                        tint = Color.White,
                        contentDescription = "favorites"
                    )
                }
            }
        }
    }
}

@Composable
fun Banner(modifier: Modifier = Modifier) {

    val isFavorite = remember { mutableStateOf(false) }

    DrawBackground(
        modifier = modifier
            .fillMaxSize()
            .background(color = OrangeDark)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 16.dp, top = 20.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.3f)
            ) {
                Text(
                    modifier = Modifier.padding(start = 50.dp),
                    text = "25/30",
                    style = Typography.caption,
                    color = Color.White
                )
                Text(
                    modifier = Modifier.padding(start = 50.dp),
                    text = "Nuestra Radio",
                    style = Typography.h1,
                    color = Color.White
                )
                Text(
                    modifier = Modifier.padding(start = 50.dp),
                    text = "100K Vistas",
                    style = Typography.h5,
                    color = Color.White
                )
            }
            IconButton(
                onClick = {
                    isFavorite.value = !isFavorite.value
                },
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(color = OrangeDark)
            ) {

                if (!isFavorite.value) {
                    Icon(
                        Icons.Filled.FavoriteBorder,
                        tint = Color.White,
                        contentDescription = "favorites"
                    )
                } else {
                    Icon(
                        Icons.Filled.Favorite,
                        tint = Color.White,
                        contentDescription = "favorites"
                    )
                }
            }
        }
    }
}

@Composable
fun DrawBackground(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val smallCircleRadius = 60f
    val smallOffset = Offset(x = -50f, y = -50f)
    val bigCircleRadius = 180f
    val bigCircleOffset = Offset(x = 180f, y = -50f)
    Box(modifier = modifier) {
        CircleDraw(
            modifier = Modifier
                .size(Dp(smallCircleRadius) * 2)
                .offset(
                    x = Dp(smallOffset.x), y = Dp(smallOffset.y)
                ), color = OrangeLight200
        )
        CircleDraw(
            modifier = Modifier
                .size(Dp(bigCircleRadius) * 2)
                .offset(x = Dp(bigCircleOffset.x), y = Dp(bigCircleOffset.y)),
            color = OrangeLight
        )
        content()
    }
}

@Composable
fun CircleDraw(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier,
        onDraw = {
            drawCircle(color = color)
        })
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NuestraRadioTheme {
        NuestraRadio()
    }
}