package ui.pages

import kotlinx.html.*


//page containing that top section for general stats/information on profile/playlist/etc. pages
interface IInfoViewPage : IPage {

    fun FlowContent.divInfoView(
        imgUrl :String?,
        imgTxt :String,
        block: DIV.() -> Unit
    ) :Unit =
        div("infobar") {
            //user image and name
            div {
                style = "padding: 0; margin: 0; display: inline; float: left; width: 34%; max-width: 225px; text-align: center;"
                img(classes = "profileImage") {
                    style = "width: 93%; max-width: 210px; display: block; border: 3.3px solid rgba(11,77,11,0.55); border-radius: 15%; background-color: rgba(4,45,4,0.05);"
                    src = imgUrl ?: "#"
                }
                span {
                    style = "display: block; font-size: 1.4em; font-shadow: .3px .3px .6px 1px #131; font-weight: bold; white-space: nowrap;"
                    +"${imgTxt}"
                }





            }
            //and whatever else was included...
            block()

        }








}