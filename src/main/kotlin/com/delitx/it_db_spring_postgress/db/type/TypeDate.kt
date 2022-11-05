package com.delitx.it_db_spring_postgress.db.type

import com.delitx.it_db_spring_postgress.Date

data class TypeDate(
    override val id: String,
    val value: Date
) : Type {
    override val name: String
        get() = "Date"

    override fun toString(): String = value.toString()
}
