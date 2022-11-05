package com.delitx.it_db_spring_postgress.db.table

import com.delitx.it_db_spring_postgress.db.type.Type

data class AttributeImpl(
    override val id: String,
    override val name: String,
    override val type: Type,
) : Attribute {
    override fun isThisType(type: Type): Boolean = this.type::class.isInstance(type)
}
