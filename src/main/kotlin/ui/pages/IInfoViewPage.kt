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
                style = "padding: 0; margin: 0; width: 8em; display: inline; float: left;"
                img(classes = "profileImage") {
                    style = "display: block; "
                    src = imgUrl ?: "#"
                }
                span {
                    style = "display: block; font-size: 1.35em; font-weight: bold;"
                    +"${imgTxt}"
                }





            }
            //and whatever else was included...
            block()
        }








}