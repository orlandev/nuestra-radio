package cu.ondev.nuestraradio.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cu.ondev.nuestraradio.ui.theme.*


@Composable
fun NuestraRadio() {
    Banner(modifier = Modifier.height(230.dp))
}

@Composable
fun Banner(modifier: Modifier = Modifier) {

    var isFavorite = remember { mutableStateOf(false) }

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

                if(!isFavorite.value) {
                    Icon(
                        Icons.Filled.FavoriteBorder,
                        tint = Color.White,
                        contentDescription = "favorites"
                    )
                }else {
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