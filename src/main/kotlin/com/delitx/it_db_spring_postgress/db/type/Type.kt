package com.delitx.it_db_spring_postgress.db.type

import com.delitx.it_db_spring_postgress.Date

sealed interface Type {
    val id: String
    val name: String

    companion object {
        fun getSubclasses(): List<Type> = listOf(
            TypeDate("", Date(0L)),
            TypeDateInvl("", Date(0L), Date(0L)),
            TypeInt("", 0),
            TypeString("", ""),
            TypeDouble("", 0.0),
            TypeChar("", ' ')
        )
    }
}
