package com.delitx.it_db_spring_postgress.network_dto

import com.delitx.it_db_spring_postgress.Date
import com.delitx.it_db_spring_postgress.db.type.*
import com.fasterxml.jackson.annotation.JsonProperty

class TypeDto(
    @field:JsonProperty("id")
    val id: Int,
    @field:JsonProperty("name")
    val name: String,
    @field:JsonProperty("value")
    val value: String,
) {
    fun toModel(): Type {
        val classes = Type.getSubclasses()
        for (type in classes) {
            if (type.name == name) {
                return when (type) {
                    is TypeChar -> TypeChar(id, value.first())
                    is TypeDate -> TypeDate(id, Date(value))
                    is TypeDateInvl -> TypeDateInvl(id, value)
                    is TypeDouble -> TypeDouble(id, value.toDouble())
                    is TypeInt -> TypeInt(id, value.toInt())
                    is TypeString -> TypeString(id, value)
                }
            }
        }
        error("No according type")
    }
}

fun Type.toDto(): TypeDto = TypeDto(
    id,
    name,
    toString(),
)
