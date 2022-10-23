package com.delitx.it_db_spring_postgress

import java.text.ParseException
import java.text.SimpleDateFormat

data class Date constructor(val value: Long) {
    constructor(value: String) : this(
        kotlin.run {
            try {
                val parsed = SimpleDateFormat(DateFormat).parse(value)
                parsed?.time ?: throw DateParseException()
            } catch (e: ParseException) {
                throw DateParseException()
            }
        }
    )

    override fun toString(): String =
        SimpleDateFormat(DateFormat).format(java.util.Date(value))
}

const val DateFormat = "dd.MM.yyyy"

class DateParseException : RuntimeException()
