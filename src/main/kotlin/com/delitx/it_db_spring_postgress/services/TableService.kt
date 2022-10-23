package com.delitx.it_db_spring_postgress.services

import com.delitx.it_db_spring_postgress.db.table.Table
import com.delitx.it_db_spring_postgress.entity.toEntity
import com.delitx.it_db_spring_postgress.repository.TableRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class TableService {
    @Autowired
    private lateinit var repository: TableRepository

    fun getAll(): List<Table> = repository.findAll().map { it.toModel() }

    fun getById(id: Int): Table? = repository.findByIdOrNull(id)?.toModel()

    fun update(table: Table) {
        repository.save(table.toEntity())
    }

    fun deleteById(id: Int) {
        repository.deleteById(id)
    }
}
