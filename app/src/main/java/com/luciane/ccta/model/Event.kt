package com.luciane.ccta.model

data class Event(
    val date : Long,
    val description : String,
    val lastModified : Long,
    val title : String,
    val userUid : String
)
