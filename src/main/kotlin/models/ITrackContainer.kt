package models

interface ITrackContainer  {

    var tracksList : List<Track>


    fun trackIDs() :List<String> {

                val listout = mutableListOf<String>()
                tracksList.forEach { listout.add(it.id) }
                return listout


    }

    fun setTrackAudioFeatures(feats :List<AudioFeatures>){
        feats.forEach { feat ->
            tracksList.find { trac ->
                trac.id == feat.id
            }?.audioFeatures = feat
        }
    }





}