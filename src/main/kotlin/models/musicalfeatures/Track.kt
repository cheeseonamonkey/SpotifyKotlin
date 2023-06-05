

package models.musicalfeatures

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Track (

  @SerialName("num_samples"               ) var numSamples              : Int?    = null,
  @SerialName("duration"                  ) var duration                : Double? = null,
  @SerialName("sample_md5"                ) var sampleMd5               : String? = null,
  @SerialName("offset_seconds"            ) var offsetSeconds           : Int?    = null,
  @SerialName("window_seconds"            ) var windowSeconds           : Int?    = null,
  @SerialName("analysis_sample_rate"      ) var analysisSampleRate      : Int?    = null,
  @SerialName("analysis_channels"         ) var analysisChannels        : Int?    = null,
  @SerialName("end_of_fade_in"            ) var endOfFadeIn             : Double?    = null,
  @SerialName("start_of_fade_out"         ) var startOfFadeOut          : Double? = null,
  @SerialName("loudness"                  ) var loudness                : Double? = null,
  @SerialName("tempo"                     ) var tempo                   : Double? = null,
  @SerialName("tempo_confidence"          ) var tempoConfidence         : Double? = null,
  @SerialName("time_signature"            ) var timeSignature           : Int?    = null,
  @SerialName("time_signature_confidence" ) var timeSignatureConfidence : Double? = null,
  @SerialName("key"                       ) var key                     : Int?    = null,
  @SerialName("key_confidence"            ) var keyConfidence           : Double? = null,
  @SerialName("mode"                      ) var mode                    : Int?    = null,
  @SerialName("mode_confidence"           ) var modeConfidence          : Double? = null,
  @SerialName("codestring"                ) var codestring              : String? = null,
  @SerialName("code_version"              ) var codeVersion             : Double? = null,
  @SerialName("echoprintstring"           ) var echoprintstring         : String? = null,
  @SerialName("echoprint_version"         ) var echoprintVersion        : Double? = null,
  @SerialName("synchstring"               ) var synchstring             : String? = null,
  @SerialName("synch_version"             ) var synchVersion            : Double? = null,
  @SerialName("rhythmstring"              ) var rhythmstring            : String? = null,
  @SerialName("rhythm_version"            ) var rhythmVersion           : Double? = null

)