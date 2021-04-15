package cu.ondev.nuestraradio.utilities

import cu.ondev.nuestraradio.data.RadioBase
import org.jsoup.nodes.Document

class JsoupRadioParser(private val htmlDoc: Document) {

    fun toRadioBase(): List<RadioBase> {
        val artItems = htmlDoc.select("div.items-leading")
        val artPost = artItems.select("article.art-post")
        val newRadioBase = mutableListOf<RadioBase>()

        for (artPostTmp in artPost.toList()) {
            //Separamos el titulo del resto del texto que dice Audio en Internet
            val title =
                artPostTmp.select("a[href]").eachText().first().toString().split("|")[0].trim()

            //Separamos las visitas usando el delimitador :
            val visto =
                artPostTmp.select("div.art-postheadericons").eachText().first().split(":")[1].trim()

            val streamUrl =
                artPostTmp.select("source").attr("type", "audio/mp3").eachAttr("src").first()

            newRadioBase.add(RadioBase(0, title, streamUrl, visitas = visto.toInt()))
        }
        return newRadioBase
    }

}