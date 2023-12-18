import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.await
import kotlinx.coroutines.delay
import kotlinx.html.dom.append
import kotlinx.html.li
import kotlinx.html.*
import kotlinx.html.style
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import misc.TimeLength
import misc.js.Cookie
import models.AudioFeatures
import models.Playlist
import models.Profile
import models.Track
import models.musicalfeatures.MusicalFeatures
import org.w3c.dom.HTMLUListElement
import org.w3c.fetch.Headers
import org.w3c.fetch.RequestInit
import org.w3c.fetch.Response
import kotlin.coroutines.suspendCoroutine

class Requester(
    val authCode :String? = run {
        ( Cookie.get("authCode") ?: "" )
                .ifEmpty { Cookie.get("clientCredKey") ?: "" }
                .ifEmpty { getClientCreds() }
                .ifEmpty{ throw Exception("No authCode (user nor client) provided, nor able to be generated, and none were found in cookies.") }
        }
) {

    //autofills the auth code; this should work for every get!
    private val reqSettings =
        RequestInit().apply {
            method = "GET"
            headers = Headers().apply { append("Authorization", "Bearer $authCode") }
        }


    suspend inline fun <reified T> getAsObject(url: String, reqInit :RequestInit = RequestInit(
        method = "GET",
        headers = Headers().apply {
            append("Authorization", "Bearer $authCode")
            append("Content-Type", "application/json")
        }
    )): T {


        val response = window.fetch(url, reqInit).await()
        if (!response.ok) {
            val text = response.text().await()
            throw Exception("HTTP error ${response.status}:\n > $text")
        }else {
            val json = (Json { ignoreUnknownKeys = true })
            val myDataClass = json.decodeFromString<T>(response.text().await().toString())
            return myDataClass
        }
    }

    suspend fun likeTracks(trackids :List<String>) : Boolean {
        var allSucceeded = true;

        suspend fun List<String>.sanitizeDuplicates() :List<String> {

            // remove already liked songs
            val lstSanitized = mutableListOf<String>()

            // get liked songs
            val likesMeLikesMeNot = getIsLiked(this.toMutableList())

            for(i in 0 until lstSanitized.size){
                if(likesMeLikesMeNot[i] == false) //not already liked
                    lstSanitized.add(this[i]);
            }

            //also remove nulls
            lstSanitized.forEach {
                if(it == undefined || it == null || it == "null")
                    lstSanitized.remove(it)
            }

            console.log("${lstSanitized.size} songs are \"unliked\"")
            return lstSanitized
        }

        val trackidsSanitized = trackids.sanitizeDuplicates()

        trackidsSanitized.chunked(50).forEach { chunkedIds ->
            runCatching {

                val r = window.fetch("https://api.spotify.com/v1/me/tracks", RequestInit().apply {
                    method = "PUT"
                    headers = Headers().apply { append("Authorization", "Bearer $authCode") }
                    body = JSON.stringify(chunkedIds)
                }).await()
                if(r.status.toInt() != 200)
                    allSucceeded = false;
                delay(5)
            }.onFailure { console.error(it.message) }

        }

        return allSucceeded
    }


    suspend fun getMusicalFeatures(trackid :String) : MusicalFeatures {
        var baseUrl = "https://api.spotify.com/v1/audio-analysis/${trackid}"

        val mf = getAsObject<MusicalFeatures>(baseUrl)
        return mf
    }
    suspend fun getAudioFeatures(ids :List<String>) :List<AudioFeatures>  {

        var listout = mutableListOf<AudioFeatures>()

        ids.chunked(99).forEach { listChunk ->
            var baseUrl = "https://api.spotify.com/v1/audio-features?ids=${
                        let {
                            var a = ""
                            listChunk.forEach { a += "$it," }
                            //remove last comma 
                            a = a.removeRange(a.length - 2 until a.length)
                            a
                        }}"

            window.fetch(baseUrl, reqSettings)
                .then { resp -> resp.json() }
                .then { j ->
                    val jDyn = j.asDynamic()

                    val featureList_chunk = AudioFeatures.listFromDynamic(jDyn.audio_features)

                    listout.addAll(featureList_chunk)

                }.await()

        }

        return listout
    }

    suspend fun getSearchQuery(q :String, qType :List<String>) :List<Playlist> {

        var listout = mutableListOf<Playlist>()

        var baseUrl = "https://api.spotify.com/v1/search" +
                "?q=${q}&type=${ let {
                    var a = ""
                    qType.forEach { a += "$it," }
                    a = a.removeRange(a.length - 2 until a.length)
                    a
                }}" +
                "&limit=50"

        var playlistList: List<Playlist> = listOf()

        window.fetch(baseUrl, reqSettings)
            .then { resp -> resp.json() }
            .then { j ->
                val jDyn = j.asDynamic()
                //console.log(jDyn)
                playlistList = Playlist.listFromDynamic(jDyn.playlists)
                listout.addAll(playlistList)

            }.await()

        return listout
    }

    suspend fun getMyPlaylists() :List<Playlist>{

    var listout = mutableListOf<Playlist>()

    var pageurl = "https://api.spotify.com/v1/me/playlists?limit=50"
    var looping = true

    while(looping) {

            window.fetch(pageurl, reqSettings)
                .then { r -> r.json() }
                .then { j ->


                    val pll = Playlist.listFromDynamic(j)

                    listout.addAll(pll)

                    if (j.asDynamic().next == null)
                        looping = false
                    else
                        pageurl = j.asDynamic().next


                }.await()
            delay(5)
        }


    return listout
}

    suspend fun getIsLiked(trackids:MutableList<String>): List<Boolean> {

        trackids.forEach {
            if (it == undefined || it == null || it == "null")
                trackids.remove(it)
        }

        val lstOut = mutableListOf<Boolean>()



        trackids.chunked(49).forEach { chunkedIds ->
            try {
                val trackIdsString = chunkedIds.joinToString(",")
                val url = "https://api.spotify.com/v1/me/tracks/contains?ids=${trackIdsString}".replace(",", "%2C")
                val lstOutchunk = getAsObject<List<Boolean>>(url, reqSettings)
                lstOut.addAll(lstOutchunk)
            }catch(ex:Exception){ console.error(ex.message) }
        }
        return lstOut
    }

    suspend fun getMyLikedTracks(progressBlock:(iProg:Int?, iTotal:Int?, iWasCached:Boolean)->Any?) :List<Track> {

        val cache = window.caches.open("likedTracks").await()

        var listout = mutableListOf<Track>()

        var pageurl = "https://api.spotify.com/v1/me/tracks?limit=50&offset=0"
        var looping = true

        while (looping) {

            val cachedResponse = (cache.match(pageurl).await()) as Response?
            // Check if response is already cached
            val j = if (cachedResponse != null) {

                //is cached
                val j = cachedResponse.json().await()
                if(j.asDynamic().offset == undefined) {
                    console.log("cache corrupt - deleting it.")

                    break;
                }
                console.log("got cached - ${j.asDynamic().offset} - ${j.asDynamic().total}")
                progressBlock(j.asDynamic().offset, j.asDynamic().total, true)
                listout.addAll(Track.listFromDynamic(j))

                j //return
            } else {

                // is not cached - make new request (and cache response)
                val response = window.fetch(pageurl, reqSettings).await()
                cache.put(pageurl, response.clone())
                val j = response.json().await()
                console.log("cached http - ${j.asDynamic().offset} - ${j.asDynamic().total}")
                progressBlock(j.asDynamic().offset, j.asDynamic().total, false)
                listout.addAll(Track.listFromDynamic(j))
                delay(15)
                j //return
            }

            if (j.asDynamic().next == null)
                looping = false
            else
                pageurl = j.asDynamic().next




        }
        return listout
    }

    suspend fun getUsersPlaylists(uid :String) :List<Playlist> {

        var listout = mutableListOf<Playlist>()

        var pageurl = "https://api.spotify.com/v1/users/${uid}/playlists?limit=50"
        var looping = true

        while(looping) {
            window.fetch(pageurl, reqSettings)
                .then { r -> console.log(r); r.json(); }
                .then { j ->

                    val pll = Playlist.listFromDynamic(j)

                    listout.addAll(pll)

                    if(j.asDynamic().next == null)
                        looping = false
                    else
                        pageurl = j.asDynamic().next



                }.await()
            delay(10)
        }
        return listout
    }

    suspend fun getPlaylist(pid :String) : Playlist {
        return window.fetch("https://api.spotify.com/v1/playlists/${pid}", reqSettings)
            .then { r -> r.json() }
            .then { j -> Playlist.fromDynamic(j) }.await()
    }

    suspend fun getPlaylistTracks(tracksUrl :String, progressBlock:(iProg:Int?, iTotal:Int?, iWasCached:Boolean)->Any? = {a,b,c->} ): List<Track> {

        val cache = window.caches.open("PlaylistTracks").await()

        var listout = mutableListOf<Track>()

        var pageurl = tracksUrl
        var looping = true

        while (looping) {
            val cachedResponse = (cache.match(pageurl).await()) as Response?
            // Check if response is already cached
            val j = if (cachedResponse != null) {
                //is cached
                val j = cachedResponse.json().await()
                console.log("got cached - ${j.asDynamic().offset} - ${j.asDynamic().total}")
                progressBlock(j.asDynamic().offset, j.asDynamic().total, true)
                listout.addAll(Track.listFromDynamic(j))

                j //return
            } else {
                // is not cached - make new request (and cache response)
                val response = window.fetch(pageurl, reqSettings).await()
                cache.put(pageurl, response.clone())
                val j = response.json().await()
                console.log("cached http - playlist tracks;  ${j.asDynamic().offset}-${j.asDynamic().total}")
                progressBlock(j.asDynamic().offset, j.asDynamic().total, false)
                listout.addAll(Track.listFromDynamic(j))

                delay(8)

                j //return
            }
            if (j.asDynamic().next == null)
                looping = false
            else
                pageurl = j.asDynamic().next

        }
        return listout
    }

    suspend fun getTopTracks(timeLength: TimeLength, offset :Int = 0) : List<Track> {


        var listout = mutableListOf<Track>()

        var url =
            "https://api.spotify.com/v1/me/top/tracks" + "?limit=49&time_range=${timeLength}&offset=${offset}"

        var looping = true
        while (looping) {
            val a = window.fetch(url, reqSettings)
                .then { r -> r.json() }
                .then { j ->

                    //get List<Track> from resonse
                    val tl: List<Track> =
                        Track.listFromDynamic(j)

                    //concat to existing list
                    listout.addAll(tl)

                    if (j.asDynamic().next == null)
                        looping = false
                    else
                        url = j.asDynamic().next
                }.await()
        }
        //console.log(listout)
        return listout

    }



    suspend fun getMe() =
        getAsObject<Profile>("https://api.spotify.com/v1/me")


    suspend fun getUser(uid :String) =
        getAsObject<Profile>("https://api.spotify.com/v1/users/${uid}")

    suspend fun get(url: String) =
        window.fetch(url, reqSettings)
            .then { r -> r.json() }
            .then { ob -> ob }



    companion object{

        private val encodedHash = "YTJkMjAyMmQ1NzIzNDQ3ZDgyNjIwYTc5YmEwMGE2ZjU6N2Y2ZGE5NTE0MGNjNDllMThhZjIxNzZiMjBmNTZlZWY="

        fun getClientCreds() :String {
            if ( ! Cookie.get("clientCredKey").isNullOrEmpty()) {
                return Cookie.get("clientCredKey") ?: ""
            } else {
                val url = "https://accounts.spotify.com/api/token"
                val authReqSettings =
                    RequestInit().apply {
                        method = "POST"
                        headers = Headers().apply {
                            append("Authorization", "Basic $encodedHash")
                            append("Content-Type", "application/x-www-form-urlencoded")
                        }
                        body = "grant_type=client_credentials"

                    }

                window.fetch(url, authReqSettings)
                    .then { r -> r.json() }
                    .then { j ->
                        val clienttoken = j.asDynamic().access_token
                        Cookie.set("clientCredKey", clienttoken)
                        console.log("clientCreds got successfully. refreshing...")
                        window.location.href = window.location.href


                    }
                return Cookie.get("clientCredKey") ?: ""
            }

        }


    }

}





