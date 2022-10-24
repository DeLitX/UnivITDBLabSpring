package com.delitx.it_db_spring_postgress.entity

import com.delitx.it_db_spring_postgress.db.row.Row
import javax.persistence.*

@Entity
@Table(name = "rows")
class RowEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int,

    @OneToMany(
        targetEntity = TypeEntity::class,
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY
    )
    var values: MutableList<TypeEntity>,
) {
    constructor() : this(0, mutableListOf())

    fun toModel(): Row = Row.create(id, values.map { it.toModel() })
}

fun Row.toEntity(): RowEntity = RowEntity(
    id,
    values.map { it.toEntity() }.toMutableList(),
)
