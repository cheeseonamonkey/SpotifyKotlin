package ui.pages

import Requester
import kotlinx.browser.document
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.html.div
import kotlinx.html.dom.append
import kotlinx.html.*
import ui.shared_components.dataListView
import ui.shared_components.cards.playlistCard
import ui.shared_components.searchBarInput

object SearchResultsPage : IInfoViewPage {

    override fun loadPage() {
        document.body?.append {
            GlobalScope.launch {

                getClientCreds()

                val param_q = params.get("q")
                val param_type = params.get("type")

                val datalist = Requester().getSearchQuery(param_q.toString(), param_type!!.split(","))
                console.log(datalist)

                div {

                    divInfoView("${urlOrigin}/spotify/res/search.svg", "") {

                        h5 { +"search" }
                        br{style="clear:both;"}
                        searchBarInput()
                    }



                    dataListView("search results:", datalist) {
                        playlistData ->
                            playlistCard(playlistData)
                    }

                }
            }
        }
    }
}
