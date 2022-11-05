package com.delitx.it_db_spring_postgress.entity

import com.delitx.it_db_spring_postgress.db.table.Table
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("tables")
class TableEntity(
    @Id
    val id: String?,
    val name: String,
    val attributes: MutableList<AttributeEntity>,
    val rows: MutableList<RowEntity>,
) {
    constructor() : this(null, "", mutableListOf(), mutableListOf())

    fun toModel(): Table = Table.create(id!!, name, attributes.map { it.toModel() }, rows.map { it.toModel() })
}

fun Table.toEntity(): TableEntity = TableEntity(
    id.ifEmpty { null },
    name,
    attributes.map { it.toEntity() }.toMutableList(),
    rows.map { it.toEntity() }.toMutableList(),
)
