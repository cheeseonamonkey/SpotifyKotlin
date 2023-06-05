package ui.pages

import ui.shared_components.dataListView
import Requester
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.html.*
import kotlinx.html.dom.append
import ui.shared_components.cards.trackCard

object PlaylistPage : IInfoViewPage {
    override fun loadPage() {
        document.body?.append {

            GlobalScope.launch {

                getClientCreds()



                val qPid = params.get("pid")
                if (qPid == null) {
                    console.log("no playlist id")
                }

                val playlist = Requester().getPlaylist(qPid.toString())
                console.log("playlist:")
                console.log(playlist)


                //get tracks
                playlist.tracksList = Requester().getPlaylistTracks(playlist.tracksLink)
                //get track features
                val audioFeatures = Requester().getAudioFeatures(playlist.trackIDs())
                //asign audiofeatures to each track
                playlist.setTrackAudioFeatures(audioFeatures)

                console.log("playlist.tracksList:")
                console.log(playlist.tracksList)

                div {

                    divInfoView(
                        imgUrl = playlist.imageUri,
                        imgTxt = playlist.name
                    ){

                        ul {
                            li {
                                +"owner: "
                                a(href="${window.location.origin}/spotify/profile/?uid=${playlist.ownerId}"){
                                    +"${playlist.ownerId}"
                                }
                            }
                            li {
                                +"playlist tracks: ${playlist.numTracks}"
                            }
                        }
                        //id bottom right
                        small { style = "text-align: right; flex-grow: 1; float: right; margin-right: 2vw; "
                            +playlist.id }

                        br { style = "break: both;" }
                    }

                    small {+"select track to hear preview!" }

                    dataListView("playlist tracks", playlist.tracksList) {
                            cardData -> trackCard(cardData)
                    }









                }

            }

        }
    }


}
