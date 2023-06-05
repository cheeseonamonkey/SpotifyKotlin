@file:OptIn(DelicateCoroutinesApi::class)

package ui.shared_components

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.html.*
import kotlinx.html.dom.prepend
import kotlinx.html.js.*
import kotlinx.html.js.onTouchEndFunction
import kotlinx.html.js.onTouchStartFunction
import learning.embedding.Model
import models.Track
import org.w3c.dom.*
import ui.menus.toolsMenus.*


fun <T> TagConsumer<HTMLElement>.dataListView(
    title: String,
    dataList :Collection<T>,
    cardEach: DIV.(cardData:T) -> Unit
) {

    div("dataViewHolder") {
        dataViewTitleBar( dataList, title)
        dataViewInsetList(dataList, cardEach)
    }
}

private fun <T> TagConsumer<HTMLElement>.dataViewTitleBar(dataList:Collection<T>, title: String) {

    div("divDataViewTitle") {
        style = "background-color: rgba(88,88,105,0.25); text-align: right; padding:  .1em .1em 0 .1em; margin: 0.1em 4px 1px 3px;" +
                "border: 1px solid rgba(5,5,5,0.25); border-bottom: none;"
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
            ToolsButton( "btnEmbedding", "/spotify/res/speech.png","embedding") {
                GlobalScope.launch {
                    console.log("\n btnEmbedding\n----------------------")

                    val model = Model.loadModel()

                    if (dataList.first() is Track) {
                        lateinit var embedProg : HTMLProgressElement

                        document.querySelector(".dataViewHolder")?.prepend {
                            embedProg = progress {
                                span { +"title embedding progress" }
                                style = "height: 1em; width: 85%; padding: 0;"
                                value = "0"
                                max   = ((dataList.size / 7.0).toInt()).floorDiv(1).toString()
                            }
                        }

                        dataList.toList().chunked(7).forEach { chunk ->

                            chunk.forEach { itt ->
                                GlobalScope.async {
                                    val titleEmbedded = model.embed((itt as Track).name)
                                    (itt as Track).embedding = titleEmbedded
                                }.await()
                            }
                            embedProg.value += 1;
                        }
                        embedProg.remove()
                            document.querySelector(".dataViewHolder .h4DataViewTitle")?.remove()
                        document.querySelector(".dataViewHolder")?.prepend {
                            span { +"semantic title search"; style = "float: left;"; }

                            var timeout :Int? = null

                            val inputEmbeddingSearch = input {
                                style = "float: left;"
                                onChangeFunction = { ev->
                                    if(timeout != null)
                                        window.clearTimeout(timeout!!)
                                    timeout = window.setTimeout({
                                        console.log("embedding query: '${ev.target.asDynamic().value}'")
                                    },250)
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
