package learning.embedding

external fun require(module: String): dynamic

internal val similarity = require("compute-cosine-similarity") as ( (dynamic,dynamic)->Double )

fun cosineSimilarity(a :Array<Float>, b:Array<Float>) = similarity(a.asDynamic(),b.asDynamic())

fun Array<Float>.cosSimilarityTo(b:Array<Float>) = cosineSimilarity(this, b)



