package com.delitx.it_db_spring_postgress.network_dto

import com.delitx.it_db_spring_postgress.db.table.Table
import com.fasterxml.jackson.annotation.JsonProperty

data class TableDto(
    @field:JsonProperty("id")
    val id: Int,
    @field:JsonProperty("name")
    val name: String,
    @field:JsonProperty("attributes")
    val attributes: List<AttributeDto>,
    @field:JsonProperty("rows")
    val rows: List<RowDto>,
) {
    fun toModel(): Table = Table.create(
        id,
        name,
        attributes.map { it.toModel() },
        rows.map { it.toModel() },
    )
}

fun Table.toDto(): TableDto = TableDto(
    id,
    name,
    attributes.map { it.toDto() },
    rows.map { it.toDto() },
)
