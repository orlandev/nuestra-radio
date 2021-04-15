package cu.ondev.nuestraradio.api

import cu.ondev.nuestraradio.data.RadioBase
import cu.ondev.nuestraradio.utilities.JsoupRadioParser
import org.jsoup.Jsoup

object RadioApi {
    private val TAG = "URL-GET"
    const val API_URL = "https://www.radiocubana.icrt.cu/emisoras/"

    fun getRadiosStreamData(): List<RadioBase>? {

        val htmlDoc = Jsoup.connect(API_URL).userAgent("Mozilla").get()
        return JsoupRadioParser(htmlDoc = htmlDoc).toRadioBase()

    }
}