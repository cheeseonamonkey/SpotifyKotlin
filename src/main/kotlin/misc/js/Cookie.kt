package misc.js

@JsModule("js-cookie")
@JsNonModule
external object Cookie {

    fun set(key:String, value:String, exp:Int)
    fun set(key:String, value:String)
    fun get(key:String) :String?
    fun get() :String
    fun remove(key:String)

}