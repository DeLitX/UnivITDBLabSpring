package com.delitx.it_db_spring_postgress.db.database

import com.delitx.it_db_spring_postgress.db.table.Table

interface Database {
    val id: Int
    val tables: List<Table>

    companion object {
        fun create(id: Int, tables: List<Table>): Database = DatabaseImpl(id, tables)
    }
}
