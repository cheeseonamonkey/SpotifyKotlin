package learning




import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.pow
import kotlin.random.Random

class Regressor (private val numFeatures : Int ) {

    private var weights : DoubleArray
    private var bias = 0.0

    init {
        val random = Random
        val weights = DoubleArray(numFeatures)
        for (x in 0 until numFeatures) {
            weights[x] = random.nextFloat().toDouble()
        }
        this.weights = weights
    }

    fun fit ( x : Array<DoubleArray> , y: DoubleArray , epochs : Int , batchSize: Int ) {
        val batches = batch( x , y , batchSize )
        for ( e in 0 until epochs ) {
            for ( batch in batches ) {
                val gradients = ArrayList<Array<Any>>()
                for ( pair in batch ) {
                    val predictions = forwardPropogate( pair.first )
                    gradients.add( calculateGradients( pair.first , predictions , pair.second ) )
                    println( (predictions - pair.second).pow( 2 ) )
                }
                optimizeParameters( gradients , 0.001 )
            }
        }
    }


    private fun forwardPropogate( x : DoubleArray ) : Double {
        return MathOps.dot( this.weights , x ) + bias
    }

    private fun calculateGradients( inputs : DoubleArray , predY : Double , targetY : Double ) : Array<Any> {
        val dJ_dPred = meanSquaredErrorDerivative( predY , targetY )
        val dPred_dW = inputs
        val dJ_dW = MathOps.multiplyScalar( dPred_dW , dJ_dPred )
        val dJ_dB = dJ_dPred
        return arrayOf( dJ_dW , dJ_dB )
    }

    private fun optimizeParameters( gradients : ArrayList<Array<Any>> , learningRate : Double ) {
        val weightGradientsList = ArrayList<DoubleArray>()
        for( gradient in gradients ) {
            weightGradientsList.add( gradient[0] as DoubleArray )
        }
        val weightGradients = MathOps.multidimMean( weightGradientsList.toTypedArray()  ).toDoubleArray()

        val biasGradientsList = ArrayList<Double>()
        for( gradient in gradients ) {
            biasGradientsList.add( gradient[1] as Double )
        }
        val biasGradients = ( biasGradientsList.toTypedArray() ).average()
        this.weights = MathOps.subtract( this.weights , MathOps.multiplyScalar( weightGradients , learningRate ) )
        this.bias = this.bias - ( biasGradients * learningRate )
    }

    private fun meanSquaredErrorDerivative( predY : Double , targetY : Double ) : Double {
        return 2 * ( predY - targetY )
    }

    private fun batch ( x : Array<DoubleArray> , y : DoubleArray , batchSize : Int ) : List<List<Pair<DoubleArray,Double>>> {
        val data = x.zip( y.toTypedArray() )
        return data.chunked( batchSize )
    }

}






//
//
class MathOps {

    companion object {

        fun dot ( a : DoubleArray , b : DoubleArray ) : Double {
            var dotProduct = 0.0
            for( i in 0 until a.size ) {
                dotProduct += ( a[ i ] * b[ i ] )
            }
            return dotProduct
        }

        fun subtract ( a : DoubleArray , b : DoubleArray ) : DoubleArray {
            val difference = DoubleArray( a.size )
            for( i in 0 until a.size ) {
                difference[i] =  ( a[ i ] - b[ i ] )
            }
            return difference
        }

        fun multiplyScalar ( a : DoubleArray , k : Double ) : DoubleArray {
            val results = DoubleArray( a.size )
            for ( i in 0 until a.size ) {
                results[ i ] = a[ i ] * k
            }
            return results
        }

        fun multidimMean( x : Array<DoubleArray> ) : Array<Double> {
            val mean = ArrayList<Double>()
            for ( i in 0 until x[0].size ) {
                var sum = 0.0
                for ( array in x ) {
                    sum += array[i]
                }
                mean.add( sum / x.size)
            }
            return mean.toTypedArray()
        }

    }



}