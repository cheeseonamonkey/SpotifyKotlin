import kotlinx.browser.*

import ui.pages.*
import ui.pages.auth.AuthPage


suspend fun main() {

    val path = window.location.pathname

    //lazy routing
    when(path.trim('/')) {

        //auth page
        "spotify/auth" ->
            window.onload = { AuthPage.loadPage() }

        //profile/me page
        "spotify/profile/me" ->
            window.onload = { Profile_MePage.loadPage() }
        //profile page
        "spotify/profile" ->
            window.onload = { ProfilePage.loadPage() }

        //playlist page
        "spotify/playlist" ->
            window.onload = { PlaylistPage.loadPage() }

        "spotify/search" ->
            window.onload = { SearchResultsPage.loadPage() }

        //tools page
        "spotify/profile/me/liked" ->
            window.onload = { LikedTracksPage.loadPage() }

        //index page
        "" ->
            window.onload = { IndexPage.loadPage() }

    }

}


