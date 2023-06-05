package ui.menus.toolsMenus

import kotlinx.browser.document
import kotlinx.html.div
import kotlinx.html.*
import kotlinx.html.dom.append
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onTouchEndFunction
import kotlinx.html.js.onTouchStartFunction
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.Node
import org.w3c.dom.asList

class ToolsButton (
    private val htmlID          :String,
    val imgSrc          :String,
    val label           :String,
    val onClickBlock    :DIV.() -> Unit
) {
    val selector: String get() = "#${htmlID}.divToolsButton"
    val element: Element? get() = document.getElementById(htmlID)
    fun destroy() = element?.remove()
    fun render(p: DIV) {


        p.div("divToolsButton") {
            onTouchStartFunction = {
                it.target.asDynamic().style.boxShadow =
                    "inset 0.25em 0.25em 0.5em -1em rgba(20,20,20,0.15)"
            }
            onTouchEndFunction = {
                it.target.asDynamic().style.boxShadow = ".8px .8px 1px 2px rgba(1,1,1,0.3)"
            }
            style = """
                    float: right;
                    width: 1.5em; height: 1.5em;
                    box-shadow: .8px .8px 1px 2px rgba(1,1,1,0.3);
                    border: 2px solid rgba(11,22,00,0.4); padding: 0.13em; margin: 0 0.04em 0 0.04em;
                """
            img(classes = "imgToolsMenuIcon", src = imgSrc) {
                style = " height: 100%; width: 100%; border: thin solid rgba(19,19,19,0.45); padding: 0; margin: 0; "
            }
            onClickFunction = {
                onClickBlock()
            }
        }
    }
}









