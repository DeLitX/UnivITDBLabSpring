package com.delitx.it_db_spring_postgress.db.type

data class TypeDouble(override val id: Int, val value: Double) : Type {
    override val name: String
        get() = "Double"

    override fun toString(): String = value.toString()
}
