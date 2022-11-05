package com.delitx.it_db_spring_postgress.services

import com.delitx.it_db_spring_postgress.db.type.Type
import com.delitx.it_db_spring_postgress.entity.toEntity
import com.delitx.it_db_spring_postgress.repository.TypeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class TypeService {
    @Autowired
    private lateinit var repository: TypeRepository

    fun getAll(): List<Type> = repository.findAll().map { it.toModel() }

    fun getById(id: String): Type? = repository.findByIdOrNull(id)?.toModel()

    fun update(type: Type) {
        repository.save(type.toEntity())
    }

    fun deleteById(id: String) {
        repository.deleteById(id)
    }
}
