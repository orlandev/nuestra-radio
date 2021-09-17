package cu.ondev.nuestraradio.api

import android.util.Log
import cu.ondev.nuestraradio.data.RadioBase
import cu.ondev.nuestraradio.utilities.JsoupRadioParser
import org.jsoup.Jsoup

object RadioApi {
    private val TAG = "URL-GET"
    const val BASE_URL = "https://www.radiocubana.icrt.cu"
    const val API_URL = "$BASE_URL/emisoras/"

    fun getRadiosStreamData(): List<RadioBase>? {
        val page1 = Jsoup.connect(API_URL).userAgent("Mozilla").get()
        val radioListPage1 = JsoupRadioParser(htmlDoc = page1).toRadioBase()
        val resultList = mutableListOf<RadioBase>()
        resultList.addAll(radioListPage1)
        Log.d(TAG, "getRadiosStreamData: PAGE1: ${radioListPage1.size}")
        return resultList.toList()
    }
}