package ui.shared_components

import kotlinx.html.*

fun FlowContent.staticProgressBar(
    inVal: Float,
    fillColor: String = "#dd77aa88",
    maxVal: Float = 1.0f,
    //styleAppendage :String? = null
) {
    div("progBarStatic") {
        style = "padding: 0.025em; margin: 0.025em 0.075em 0.025em 0.025em;"
        div("trackCardProgressBar") {
            style = "vertical-align: middle; display: inline; height: 0.75em; width: 58%; float: right; text-align: right; margin: 0 1% 0 0; padding: 0 0 0 0; " +
                    "border-top: thin solid rgba(22,22,11,0.55);" +
                    "border-bottom: thin solid rgba(22,22,11,0.55); " +
                    "border-left: thin solid rgba(22,22,11,0.55); " +
                    "border-start-end-radius: 4px;" +
                    "border-end-start-radius: 4px;" +
                    "box-shadow: inset -1px 1px 1px 0 rgba(8,8,8,0.12);"

            val pct = ((inVal / maxVal) * 100).toInt()
            div {
                style = "vertical-align: middle; margin: 0; padding: 0; height: 100%; width:${pct}%; background-color: unset; background-color: ${fillColor}; border-right: thin solid rgba(33,44,44,0.38); " +
                        "border-start-end-radius: 4px;" +
                        "border-end-start-radius: 4px;" +
                        "box-shadow: 1px 0 1px 0 rgba(8,8,8,0.15);"
            }

        }
        br{style="clear: both;"}
    }}