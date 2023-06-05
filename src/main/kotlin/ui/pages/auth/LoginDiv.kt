package ui.pages.auth

import kotlinx.browser.window
import kotlinx.html.*
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLElement

inline fun TagConsumer<HTMLElement>.loginDiv(client_id: String, scope: String) {
    div("loginDiv") {

        h2 { +"Log into Spotify"; style="margin-right: 0.1em;"}
        br{ style="clear: both;"}
        button {
            style = "margin: 3px; margin-top: 6px; margin-bottom: 5px; border: 2px solid rgba(1,38,1,0.98); background-color: rgba(18,140,21,0.3); border-radius: 7%; box-shadow: 1px 1px 1px 2px rgba(11,26,11,0.35), 3px 3px 2px 4px rgba(11,22,11,0.14), inset 1px 1px 1px 2px rgba(1,80,3,0.51);" +
                    "    text-shadow: 1px 1px rgba(11,11,11,0.15);"
            +"Login"
            onClickFunction = {

                var loginUrl = "https://accounts.spotify.com/authorize"
                loginUrl += "?response_type=token"
                loginUrl += "&client_id=$client_id"
                loginUrl += "&scope=$scope"
                loginUrl += "&redirect_uri=${window.location.href}"
                //loginUrl += '&state=' + encodeURIComponent(state)

                window.location.href = loginUrl
            }
        }
        br()
        small {style = "line-height: 90%; font-size: 47%; margin: 0;padding: 0;margin-top: 3px; color: rgba(8,44,8,0.95);"
            i { +"(redirects to spotify.com for authentication)" }
        }

    }
}