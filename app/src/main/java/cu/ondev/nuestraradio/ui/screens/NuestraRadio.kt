package cu.ondev.nuestraradio.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import cu.ondev.nuestraradio.ui.theme.*


@Composable
fun NuestraRadio() {
    Banner(modifier = Modifier.height(230.dp))
}

@Composable
fun Banner(modifier: Modifier = Modifier) {
    DrawBackground(
        modifier = modifier
            .fillMaxSize()
            .background(color = OrangeDark)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.5f)
            ) {
                Spacer(modifier = Modifier.padding(top = 70.dp))
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
                modifier = Modifier
                    .size(50.dp)
                    .clip(shape = CircleShape),
                onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.FavoriteBorder, "menu")   // ok
            }
        }
    }
}

@Composable
fun DrawBackground(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val smallCircleRadius = 75f
    val smallOffset = Offset(x = -50f, y = -80f)
    val bigCircleRadius = 120f
    val bigCircleOffset = Offset(x = 230f, y = -120f)
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