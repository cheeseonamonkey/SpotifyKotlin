@file:Suppress("PLUGIN_IS_NOT_ENABLED")

package models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import misc.TimeLength

@Serializable
data class Profile (

    val id: String,
    @SerialName("display_name")
    val displayName: String,
    val followers: Followers,
    val images: List<Images>,
    val uri: String
) :IDataModel {



    val recentlyPlayed : List<Track>? = null
    var numPlaylists : Int? = null


    var playlists   :List<Playlist>? = null

    val topTracks = mutableMapOf<TimeLength,List<Track>>(
        Pair( TimeLength.MONTH, listOf() ),
        Pair( TimeLength.YEAR, listOf() ),
        Pair( TimeLength.ALL_TIME, listOf() )
    )







    fun toJson() = JSON.stringify(this)


    companion object  : IInitsFromDynamic {
        fun fromJson(json: String) = (JSON.parse<Profile>(json))
        override fun fromDynamic(d :dynamic) : Profile {
            val profOut =
                Profile(
                    d.id,
                    d.display_name,
                    d.followers.total,
                    d.images[0].url,
                    d.uri )
            return profOut
        }
    }
}


