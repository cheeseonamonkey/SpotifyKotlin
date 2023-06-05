package ui.pages.auth

import kotlinx.html.*
import org.w3c.dom.HTMLElement
import ui.shared_components.searchBarInput

fun stoppedTyping(){
    console.log("stopped typing (for a search feature not yet implemented)")
}

fun TagConsumer<HTMLElement>.searchDiv() {



    //for autosearch feature (not yet implemented)
    //var timer = window.setTimeout({ stoppedTyping() }.asDynamic(),600)





    div("loginDiv searchDiv") {

        h3 { small{+"or, "; style="position: relative; top: 3px; right: calc(6.1em + 12vw);";}; +"Search Spotify" }
        small {
            style = "font-size: 0.499em; width: 99%;text-align: right; margin-top: -1px; letter-spacing: -0.38px; line-height: 0.64; color: rgba(2,30,0,0.7); padding-right: 3px;"
            i { +"(you will not be able to see all of your personal stats this way)" }
        }

        searchBarInput()


        div("searchCheckboxesHolder") {
            style = "display: flex; padding: 3px; padding-top: 7px; padding-bottom: 7px; margin: 2px; margin-left: 5px; box-shadow: inset 4px 4px 2px 6px rgba(20,70,20,0.11);"



            //searching 'playlists' checkbox
            input(type = InputType.checkBox, classes = "searchOptionsChecks") {
                id = "ckbSearchPlaylists"
            }
            label{
                htmlFor = "ckbSearchPlaylists"
                +"playlists"
            }

            //searching 'profiles' checkbox
            input(type = InputType.checkBox, classes = "searchOptionsChecks") {
                id = "ckbSearchProfiles"
            }
            label{
                htmlFor = "ckbSearchProfiles"
                +"user profiles"
            }



        }
    }
}



