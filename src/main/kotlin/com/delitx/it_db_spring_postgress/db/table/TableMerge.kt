package com.delitx.it_db_spring_postgress.db.table

import com.delitx.it_db_spring_postgress.db.row.Row
import com.delitx.it_db_spring_postgress.db.type.*
import com.delitx.it_db_spring_postgress.entity.toEntity

fun mergeTablesByField(
    first: Table,
    second: Table,
    firstIndex: Int,
    secondIndex: Int,
    newFieldName: String,
    newName: String,
): Table {
    val firstAttribute = first.attributes[firstIndex]
    val secondAttribute = second.attributes[secondIndex]
    require(firstAttribute.type == secondAttribute.type)

    val newNames = first.attributes.subList(0, firstIndex) +
        first.attributes.subList(firstIndex + 1, first.attributes.size) +
        second.attributes.subList(0, secondIndex) +
        second.attributes.subList(secondIndex + 1, second.attributes.size) +
        Attribute.create(0, newFieldName, firstAttribute.type::class)
    val newRows = mutableListOf<Row>()
    for (row in first.rows) {
        val fieldValue = row.values[firstIndex]
        val similarFieldRows = second.rows.filter { it.values[secondIndex].compare(fieldValue) }
        for (similarRow in similarFieldRows) {
            val resultRowValues = row.values.subList(0, firstIndex) +
                row.values.subList(firstIndex + 1, row.values.size) +
                similarRow.values.subList(0, firstIndex) +
                similarRow.values.subList(firstIndex + 1, similarRow.values.size) +
                fieldValue
            newRows.add(Row.create(0, resultRowValues.map { it.toEntity().copy(id = 0).toModel() }))
        }
    }
    return Table.create(
        0,
        newName,
        newNames.map { Attribute.create(0, it.name, it.type::class) },
        newRows,
    )
}

private fun Type.compare(another: Type): Boolean = toString() == another.toString()
