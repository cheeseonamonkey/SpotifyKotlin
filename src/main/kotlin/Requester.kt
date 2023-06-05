import kotlin.js.*
import misc.js.Cookie
import kotlinx.browser.window
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.await
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

import misc.TimeLength
import models.*
import models.musicalfeatures.MusicalFeatures

import org.w3c.fetch.Headers
import org.w3c.fetch.RequestInit



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


    suspend fun getUsersPlaylists(uid :String) :List<Playlist> {

        var listout = mutableListOf<Playlist>()

        var pageurl = "https://api.spotify.com/v1/users/${uid}/playlists?limit=50"
        var looping = true

        while(looping) {
            window.fetch(pageurl, reqSettings)
                .then { r -> r.json() }
                .then { j ->

                    val pll = Playlist.listFromDynamic(j)

                    listout.addAll(pll)

                    if(j.asDynamic().next == null)
                        looping = false
                    else
                        pageurl = j.asDynamic().next



                }.await()
        }
        return listout
    }

    suspend fun getPlaylist(pid :String) : Playlist {
        return window.fetch("https://api.spotify.com/v1/playlists/${pid}", reqSettings)
            .then { r -> r.json() }
            .then { j -> Playlist.fromDynamic(j) }.await()
    }

    suspend fun getPlaylistTracks(tracksUrl :String) : List<Track> {

        var listout = mutableListOf<Track>()

        var pageurl = tracksUrl
        var looping = true

        while (looping) {
            val a = window.fetch(pageurl, reqSettings)
                .then { r -> r.json() }
                .then { j ->
                    val tl = Track.listFromDynamic(j)
                    listout.addAll(tl)

                    if (j.asDynamic().next == null)
                        looping = false
                    else
                        pageurl = j.asDynamic().next
                }.await()
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





