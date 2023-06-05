

package models.musicalfeatures
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Sections (

  @SerialName("start"                     ) var start                   : Double?    = null,
  @SerialName("duration"                  ) var duration                : Double? = null,
  @SerialName("confidence"                ) var confidence              : Double?    = null,
  @SerialName("loudness"                  ) var loudness                : Double? = null,
  @SerialName("tempo"                     ) var tempo                   : Double? = null,
  @SerialName("tempo_confidence"          ) var tempoConfidence         : Double? = null,
  @SerialName("key"                       ) var key                     : Int?    = null,
  @SerialName("key_confidence"            ) var keyConfidence           : Double? = null,
  @SerialName("mode"                      ) var mode                    : Int?    = null,
  @SerialName("mode_confidence"           ) var modeConfidence          : Double? = null,
  @SerialName("time_signature"            ) var timeSignature           : Int?    = null,
  @SerialName("time_signature_confidence" ) var timeSignatureConfidence : Double?    = null

)