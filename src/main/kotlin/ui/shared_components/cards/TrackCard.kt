package ui.shared_components.cards

import Requester
import kotlinx.browser.document
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.html.*
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onKeyUpFunction
import misc.js.Media
import models.Track
import org.w3c.fetch.Request
import ui.shared_components.staticProgressBar

fun DIV.trackCard(t: Track) {
    onClickFunction = { ev ->
        Media.playSoundFromUrl(t.previewURL)
    }

    span("cardTitle") { +t.name }
    if(t.similarity != null)
        span() { br(); +"CosSim: ${t.similarity!!.toString().slice(0..5)}%"; }

    span("cardSubTitle") { +t.artists }

    br { style = "clear: both;" }

    div("divTrackCardPopularity") {
        hidden = true;
        staticProgressBar( t.popularity.toString().toFloat(), "LightCoral", 100f)
    }


    ul("ulFeatures") {
        hidden = true;
        style =


                "list-style-type: none; margin-block-start: 0.5em; margin-block-end: 0.3em;" +
                "padding: 1.45% 2% 0.3% 1.5%; margin: 1px;  border:solid 1px rgba(33,55,33,0.05); " +
                "box-shadow:" +
                "inset 1px 1px 1px 2px rgba(8,8,8,0.35)," +
                "inset 3px 3px 2px 5px rgba(8,8,8,0.15);" +
                ""
    }


    div("divTrackCard_exportTrainingData") {
        style="""
            margin: 1px 0 1px 0; padding: 1px; 
            display: none;
        """

        // range input
        input(type = InputType.range){
            max = "4"
            min = "0"
            value = "2"

            onKeyUpFunction = { ev->

                console.log("trackTrainingHappiness_${t.id}")
                console.log(value)

                val lblel = document.getElementById("trackTrainingHappiness_${t.id}")

                lblel!!.asDynamic().innerHTML = when(ev.target.asDynamic().value){
                        4 -> "\uD83D\uDE04 <small>very happy</small>"
                        3 -> "\uD83D\uDE42 <small>a bit happy</small>"
                        2 -> "\uD83D\uDE10 <small>neutral - neither happy nor sad</small>"
                        1 -> "\uD83D\uDE41 <small>a bit sad</small>"
                        0 -> "\uD83D\uDE25 <small>very sad</small>"
                        else -> ""
                }
                GlobalScope.launch {

                    console.log("get mf of id:")
                    console.log(t.id)
                    console.log("mf:")
                    val a = Requester().getMusicalFeatures(t.id)
                    console.log(a)
                }

            }
        }

        span {
            id = "trackTrainingHappiness_${t.id}"
            style = "font-size: 1.2em;"
            +"\uD83D\uDE10"
        }
    }
}