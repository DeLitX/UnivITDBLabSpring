package com.delitx.it_db_spring_postgress.db.type

import com.delitx.it_db_spring_postgress.Date
import com.delitx.it_db_spring_postgress.DateParseException

data class TypeDateInvl(
    override val id: Int,
    val start: Date,
    val end: Date
) : Type {

    constructor(id: Int, value: String) : this(id, Date(value.split(" - ")[0]), Date(value.split(" - ")[1]))

    override val name: String
        get() = "DateInvl"

    init {
        if (start.value > end.value) {
            throw DateParseException()
        }
    }

    override fun toString(): String = "$start - $end"
}
