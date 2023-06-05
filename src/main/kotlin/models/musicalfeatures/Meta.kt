

package models.musicalfeatures
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Meta (

  @SerialName("analyzer_version" ) var analyzerVersion : String? = null,
  @SerialName("platform"         ) var platform        : String? = null,
  @SerialName("detailed_status"  ) var detailedStatus  : String? = null,
  @SerialName("status_code"      ) var statusCode      : Int?    = null,
  @SerialName("timestamp"        ) var timestamp       : Int?    = null,
  @SerialName("analysis_time"    ) var analysisTime    : Double? = null,
  @SerialName("input_process"    ) var inputProcess    : String? = null

)