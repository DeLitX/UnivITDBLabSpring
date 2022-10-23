package com.delitx.it_db_spring_postgress.entity

import com.delitx.it_db_spring_postgress.Date
import com.delitx.it_db_spring_postgress.db.type.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
@javax.persistence.Table(name = "types")
class TypeEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    val name: String,
    val dbValue: String
) {
    constructor() : this(0, "", "")

    fun toModel(): Type {
        val classes = Type.getSubclasses()
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

fun Type.toEntity(): TypeEntity = TypeEntity(id, name, toString())
