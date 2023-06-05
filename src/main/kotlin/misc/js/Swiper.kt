package misc.js

import kotlinx.html.TagConsumer
import kotlinx.html.div
import org.w3c.dom.HTMLElement

object Swiper {


    inline fun TagConsumer<HTMLElement>.htmlTabs(
        vararg tabsContents : TagConsumer<HTMLElement>.()->Unit,
    ) {


        div("jTabsHolder") {

            tabsContents.forEach { flowBlock ->
                div("jTab") {
                    flowBlock()
                }
            }




        }
    }

    inline fun TagConsumer<Any>.initTabs() {

        js("""
            $('.jTabsHolder').slick({
                ltr: true,
                arrows: false,
                dots: true,
                dotsClass: "slick-dots",
                
                customPaging : function(slider, i) {
                    return ( "<a><img /> </a>" ); 
                },
                                
            });
        """)



    }


}