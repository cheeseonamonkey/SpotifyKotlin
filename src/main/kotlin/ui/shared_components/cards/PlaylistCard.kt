package ui.shared_components.cards

import kotlinx.html.*
import kotlinx.html.js.onClickFunction
import models.Playlist
import ui.pages.ProfilePage

fun DIV.playlistCard(p: Playlist){

    onClickFunction = { ev ->
        ProfilePage.navigateLocal("/spotify/playlist/?pid=${p.id}")
    }

    //name
    span("cardTitle") { +p.name }

    // num tracks
    i("cardSubTitle") { +"${p.numTracks} tracks"; }

    br { style = "clear: both;" }

    //description
    span("cardDetail") {
        b { +"description:" }
        i { +p.description }
    }

}