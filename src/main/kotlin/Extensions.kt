import kotlinx.browser.document
import org.w3c.dom.Element
import org.w3c.dom.asList

fun Element.toggleHidden(){

            val dyn = this.asDynamic()
            if(dyn.style.display == "block")
                dyn.style.display = "none"
            else
                dyn.style.display = "block"
        }

