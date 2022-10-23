package com.delitx.it_db_spring_postgress.services

import com.delitx.it_db_spring_postgress.db.row.Row
import com.delitx.it_db_spring_postgress.entity.toEntity
import com.delitx.it_db_spring_postgress.repository.RowRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class RowService {
    @Autowired
    private lateinit var repository: RowRepository

    fun getAll(): List<Row> = repository.findAll().map { it.toModel() }

    fun getById(id: Int): Row? = repository.findByIdOrNull(id)?.toModel()

    fun update(row: Row) {
        repository.save(row.toEntity())
    }

    fun deleteById(id: Int) {
        repository.deleteById(id)
    }
}
