package com.delitx.it_db_spring_postgress.entity

import com.delitx.it_db_spring_postgress.db.database.Database
import javax.persistence.*

@Entity
@Table(name = "databases")
class DatabaseEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,

    @OneToMany(
        targetEntity = TableEntity::class,
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY
    )
    val tables: List<TableEntity>
) {
    constructor() : this(0, emptyList())

    fun toModel(): Database = Database.create(id, tables.map { it.toModel() })
}
