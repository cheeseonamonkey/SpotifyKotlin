@file:Suppress("MoveLambdaOutsideParentheses")
@file:OptIn(DelicateCoroutinesApi::class)

package ui.pages

import Requester
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.html.*
import kotlinx.html.dom.*
import kotlinx.html.js.onClickFunction
import misc.js.Cookie
import misc.js.Swiper.htmlTabs
import misc.js.Swiper.initTabs
import models.Track
import org.w3c.dom.HTMLUListElement
import ui.shared_components.cards.playlistCard
import ui.shared_components.cards.trackCard
import ui.shared_components.dataListView

object LikedTracksPage : IInfoViewPage {
    override fun loadPage() {
        GlobalScope.async {

            document.body?.append {
                div {
                    divInfoView("/spotify/res/hearts.png", "Your Liked Tracks") {
                        div {
                            style =
                                "font-size: 0.7em; height: 95%; margin: .05em; max-width: 63vw; min-width: 40%; float: left; padding: .2em; display: inline-block;"

                            ul("ulProgressLog") {
                                style =
                                    "font-size: 0.88em; width: 94%;  margin-block-start: .15em; list-style: none; padding-inline-start: 9px; display: inline-block;"
                            }
                        }
                        br { style = "clear:both;" }
                    }

                    br()

                }
                document.body?.append { div("divMain") {} }

            }

            // a little logger & its extension function
            val ulProgressLog = document.querySelector(".ulProgressLog") as HTMLUListElement
            fun HTMLUListElement.appendA(str: String) = this.append {
                li {
                    +str; style =
                    "font-family: monospace; font-size: 0.83em; min-width: 87%; font-weight: bold; padding: .1em; border-bottom: 2px solid rgba(11,25,11,0.2);box-shadow: inset -2px 2px 2px 4px rgba(1,11,1,0.1); border-left: 2px solid rgba(11,25,11,0.2);"
                }
            }

            operator fun HTMLUListElement.plusAssign(s: String) {
                this.appendA(s)
            }

            val requester = Requester()
            ulProgressLog += "requester init"

            ulProgressLog += "getting playlists.. "
            val myPlaylists = requester.getMyPlaylists()
            ulProgressLog.lastElementChild?.innerHTML += "  ..done!"


            ulProgressLog += "getting liked tracks.. "
            ulProgressLog += ""

            val myLiked = requester.getMyLikedTracks(
                progressBlock = { iProg, iTotal, iWasCached ->
                    document.querySelector(".ulProgressLog")?.apply {
                        if (iProg == 0) // append the first time - then modify in DOM
                            this.lastElementChild?.append {
                                span {
                                    style =
                                        "font-family: monospace; font-size: 81%; font-weight: bold; letter-spacing: -0.05em; padding: .07em; margin: .07em 0 .28em .1em; border-bottom: .8px solid rgba(11,25,11,0.25); box-shadow: inset 1px 1px 1.5px 3px rgba(5,29,5,0.05); border-right: .8px solid rgba(11,25,11,0.25);";
                                }
                            }

                        this.lastElementChild?.lastElementChild?.innerHTML =
                            "${(((iProg ?: 1) / (iTotal ?: 1).toDouble()) * 100).toString().slice(0..3)}%"

                        if (iWasCached)
                            this.lastElementChild?.lastElementChild?.innerHTML += " <i style=\"font-size:80%;\"> (cached) </i>"


                    }
                }
            )
            ulProgressLog += "  ...done! ${myLiked.size} tracks found."

            console.log(myLiked)


            // playlist tracks:
            ulProgressLog += "getting playlist tracks..."
            console.log("playlists:")
            console.log(myPlaylists)
            ulProgressLog += ""


            val playlistTracks: MutableList<Track> = mutableListOf()
            myPlaylists.forEachIndexed { i, pl ->
                runCatching {
                    console.log("getting playlist: ${pl.name}")
                    ulProgressLog.lastElementChild?.innerHTML = "playlist progress: ${
                        ((i.toDouble() / myPlaylists.size.toDouble()) * 100.0).toString().slice(0..3)
                    }%"
                    playlistTracks.addAll(requester.getPlaylistTracks(pl.tracksLink))
                }.onFailure { console.error(it.message) }
            }
            console.log(playlistTracks)

            ulProgressLog += "..done! ${playlistTracks.size} tracks found."



            document.querySelector(".divMain")?.append {
                table {
                    style = "font-weight: bold; width:96%; margin: 2%; font-size: 0.86em;"

                    //row1
                    tr {
                        td {
                            style =
                                "width:49%; margin: 1%; box-shadow: inset .4px .4px .6px 1.2px rgba(1,1,1,0.1); padding: 1px; "
                            +"liked tracks: "
                            code { +myLiked.size.toString() }
                        }
                        td {
                            style =
                                "width:49%; margin: 1%; box-shadow: inset .4px .4px .6px 1.2px rgba(1,1,1,0.1); padding: 1px; "
                            +"playlist tracks: "
                            code { +playlistTracks.size.toString() }
                        }
                    }

                    //row2
                    tr {
                        td {
                            style = "width: 100%;"

                            button {
                                style = "font-size: 0.75em; max-height: 0.9em; display: inline;"
                                +"like all ${playlistTracks.size} playlist tracks"
                                onClickFunction = {
                                    ulProgressLog += "liking all tracks..."
                                    val tracksToLike = mutableListOf<String>()
                                    playlistTracks.forEach {
                                        tracksToLike.add(it.id)
                                    }
                                    GlobalScope.async {
                                        requester.likeTracks(tracksToLike)
                                    }

                                }
                            }
                        }

                    }
                }
            }


        }


    }//end GlobalScope.async
}
