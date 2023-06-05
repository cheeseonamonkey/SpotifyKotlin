package learning.embedding


import kotlin.js.Date
import kotlin.time.Duration.Companion.milliseconds

class Timer {

    private val startTime = Date.now().milliseconds

    fun end(): Long {
        val endTime = Date.now().milliseconds
        val elapsedTime = endTime - startTime
        return elapsedTime.inWholeMilliseconds
    }
}




suspend fun <T> runTimed(finishMsg:String, block:suspend()->T) :T{
    val timer = Timer()
    val blkResult = block()
    console.info("${finishMsg} \t- ${timer.end()}ms")
    return blkResult
}


