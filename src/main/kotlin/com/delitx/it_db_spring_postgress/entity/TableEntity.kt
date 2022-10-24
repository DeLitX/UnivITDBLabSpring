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
    val attributes: MutableList<AttributeEntity>,

    @OneToMany(
        targetEntity = RowEntity::class,
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY
    )
    val rows: MutableList<RowEntity>,
) {
    constructor() : this(0, "", mutableListOf(), mutableListOf())

    fun toModel(): Table = Table.create(id, name, attributes.map { it.toModel() }, rows.map { it.toModel() })
}

fun Table.toEntity(): TableEntity = TableEntity(
    id,
    name,
    attributes.map { it.toEntity() }.toMutableList(),
    rows.map { it.toEntity() }.toMutableList(),
)
