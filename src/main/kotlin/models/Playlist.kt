package models
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Playlist (
    val collaborative: Boolean,
    val description: String,
    val href: String,
    val id: String,
    val name: String,
    val public: Boolean,
    val uri: String,
    val numTracks: Int,
    val tracksLink :String,
    val imageUri :String,
    val ownerId :String

) : ITrackContainer, IDataModel {


    override lateinit var tracksList : List<Track>



    companion object : IInitsListFromDynamic {

        override fun listFromDynamic(inListObj:dynamic) :List<Playlist>{
            val playlistsout = mutableListOf<Playlist>()
            for(pl in inListObj.items) {
                val plkt = fromDynamic(pl)

                playlistsout.add(plkt)
            }
            return playlistsout.toList<Playlist>()
        }


        override fun fromDynamic(inObj:dynamic) :Playlist{
            return Playlist(
                inObj.collaborative,
                inObj.description,
                inObj.href,
                inObj.id,
                inObj.name,
                inObj.public,
                inObj.uri,
                inObj.tracks.total,
                inObj.tracks.href,
                inObj.images[1]?.url ?: inObj.images[0]?.url ?: "",
                inObj.owner.id ?: ""
            )
        }
    }

}



@Serializable
data class Image (
    val height: Long? = null,
    val url: String,
    val width: Long? = null
)
@Serializable
data class Tracks (
    val href: String,
    val total: Long
)
