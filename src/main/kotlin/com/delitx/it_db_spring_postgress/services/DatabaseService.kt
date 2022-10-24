package com.delitx.it_db_spring_postgress.services

import com.delitx.it_db_spring_postgress.db.database.Database
import com.delitx.it_db_spring_postgress.entity.toEntity
import com.delitx.it_db_spring_postgress.repository.DatabaseRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class DatabaseService {
    @Autowired
    private lateinit var repository: DatabaseRepository

    fun getAll(): List<Database> = repository.findAll().map { it.toModel() }

    fun getById(id: Int): Database? = repository.findByIdOrNull(id)?.toModel()

    fun create(): Int {
        val dbEntity = Database.create(0, emptyList()).toEntity()
        val saved = repository.save(dbEntity)
        return saved.id
    }

    fun update(database: Database) {
        repository.save(database.toEntity())
    }

    fun deleteById(id: Int) {
        repository.deleteById(id)
    }
}
