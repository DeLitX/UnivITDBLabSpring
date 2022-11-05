package com.delitx.it_db_spring_postgress.db.database

import com.delitx.it_db_spring_postgress.db.table.Table

interface Database {
    val id: String
    val tables: List<Table>

    companion object {
        fun create(id: String, tables: List<Table>): Database = DatabaseImpl(id, tables)
    }
}
