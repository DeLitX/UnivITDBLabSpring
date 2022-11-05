package com.delitx.it_db_spring_postgress.services

import com.delitx.it_db_spring_postgress.db.table.Attribute
import com.delitx.it_db_spring_postgress.entity.toEntity
import com.delitx.it_db_spring_postgress.repository.AttributeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class AttributeService {
    @Autowired
    private lateinit var repository: AttributeRepository

    fun getAll(): List<Attribute> = repository.findAll().map { it.toModel() }

    fun getById(id: String): Attribute? = repository.findByIdOrNull(id)?.toModel()

    fun update(attribute: Attribute) {
        repository.save(attribute.toEntity())
    }

    fun deleteById(id: String) {
        repository.deleteById(id)
    }
}
