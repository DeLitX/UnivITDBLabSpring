package com.delitx.it_db_spring_postgress.entity

import com.delitx.it_db_spring_postgress.db.table.Table
import javax.persistence.*

@Entity
@javax.persistence.Table(name = "tables")
class TableEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    val name: String,

    @OneToMany(
        targetEntity = AttributeEntity::class,
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY
    )
    val attributes: List<AttributeEntity>,

    @OneToMany(
        targetEntity = RowEntity::class,
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY
    )
    val rows: List<RowEntity>
) {
    constructor() : this(0, "", emptyList(), emptyList())

    fun toModel(): Table = Table.create(id, name, attributes.map { it.toModel() }, rows.map { it.toModel() })
}

fun Table.toEntity(): TableEntity = TableEntity(id, name, attributes.map { it.toEntity() }, rows.map { it.toEntity() })
