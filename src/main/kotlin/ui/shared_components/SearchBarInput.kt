package ui.shared_components

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.html.*
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onKeyPressFunction
import kotlinx.html.js.onKeyUpFunction
import org.w3c.dom.HTMLElement

fun TagConsumer<HTMLElement>.searchBarInput() {
    input(type = InputType.text) {
        id = "txtSearch"
        style =
            "width: 85%;  margin: 2px;  float: left; box-shadow: inset 1px 1px 0 1px rgba(14,76,15,0.2); line-height:0.95; letter-spacing: -0.5px; margin-top: 1.32em;"

        onKeyUpFunction = { ev ->
            //window.clearTimeout(timer)
            //timer = window.setTimeout({ stoppedTyping() }.asDynamic(),600)
        }
        onKeyPressFunction = { ev ->

            val eventkey = ev.asDynamic().key

            if (eventkey.toString().contains("Enter", true)) {
                //TODO: enter to search
                console.log(eventkey.toString())
                window.location.href = window.location.origin + "/spotify/search?q=${this.value}&type=playlists"
            } else {
                //window.clearTimeout(timer)
            }

        }

    }
    button {
        style = "width: 9%; height: 25px; line-height:0.95;float: left;margin: 2px; padding: 0;  margin-top: 1.36em; font-size: 55%; letter-spacing: -0.65px; font-weight: thin;"
        onClickFunction = { window.location.href = window.location.origin + "/spotify/search?q=${document.getElementById("txtSearch").asDynamic().value }&type=playlists" }
        +"search"
    }
    br{style="clear:both;"}
}