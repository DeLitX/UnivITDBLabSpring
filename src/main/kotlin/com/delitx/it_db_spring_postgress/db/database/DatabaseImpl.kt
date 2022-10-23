package com.delitx.it_db_spring_postgress.db.database

import com.delitx.it_db_spring_postgress.db.table.Table

data class DatabaseImpl(
    override val id: Int,
    override val tables: List<Table>
) : Database
