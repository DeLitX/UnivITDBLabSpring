package com.delitx.it_db_spring_postgress.db.type

data class TypeInt(override val id: String, val value: Int) : Type {
    override val name: String
        get() = "Int"

    override fun toString(): String = value.toString()
}
