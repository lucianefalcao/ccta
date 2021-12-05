package com.luciane.ccta.model

enum class Turno {
    M, N, T, I
}

data class Course(
    val id: String,
    val name : String,
    val subType : String,
    val type : String,
    val nrPeriods : String,
    val description : String,
    val turno: String,
    val lastModified : Long,
    val userUid : String
)
