package com.delitx.it_db_spring_postgress.db.row

import com.delitx.it_db_spring_postgress.db.type.Type

data class RowImpl(
    override val id: Int,
    override val values: List<Type>
) : Row {
    override fun copy(update: (List<Type>) -> List<Type>): Row =
        RowImpl(id, update(values))
}
