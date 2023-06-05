

package models.musicalfeatures

import com.example.example.Segments
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import models.IDataModel
import models.IInitsFromDynamic
import kotlinx.serialization.SerialName

@Serializable
data class MusicalFeatures(

    @SerialName("meta"     )
    var meta     : Meta?               = Meta(),
    @SerialName("track"    )
    var track    : Track?              = Track(),
    @SerialName("bars"     )
    var bars     : ArrayList<Bars>     = arrayListOf(),
    @SerialName("beats"    )
    var beats    : ArrayList<Beats>    = arrayListOf(),
    @SerialName("sections" )
    var sections : ArrayList<Sections> = arrayListOf(),
    @SerialName("segments" )
    var segments : ArrayList<Segments> = arrayListOf(),
    @SerialName("tatums"   )
    var tatums   : ArrayList<Tatums>   = arrayListOf()

) :IDataModel {

   companion object : IInitsFromDynamic {
       override fun fromDynamic(inD: dynamic): MusicalFeatures {
           return MusicalFeatures(
               inD.meta ,
               inD.track ,
               inD.bars ,
               inD.beats ,
               inD.sections ,
               inD.segments ,
               inD.tatums
           )
       }

   }
}