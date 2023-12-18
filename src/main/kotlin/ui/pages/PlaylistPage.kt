package ui.pages

import ui.shared_components.dataListView
import Requester
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.html.*
import kotlinx.html.dom.append
import kotlinx.html.js.progress
import learning.embedding.sentenceencoder.Model
import learning.embedding.runTimed
import models.Track
import org.w3c.dom.HTMLProgressElement
import org.w3c.dom.HTMLSpanElement
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
                    ) {

                        ul {
                            li {
                                +"owner: "
                                a(href = "${window.location.origin}/spotify/profile/?uid=${playlist.ownerId}") {
                                    +"${playlist.ownerId}"
                                }
                            }
                            li {
                                +"playlist tracks: ${playlist.numTracks}"
                            }
                        }
                        //id bottom right
                        small {
                            style = "text-align: right; flex-grow: 1; float: right; margin-right: 2vw; "
                            +playlist.id
                        }

                        br { style = "break: both;" }
                    }
                    div("divSecondary"){

                    small { +"select track to hear preview!" }; br();

                    GlobalScope.launch {

                        if (playlist.tracksList.first() is Track) {
                            lateinit var embedProg: HTMLProgressElement

                            document.querySelector(".infobar")?.append {
                                br();
                                span { style = "font-size: 75%;"; +"semantic embedding:" }
                                embedProg = progress {

                                    style = "height: 1em; width: 85%; padding: 0;"
                                    value = "0.23"
                                    max = (((playlist.tracksList.size / 7.5)) + 1).toString()
                                }


                            }



                            delay(200)

                            // embed all the tracks


                            val model = Model.loadModel()

                            playlist.tracksList.toList().chunked(7).forEach { chunk ->
                                runTimed<Unit>("embed shard (7 count)   ") {
                                    chunk.forEach { itt ->
                                        embedProg.value += 1.0 / chunk.size
                                        GlobalScope.async {
                                            val titleEmbedded = model.embed((itt as Track).name)
                                            (itt as Track).embedding = titleEmbedded
                                        }.await()
                                    }
                                    embedProg.value += 1;
                                }
                            }
                            embedProg.previousElementSibling?.run { if (this is HTMLSpanElement) this.remove() }
                            embedProg.remove()
                        }
                    }
                }
                    dataListView("playlist tracks", playlist.tracksList) {
                            cardData -> trackCard(cardData)
                    }










                }

            }

        }
    }


}
