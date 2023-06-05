package learning.embedding


import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.js.Promise



class Model private constructor(
    private val use:UniversalSentenceEncoder,
) {
    suspend fun embedAll(inputs: List<String>): List<Array<Float>> = withContext(Dispatchers.Default) {
        val embeddings = inputs.map { input ->
            async {
                embed(input)
            }
        }
        embeddings.awaitAll()
    }
    suspend fun embedAll(vararg inputs: String): List<Array<Float>> = embedAll(inputs.toList())

    suspend fun embed(input: String): Array<Float> = withContext(Dispatchers.Default) {
        runTimed<Array<Float>>("embedded (\"${input.slice(0..10)}..\") \t") {
            val outDynamic = use.embed(input).await()
            //console.log("outDynamic:")
            //console.log(outDynamic)
            val arrOut = outDynamic.array().await()
            //console.log("arrOut:")
            //console.log(arrOut)

            arrOut
        }
    }
    companion object {
        // static builder
        suspend fun loadModel(): Model {
            val a = runTimed<Model>("model loaded        \t") {
                val use = UniversalSentenceEncoder.load().await();
                Model(use);
            }
            return a;
        }
    }
}


internal external class EmbeddingResult {
    suspend fun array() :Promise<Array<Float>>
}


@JsModule("@tensorflow-models/universal-sentence-encoder")
@JsNonModule
internal external class UniversalSentenceEncoder {
    suspend fun embed(input: String): Promise<EmbeddingResult>

    companion object {
        suspend fun load(): Promise<UniversalSentenceEncoder>
        suspend fun load(model: String): Promise<UniversalSentenceEncoder>
    }
}

