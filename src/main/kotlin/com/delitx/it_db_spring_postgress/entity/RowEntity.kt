package com.delitx.it_db_spring_postgress.entity

import com.delitx.it_db_spring_postgress.db.row.Row
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
@Document("rows")
class RowEntity(
    @Id
    var id: String?,
    var values: MutableList<TypeEntity>,
) {
    constructor() : this(null, mutableListOf())

    fun toModel(): Row = Row.create(id!!, values.map { it.toModel() })
}

fun Row.toEntity(): RowEntity = RowEntity(
    id.ifEmpty { null },
    values.map { it.toEntity() }.toMutableList(),
)
