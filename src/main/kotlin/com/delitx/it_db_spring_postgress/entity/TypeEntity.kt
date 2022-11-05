package com.delitx.it_db_spring_postgress.entity

import com.delitx.it_db_spring_postgress.Date
import com.delitx.it_db_spring_postgress.db.type.*
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("types")
data class TypeEntity(
    @Id
    val id: String?,
    val name: String,
    val dbValue: String,
) {
    constructor() : this(null, "", "")

    fun toModel(): Type {
        val classes = Type.getSubclasses()
        id!!
        for (type in classes) {
            if (type.name == name) {
                return when (type) {
                    is TypeChar -> TypeChar(id, dbValue.first())
                    is TypeDate -> TypeDate(id, Date(dbValue))
                    is TypeDateInvl -> TypeDateInvl(id, dbValue)
                    is TypeDouble -> TypeDouble(id, dbValue.toDouble())
                    is TypeInt -> TypeInt(id, dbValue.toInt())
                    is TypeString -> TypeString(id, dbValue)
                }
            }
        }
        error("No according type")
    }
}

fun Type.toEntity(): TypeEntity = TypeEntity(id.ifEmpty { null }, name, toString())
