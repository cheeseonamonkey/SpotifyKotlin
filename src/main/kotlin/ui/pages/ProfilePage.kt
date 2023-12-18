package ui.pages

import ui.shared_components.dataListView
import Requester
import kotlinx.browser.document
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.html.*
import kotlinx.html.dom.append

import misc.TimeLength
import misc.js.Cookie
import misc.js.Swiper.htmlTabs


import misc.js.Swiper.initTabs
import ui.shared_components.cards.trackCard
import models.Playlist
import models.Profile
import models.Track

import org.w3c.dom.HTMLElement
import org.w3c.dom.get
import org.w3c.fetch.Headers
import org.w3c.fetch.RequestInit
import ui.shared_components.cards.playlistCard


object ProfilePage : IInfoViewPage {

    override fun loadPage() {






        document.body?.append {

            getClientCreds()

            GlobalScope.launch {

                val qUid = params.get("uid")

                if (qUid == null)
                    navigateLocal("spotify/profile/me")


                //
                // awaited requests:

                //user info
                val user = Requester().getUser(qUid!!)

                //draw info bar, before awaiting more requests
                infoBar(user, qUid)
                //loading image to hide later
                loadingGif()


                //playlists
                user.playlists = Requester().getUsersPlaylists(qUid)
                //top tracks (authenticated user only)
                if(Cookie.get("myUid") == user.id){
                    runCatching {
                        user.topTracks[TimeLength.MONTH] = Requester().getTopTracks(TimeLength.MONTH)
                        user.topTracks[TimeLength.YEAR] = Requester().getTopTracks(TimeLength.YEAR)
                        user.topTracks[TimeLength.ALL_TIME] = Requester().getTopTracks(TimeLength.ALL_TIME)
                    }.onFailure { console.error("error getting top tracks") }
                }



                // dev log stuff:
                console.info(user)
                // these are already in the user object:
                //console.info(user.playlists)
                //console.info(user.topTracks)




                htmlTabs(

                    //tab 1 (playlists)
                    {
                        dataListView<Playlist>("playlists", user.playlists!!) {
                            playlistCard(it)
                        }
                    },

                    //tab 2 (tops month):
                    {
                        dataListView<Track>("top tracks (month):", user.topTracks[TimeLength.MONTH] ?: listOf<Track>() ) {
                            topT ->
                            trackCard(topT)
                        }
                    },
                    //tab 3 (tops year):
                    {
                        dataListView<Track>("top tracks (year):", user.topTracks[TimeLength.YEAR] ?: listOf<Track>() ) {
                                topT ->
                            trackCard(topT)
                        }
                    },
                    //tab 4 (tops all time):
                    {
                        dataListView<Track>("top tracks (all time):", user.topTracks[TimeLength.ALL_TIME] ?: listOf<Track>() ) {
                                topT ->
                            trackCard(topT)
                        }
                    },



                )


                initTabs()

                //hide loading image
                document.getElementsByClassName("loadingGif")[0]?.remove()








            }

        }
    }


    inline fun TagConsumer<HTMLElement>.loadingGif() {
            img(classes = "loadingGif") {
                src = "/spotify/res/loading.svg"
                style = "min-height: 365px; width:103vw; max-width: 663px; position: fixed; left:1.3vw; pointer-events:none; opacity: 86%;"
            }
    }


    inline fun TagConsumer<HTMLElement>.infoBar(user: Profile, qUid: String) = div {

            divInfoView(
                imgUrl = user.images[0].url,
                imgTxt = user.displayName,

            ) {



                    li {
                        +"Followers: ${user.followers.total}"
                    }



                //user id bottom right
                small {
                    style = "text-align: right; flex-grow: 1; float: right; margin-right: 2vw; "
                    +"$qUid"
                }

                br { style = "break: both;" }
            }
        }
    }



