package ui.pages

import kotlinx.browser.window
import org.w3c.dom.url.URLSearchParams

interface IPage {

    val urlOrigin :String
        get() = window.location.origin

    fun loadPage()

    fun getClientCreds() = Requester.getClientCreds()

    fun navigateLocal(localUrl :String) {
        window.location.href = """${urlOrigin}/${localUrl.trim{ c -> c == '/' }}"""
    }



    val params : URLSearchParams
        get() {

        fun addQParam() {
            if(! window.location.href.contains("?")) {
                console.log("adding url param")
                window.location.href = window.location.href + "?"
            }

        }

        addQParam()

        return URLSearchParams(
            window.location.href.split("?")[1]
                .split("#")[0]
        )
    }
}