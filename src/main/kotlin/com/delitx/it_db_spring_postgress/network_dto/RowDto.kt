package com.delitx.it_db_spring_postgress.network_dto

import com.delitx.it_db_spring_postgress.db.row.Row
import com.fasterxml.jackson.annotation.JsonProperty

class RowDto(
    @field:JsonProperty("id")
    val id: Int,
    @field:JsonProperty("values")
    val values: List<TypeDto>,
) {
    fun toModel(): Row = Row.create(
        id,
        values.map { it.toModel() },
    )
}

fun Row.toDto(): RowDto = RowDto(
    id,
    values.map { it.toDto() },
)
