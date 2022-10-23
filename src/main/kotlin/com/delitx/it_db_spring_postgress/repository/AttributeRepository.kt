package com.delitx.it_db_spring_postgress.repository

import com.delitx.it_db_spring_postgress.entity.AttributeEntity
import org.springframework.data.repository.CrudRepository

interface AttributeRepository : CrudRepository<AttributeEntity, Int>
