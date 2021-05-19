package cu.ondev.nuestraradio.api

import android.util.Log
import cu.ondev.nuestraradio.data.RadioBase
import cu.ondev.nuestraradio.utilities.JsoupRadioParser
import org.jsoup.Jsoup

object RadioApi {
    private val TAG = "URL-GET"
    const val BASE_URL = "https://www.radiocubana.icrt.cu"
    const val API_URL_PAGE_1 = "$BASE_URL/emisoras/"
    const val API_URL_PAGE_2 = "$BASE_URL/emisoras?start=84"

    fun getRadiosStreamData(): List<RadioBase>? {

        val page1 = Jsoup.connect(API_URL_PAGE_1).userAgent("Mozilla").get()
        val page2 = Jsoup.connect(API_URL_PAGE_2).userAgent("Mozilla").get()
        var radioListPage1 = JsoupRadioParser(htmlDoc = page1).toRadioBase()
        var radioListPage2 = JsoupRadioParser(htmlDoc = page2).toRadioBase()
        val resultList = mutableListOf<RadioBase>()
        resultList.addAll(radioListPage1)
        resultList.addAll(radioListPage2)
        Log.d(TAG, "getRadiosStreamData: PAGE1: ${radioListPage1.size}")
        Log.d(TAG, "getRadiosStreamData: PAGE2: ${radioListPage2.size}")
        return resultList.toList()

    }
}