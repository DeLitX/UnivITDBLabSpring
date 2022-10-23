package com.delitx.it_db_spring_postgress.db.type

import com.delitx.it_db_spring_postgress.Date

sealed interface Type {
    val id: Int
    val name: String

    companion object {
        fun getSubclasses(): List<Type> = listOf(
            TypeDate(0, Date(0L)),
            TypeDateInvl(0, Date(0L), Date(0L)),
            TypeInt(0, 0),
            TypeString(0, ""),
            TypeDouble(0, 0.0),
            TypeChar(0, ' ')
        )
    }
}
