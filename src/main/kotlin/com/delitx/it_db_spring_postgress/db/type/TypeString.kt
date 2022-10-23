package com.delitx.it_db_spring_postgress.db.type

data class TypeString(override val id: Int, val value: String) : Type {
    override val name: String
        get() = "String"

    override fun toString(): String = value
}
