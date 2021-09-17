package cu.ondev.nuestraradio.extensions

import android.content.res.Resources
import androidx.compose.ui.unit.Dp

val Int.toDp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.toPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()