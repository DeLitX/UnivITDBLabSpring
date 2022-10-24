package com.delitx.it_db_spring_postgress.network_dto

import com.delitx.it_db_spring_postgress.db.database.Database
import com.fasterxml.jackson.annotation.JsonProperty

class DatabaseDto(
    @field:JsonProperty("id")
    val id: Int,
    @field:JsonProperty("tables")
    val tables: List<TableDto>,
) {
    fun toModel(): Database = Database.create(
        id,
        tables.map { it.toModel() },
    )
}
