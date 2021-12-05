package com.luciane.ccta.model

class Course(var descripton: String, var lastModified: Long, var name: String, var nrPeriods: String,
             var subType: String, var turno: String, var type: String, var userUid: String) {

    constructor() : this("", 0, "","", "", "", "","")
}