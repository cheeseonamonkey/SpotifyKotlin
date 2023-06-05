package ui.pages

import kotlinx.browser.document
import kotlinx.html.*
import kotlinx.html.dom.append

object IndexPage : IPage {
    override fun loadPage() {
        document.body?.append {
            ul {
                li {
                    a {
                        navigateLocal("/spotify/auth")
                        style = "font-size: 1.5em;"
                            h1{+"Spotify ->"}
                    }
                }
            }
        }
    }
}