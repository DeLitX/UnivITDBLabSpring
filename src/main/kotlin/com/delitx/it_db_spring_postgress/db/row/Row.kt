package com.delitx.it_db_spring_postgress.db.row

import com.delitx.it_db_spring_postgress.db.type.Type

interface Row {
    val id: String
    val values: List<Type>

    fun copy(update: (List<Type>) -> List<Type> = { it }): Row

    companion object {
        fun create(id: String, values: List<Type>): Row = RowImpl(id, values)
    }
}
