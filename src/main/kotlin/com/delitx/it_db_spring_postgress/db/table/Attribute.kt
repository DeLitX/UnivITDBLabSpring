package com.delitx.it_db_spring_postgress.db.table

import com.delitx.it_db_spring_postgress.Date
import com.delitx.it_db_spring_postgress.db.type.*
import kotlin.reflect.KClass

interface Attribute {
    val id: String
    val name: String
    val type: Type

    fun isThisType(type: Type): Boolean

    companion object {
        fun <T : Type> create(id: String, name: String, type: KClass<T>): Attribute =
            AttributeImpl(
                id,
                name,
                when (type) {
                    TypeInt::class -> TypeInt("", 0)
                    TypeChar::class -> TypeChar("", Char(0))
                    TypeDate::class -> TypeDate("", Date(0L))
                    TypeDateInvl::class -> TypeDateInvl("", Date(0L), Date(0L))
                    TypeString::class -> TypeString("", "")
                    TypeDouble::class -> TypeDouble("", 0.0)
                    else -> error("invalid type class")
                }
            )

        fun create(id: String, name: String, typeName: String): Attribute {
            val classes = Type.getSubclasses()
            for (type in classes) {
                if (typeName == type.name) {
                    return create(
                        id,
                        name,
                        when (type) {
                            is TypeChar -> TypeChar::class
                            is TypeDate -> TypeDate::class
                            is TypeDateInvl -> TypeDateInvl::class
                            is TypeDouble -> TypeDouble::class
                            is TypeInt -> TypeInt::class
                            is TypeString -> TypeString::class
                        }
                    )
                }
            }
            error("")
        }
    }
}
