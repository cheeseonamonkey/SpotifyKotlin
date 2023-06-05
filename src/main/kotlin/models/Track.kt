package models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Cursors (
    val after: String,
    val before: String
)
@Serializable
data class Item (
    val track: Track,

    @SerialName("played_at")
    val playedAt: String,

    @Transient
    val context: Any? = null
)
@Serializable
data class Track (
    val album: Album,
    val artists: String,
    @SerialName("duration_ms")
    val durationMS: Long,
    val explicit: Boolean,
    val href: String,
    val id: String,
    val name: String,
    val popularity: Long,
    @SerialName("preview_url")
    val previewURL: String,
    val uri: String
) :IDataModel {


    var audioFeatures :AudioFeatures? = null
    var embedding     :Array<Float>?  = null



    @Serializable
    companion object : IInitsListFromDynamic  {
        override fun fromDynamic(inobjGeneric:dynamic) :Track{


            // json responses are a bit inconsistent between endpoints -
            //  there's sometimes a property hiding each track (even though the containing objects ARE the same object type),
            //  so this lets us abstract to our existing data models:
            val inobj = inobjGeneric.track ?: inobjGeneric

/*         // you can turn on these logs if you still need to understand:
            console.log("\ninobjgeneric:");
            console.log(inobjGeneric)
            console.log("\ninobjgeneric.track:");
            console.log(inobjGeneric.track)
*/




            var artistsStr = ""

            for(i in 0..inobj.artists.length-1)
                artistsStr += inobj.artists[i].name.toString() + ", "

            return Track(
                inobj.album,
                artistsStr,
                inobj.duration_ms,
                inobj.explicit,
                inobj.href,
                inobj.id,
                inobj.name,
                inobj.popularity,
                inobj.preview_url,
                inobj.uri

            )
        }

         override fun listFromDynamic(inListObj:dynamic) :List<Track>{
            val tracksout = mutableListOf<Track>()
            //console.log("inlistobj:")
            //console.log(inListObj)
            for(t in inListObj.items) {
                //console.log(t)

                val plkt = fromDynamic(t)
                tracksout.add(plkt)
            }
            //        it -> trackout.add(Track.fromDynamic(it))

            //trackout.forEach { console.info(it) }

            return tracksout.toList<Track>()
        }
    }
}
@Serializable
data class Album (

    val artists: List<Artist>,
    val href: String,
    val id: String,
    val images: List<Image>,
    val name: String,
    val uri: String
)
@Serializable
data class Artist (
    val id: String,
    val name: String,
    val uri: String
)




/*

data class Image (
    val height: Long,
    val url: String,
    val width: Long
)




*/