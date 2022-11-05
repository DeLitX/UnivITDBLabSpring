package com.delitx.it_db_spring_postgress.db.type

data class TypeChar(
    override val id: String,
    val value: Char
) : Type {
    override val name: String
        get() = "Char"

    override fun toString(): String = value.toString()
}
