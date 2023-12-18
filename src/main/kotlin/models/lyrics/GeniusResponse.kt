package models.lyrics
import kotlinx.serialization.Serializable

@Serializable
data class GeniusResponse(
    private val meta: Meta,
    private val response: Response
){
    val hits :List<Hit> get()= response.hits

    @Serializable
    data class Meta( val status: Int )

    @Serializable
    data class Response ( val hits: List<Hit> )
}




@Serializable
data class Hit(
    private val highlights: List<String>,
    val index: String,
    val type: String,
    val result: Result
)

@Serializable
data class Result(
    val annotation_count: Int,
    val api_path: String,
    val artist_names: String,
    val full_title: String,
    val header_image_thumbnail_url: String,
    val header_image_url: String,
    val id: Int,
    val language: String,
    val lyrics_owner_id: Int,
    val lyrics_state: String,
    val path: String,
    val pyongs_count: Int,
    val relationships_index_url: String,
    val release_date_components: ReleaseDateComponents,
    val release_date_for_display: String,
    val release_date_with_abbreviated_month_for_display: String,
    val song_art_image_thumbnail_url: String,
    val song_art_image_url: String,
    val stats: Stats,
    val title: String,
    val title_with_featured: String,
    val url: String,
    val featured_artists: List<String>,
    val primary_artist: PrimaryArtist
)

@Serializable
data class ReleaseDateComponents(
    val year: Int,
    val month: Int,
    val day: Int
)

@Serializable
data class Stats(
    val unreviewed_annotations: Int,
    val concurrents: Int? = null,
    val hot: Boolean,
    val pageviews: Int
)

@Serializable
data class PrimaryArtist(
    val api_path: String,
    val header_image_url: String,
    val id: Int,
    val image_url: String,
    val is_meme_verified: Boolean,
    val is_verified: Boolean,
    val name: String,
    val url: String
)
