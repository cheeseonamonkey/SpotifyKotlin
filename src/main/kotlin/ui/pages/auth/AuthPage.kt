package ui.pages.auth

import Requester
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.html.*
import kotlinx.html.dom.append
import misc.js.Cookie
import org.w3c.dom.url.URLSearchParams
import ui.pages.IPage

object AuthPage : IPage {
    override fun loadPage() {





        val client_id = "a2d2022d5723447d82620a79ba00a6f5"
        val scope = "user-read-private user-read-email user-top-read user-read-recently-played"


        //no params - log in
        if (!window.location.href.contains('#')) {
            document.body?.append {

                div("flexRowHolder") {
                    loginDiv(client_id, scope)
                    searchDiv()
                    br { style = "clear: both;" }
                }
        }}
        //url params present (possible auth success callback)
        else {

            handleAuthCallback()//end coroutine

        }


        //init search token (saves in cookies)
        Requester.getClientCreds()

        Unit
    }

    private fun handleAuthCallback() {
        GlobalScope.launch {
            document.title = "Redirecting..."
            val params = URLSearchParams(window.location.href.replace("#", "?").split('?')[1])
            val token = object {
                val access_token = params.get("access_token").toString()  //Token
                val token_type = params.get("token_type").toString()    //Bearer
                val expires_in = params.get("expires_in").toString()    //3600
            }
            //console.log(token)

            Cookie.set("authTokenObj", JSON.stringify(token))
            Cookie.set("authCode", token.access_token)

            val requester = Requester(token.access_token)

            val myprofile = runCatching { requester.getMe() }
                .onSuccess { Cookie.set("myUid", it.id) }
                .onFailure {
                    document.body?.append {
                        p("errorMessage") { +"\n\nError:\n\n${it.message}" }
                    }
                }

            //navigate
            navigateLocal("/spotify/profile/me")

        }
    }




}