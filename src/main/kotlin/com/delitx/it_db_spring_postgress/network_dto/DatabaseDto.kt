package com.delitx.it_db_spring_postgress.network_dto

import com.delitx.it_db_spring_postgress.db.database.Database
import com.fasterxml.jackson.annotation.JsonProperty

data class DatabaseDto(
    @field:JsonProperty("id")
    val id: String,
    @field:JsonProperty("tables")
    val tables: List<TableDto>,
) {
    fun toModel(): Database = Database.create(
        id,
        tables.map { it.toModel() },
    )
}

fun Database.toDto(): DatabaseDto = DatabaseDto(
    id,
    tables.map { it.toDto() },
)
