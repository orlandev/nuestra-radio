package cu.ondev.nuestraradio.api

import android.util.Log
import cu.ondev.nuestraradio.data.RadioBase
import org.jsoup.Jsoup

class RadioApi {
    private val TAG = "URL-GET"

    companion object {
        const val API_URL = "https://www.radiocubana.icrt.cu/emisoras/"
    }

    fun getRadiosStreamData(): List<RadioBase>? {

        var newRadioBase = mutableListOf<RadioBase>()

        val htmlDoc = Jsoup.connect(API_URL).get()
        val artItems = htmlDoc.select("div.items-leading")
        val artPost = artItems.select("article.art-post")

        for (artPostTmp in artPost.toList()) {
            val title =
                artPostTmp.select("a[href]").eachText().first().toString().split("|")[0].trim()
            val visto =
                artPostTmp.select("div.art-postheadericons").eachText().first().split(":")[1].trim()
            val streamUrl =
                artPostTmp.select("source").attr("type", "audio/mp3").eachAttr("src").first()
            Log.d(TAG, "newRadioBase: $title | $streamUrl | $visto")
        }


        return null
    }
}