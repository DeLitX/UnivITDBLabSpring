package com.delitx.it_db_spring_postgress.entity

import com.delitx.it_db_spring_postgress.db.database.Database
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("databases")
class DatabaseEntity(
    @Id
    var id: String?,
    var tables: MutableList<TableEntity>,
) {
    constructor() : this(null, mutableListOf())

    fun toModel(): Database = Database.create(id!!, tables.map { it.toModel() })
}

fun Database.toEntity(): DatabaseEntity = DatabaseEntity(
    id.ifEmpty { null },
    tables.map { it.toEntity() }.toMutableList()
)
