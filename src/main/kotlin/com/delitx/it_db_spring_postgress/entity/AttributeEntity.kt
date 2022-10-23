package com.delitx.it_db_spring_postgress.entity

import com.delitx.it_db_spring_postgress.db.table.Attribute
import javax.persistence.*

@Entity
@Table(name = "attributes")
class AttributeEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int,
    var name: String,
    var typeName: String
) {
    constructor() : this(0, "", "")

    fun toModel(): Attribute = Attribute.create(id, name, typeName)
}

fun Attribute.toEntity(): AttributeEntity = AttributeEntity(id, name, type.name)
