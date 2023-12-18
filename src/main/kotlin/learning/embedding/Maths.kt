/*
package learning.embedding


import kotlin.math.*

import kotlin.math.*

fun cosineSimilarity(a: FloatArray, b: FloatArray): Float {
    require(a.size == b.size) { "Arrays must have the same size" }
    var dotProduct = 0.0f
    var normA = 0.0f
    var normB = 0.0f
    for (i in a.indices) {
        dotProduct += a[i] * b[i]
        normA += a[i] * a[i]
        normB += b[i] * b[i]
    }
    return dotProduct / (sqrt(normA) * sqrt(normB))
}



// Array<Float> extension
fun FloatArray.cosineSimilarity(comparator:FloatArray) :Float = cosineSimilarity(this, comparator)




*/