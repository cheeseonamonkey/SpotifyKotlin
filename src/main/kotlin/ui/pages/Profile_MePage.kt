package ui.pages

import misc.js.Cookie
object Profile_MePage : IPage {
    override fun loadPage() {

        val myUid = Cookie.get("myUid")

        console.log(myUid)

        if (myUid.isNullOrEmpty())
            navigateLocal("/spotify/auth")
        else
            navigateLocal("spotify/profile?uid=$myUid")
    }
}