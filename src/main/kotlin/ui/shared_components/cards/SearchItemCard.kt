package ui.shared_components.cards

import kotlinx.html.DIV
import kotlinx.html.js.onClickFunction
import kotlinx.html.span

fun DIV.searchItemCard(item: Any?) {
    onClickFunction = { ev ->

    }

    span("cardTitle") { item.toString() }

    span("cardSubTitle") {  }
}