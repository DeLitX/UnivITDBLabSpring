package com.delitx.it_db_spring_postgress.db.table

import com.delitx.it_db_spring_postgress.db.row.Row

interface Table {
    val id: Int
    val name: String
    val attributes: List<Attribute>
    val rows: List<Row>

    fun deleteRow(index: Int): Table

    fun addRow(row: Row): Table

    fun updateRow(index: Int, update: (Row) -> Row): Table

    companion object {
        fun create(id: Int, name: String, attributes: List<Attribute>, rows: List<Row>): Table =
            TableImpl(id, name, attributes, rows)
    }
}
