package com.delitx.it_db_spring_postgress.network_dto

import com.delitx.it_db_spring_postgress.db.table.Attribute
import com.fasterxml.jackson.annotation.JsonProperty

class AttributeDto(
    @field:JsonProperty("id")
    val id: Int,
    @field:JsonProperty("name")
    val name: String,
    @field:JsonProperty("typeName")
    var typeName: String,
) {
    fun toModel(): Attribute = Attribute.create(id, name, typeName)
}

fun Attribute.toDto(): AttributeDto = AttributeDto(
    id,
    name,
    type.name,
)
