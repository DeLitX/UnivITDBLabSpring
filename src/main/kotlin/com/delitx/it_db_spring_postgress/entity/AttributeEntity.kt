package com.delitx.it_db_spring_postgress.entity

import com.delitx.it_db_spring_postgress.db.table.Attribute
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("attributes")
class AttributeEntity(
    @Id
    var id: String?,
    var name: String,
    var typeName: String,
) {
    constructor() : this(null, "", "")

    fun toModel(): Attribute = Attribute.create(id!!, name, typeName)
}

fun Attribute.toEntity(): AttributeEntity = AttributeEntity(id.ifEmpty { null }, name, type.name)
