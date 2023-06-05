package misc

enum class TimeLength(val realStr:String){
       MONTH("short_term"),
        YEAR("medium_term"),
    ALL_TIME("long_term");

    override fun toString(): String = realStr

}