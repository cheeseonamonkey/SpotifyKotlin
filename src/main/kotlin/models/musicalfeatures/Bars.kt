

package models.musicalfeatures

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Bars (

  @SerialName("start") var start      : Double? = null,
  @SerialName("duration"   ) var duration   : Double? = null,
  @SerialName("confidence" ) var confidence : Double? = null

)