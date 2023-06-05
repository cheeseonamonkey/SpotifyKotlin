package com.example.example

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Segments (

  @SerialName("start"             ) var start           : Double?           = null,
  @SerialName("duration"          ) var duration        : Double?           = null,
  @SerialName("confidence"        ) var confidence      : Double?           = null,
  @SerialName("loudness_start"    ) var loudnessStart   : Double?           = null,
  @SerialName("loudness_max"      ) var loudnessMax     : Double?           = null,
  @SerialName("loudness_max_time" ) var loudnessMaxTime : Double?           = null,
  @SerialName("loudness_end"      ) var loudnessEnd     : Double?              = null,
  @SerialName("pitches"           ) var pitches         : ArrayList<Double> = arrayListOf(),
  @SerialName("timbre"            ) var timbre          : ArrayList<Double> = arrayListOf()

)