package misc.js

import org.w3c.dom.Audio

object Media {



    fun playSoundFromUrl(url:String) = Audio(url).play()



}