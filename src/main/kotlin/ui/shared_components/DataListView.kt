@file:OptIn(DelicateCoroutinesApi::class)

package ui.shared_components

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.*
import kotlinx.html.*
import kotlinx.html.dom.append
import kotlinx.html.js.*
import kotlinx.html.js.onTouchEndFunction
import kotlinx.html.js.onTouchStartFunction
import learning.embedding.sentenceencoder.Model
import learning.embedding.cosSimilarityTo
import models.Track
import org.w3c.dom.*
import ui.menus.toolsMenus.*


fun <T> TagConsumer<HTMLElement>.dataListView(
    title: String,
    dataList :Collection<T>,
    cardEach: DIV.(cardData:T) -> Unit
) {

    div("dataViewHolder") {
        dataViewTitleBar(dataList, title, cardEach)
        dataViewInsetList(dataList, cardEach)
    }
}

private fun <T> TagConsumer<HTMLElement>.dataViewTitleBar(dataList:Collection<T>, title: String, cardEach: DIV.(cardData: T) -> Unit) {

    div("divDataViewTitle") {
        style = "cursor: grab; background-color: rgba(88,133,91,0.25); text-align: right; padding: .1em .1em 0 .1em; margin: 0.1em 4px 1px 3px;" +
                "border: 2.5px solid rgba(5,5,5,0.65); border-bottom: none; box-shadow: .5px .4px .7px 1.9px rgba(1,1,1,0.15);"
        h4("h4DataViewTitle") {
            style = """ text-align: left; display: inline; float: left; max-width: 60%; height: 100%; margin: 0 3% 0 3%;"""
            +title
        }

        div {
            style = "display: inline-block; padding: 0; margin: 0.04em 0.5em 0 0;"

            // audio features:
            ToolsButton( "btnAudioFeatures", "/spotify/res/music.png","audio feature") {
                document.querySelectorAll(".ulFeatures").asList().forEach {
                    (it as HTMLElement).hidden = ( !(it as HTMLElement).hidden)
                }
            }.render(this)


            // embedding:
            if(dataList.first() is Track)
            ToolsButton( "btnEmbedding", "/spotify/res/speech.png","embedding") {
                GlobalScope.launch {

                    val model = Model.loadModel()

                            document.querySelector(".divSecondary .h4DataViewTitle")?.remove()
                            document.querySelector(".divSecondary")?.append {
                            div {
                                style = "max-width: 55%;  margin: 0.04em; padding: 0.08em; display: inline-block;  border: 1px thin black;"
                                span {
                                    style = "float: left; font-family: sarif;"
                                    i { style = "font-size: 75%;"; +"semantic title searching" }
                                    br()
                                    b { +"find songs about:" }

                                }

                                val timeouts: MutableList<Int> = mutableListOf<Int>()
                                input {

                                    style = "float: left; font-size: 120%; line-height: 1.3em;"
                                    onKeyUpFunction = { ev ->
                                        if (timeouts.size > 0)
                                            runCatching { timeouts.forEach { timeOutID -> window.clearTimeout(timeOutID) } }

                                        timeouts.add(window.setTimeout({
                                            GlobalScope.async {
                                                GlobalScope.async {


                                                    val q = "${ev.target.asDynamic().value}"
                                                    if (q.length < 2)
                                                        return@async

                                                    //console.log("embedding query..\n   \"$q\"")
                                                    val queryEmbedded = model.embed(q)
                                                    //console.log("query embedded: \n   ${queryEmbed}")
                                                    val tl = dataList.toList() as List<Track>

                                                    val logStr = StringBuilder("\n---------------------------\n")
                                                    logStr.append(" CosSim% |  Title  \n")
                                                    tl.forEach { trk ->
                                                        trk.similarity =
                                                            (trk.embedding?.cosSimilarityTo(queryEmbedded))?.times(100)
                                                        logStr.append(
                                                            " ${
                                                                (trk.similarity ?: 0.0).toString().slice(0..5)
                                                                    .padStart(7)
                                                            }% |  ${trk.name.padEnd(35)} \n"
                                                        )

                                                    }
                                                    console.log("\n" + logStr)


                                                }.await()

                                                console.log("sorting dataList")
                                                document.querySelector(".dataViewHolder")?.remove()

                                                delay(300)

                                                document.body?.append {
                                                    dataListView(
                                                        title,
                                                        dataList.toList().sortedBy { (it as Track).similarity }
                                                            .reversed(),
                                                        cardEach
                                                    )
                                                }
                                            }


                                        }, 1000))

                                    }
                                }
                                br { style = "clear: both;" }
                            }


                        }



                }
            }.render(this)

            // training:
            ToolsButton( "btnTraining", "/spotify/res/brain.png","training") {

            }.render(this)

            // popularity:
            ToolsButton( "btnPopularity", "/spotify/res/flame.png","popularity") {
                document.querySelectorAll(".divTrackCardPopularity").asList().forEach {
                    (it as HTMLElement).hidden = ( !(it as HTMLElement).hidden)
                }
            }.render(this)

        }



    }
}


private fun <T> DIV.dataViewInsetList(
    dataList: Collection<T>,
    cardEach: DIV.(cardData: T) -> Unit
) {
    div("dataviewlist") {
        dataList.forEach {
            style = """
                     box-shadow:
                        inset 2px 2px 0.45em 0.15em rgb(25 60 90 / 70% ),
                        inset 4px 5px 0.8em 2.5em rgb(25 50 60 / 8% );
                     padding: 0.15em;
                     margin: 0.45em; margin-top: 0.05em;
                     background-color: rgb(50 45 65 / 8%);
                     max-height: 66vh; overflow-y: auto;
                """
            div("card") {
                onTouchStartFunction = { it.target.asDynamic().style.boxShadow = "inset 0.1em 0.1em 0.3em -0.03em rgba(20,20,20,0.6)" }
                onTouchEndFunction = { it.target.asDynamic().style.boxShadow = "none" }

                cardEach(it)
            }
        }


    }
}
