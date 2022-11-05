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
    @Autowired
    private lateinit var idGenerator: IdGenerationService

    fun getAll(): List<Database> = repository.findAll().map { it.toModel() }

    fun getById(id: String): Database? = repository.findByIdOrNull(id)?.toModel()

    fun create(): String {
        val dbEntity = Database.create(idGenerator.newId(), emptyList()).toEntity()
        val saved = repository.save(dbEntity)
        return saved.id!!
    }

    fun update(database: Database) {
        repository.save(database.toEntity())
    }

    fun deleteById(id: String) {
        repository.deleteById(id)
    }
}
