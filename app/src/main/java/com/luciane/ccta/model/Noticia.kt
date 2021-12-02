package com.luciane.ccta.model

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class Noticia(var id: String, var title: String, var newsText: String, var state: String,
              var coverPath: String, var userUid: String, var lastModified: Long) {

    public fun getFormatedLastModifiedDateTime(): String{
        val dateTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(this.lastModified),
            ZoneId.systemDefault()
        )
        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT)
        return dateTime.format(formatter)
    }
}