package models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AudioFeatures (
    val danceability: Double,
    val energy: Double,
    val key: Long,
    val loudness: Double,
    val mode: Long,
    val speechiness: Double,
    val acousticness: Double,
    val instrumentalness: Double,
    val liveness: Double,
    val valence: Double,
    val tempo: Double,
    val type: String,
    val id: String,
    val uri: String,

    @SerialName("track_href")
    val trackHref: String,

    @SerialName("analysis_url")
    val analysisURL: String,

    @SerialName("duration_ms")
    val durationMS: Long,

    @SerialName("time_signature")
    val timeSignature: Long
) :IDataModel {

    companion object : IInitsFromDynamic {
        override fun fromDynamic(inD :dynamic) :AudioFeatures{
            return AudioFeatures(
                        inD.danceability,
                        inD.energy,
                        inD.key,
                        inD.loudness,
                        inD.mode,
                        inD.speechiness,
                        inD.acousticness,
                        inD.instrumentalness,
                        inD.liveness,
                        inD.valence,
                        inD.tempo,
                        inD.type,
                        inD.id,
                        inD.uri,
                        inD.track_href,
                        inD.analysis_url,
                        inD.duration_ms,
                        inD.time_signature,
            )
        }
        fun listFromDynamic(inListObj:dynamic) :List<AudioFeatures>{
            val playlistsout = mutableListOf<AudioFeatures>()



            for(featur in inListObj) {
                //sometimes there's a null that needs to be left out
                if(featur != null) {
                    val plkt = fromDynamic(featur)

                    playlistsout.add(plkt)
                }
            }
            //        it -> playlistsout.add(Playlist.fromDynamic(it))

            //playlistsout.forEach { console.info(it) }

            return playlistsout.toList<AudioFeatures>()
        }
    }
}
