@file:OptIn(DelicateCoroutinesApi::class)

import kotlinx.browser.window
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import models.lyrics.GeniusResponse
import models.lyrics.Hit
import org.w3c.fetch.Headers
import org.w3c.fetch.RequestInit
import kotlin.coroutines.suspendCoroutine

object LyricsRequester {
    private val key get()= window.atob("Q2NIM1J2NzBMTmN5QklUejQwV3VJX0Z2VzlqNUYzbDRPdDVYSVNIVGZuNXhsN3JfRzROQVJLQlBrd0pkYzR1Yg==")
    private val req by lazy { Requester() }

    suspend fun search(q:String) :List<Hit> =
        GlobalScope.async {
            req.getAsObject<GeniusResponse>("https://api.genius.com/search?access_token=$key&q=${q}", RequestInit().apply { method = "GET" });
        }.await().hits


    


}