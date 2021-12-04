package com.luciane.ccta.model

class News( val id : String, var coverPath : String, val lastModified : Long, val newsText : String,
            val state : String, val title: String, val userUid : String){
    constructor() : this("", "", 0, "","","","",)
}