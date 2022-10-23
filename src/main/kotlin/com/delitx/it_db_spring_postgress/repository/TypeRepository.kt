package com.delitx.it_db_spring_postgress.repository

import com.delitx.it_db_spring_postgress.entity.TypeEntity
import org.springframework.data.repository.CrudRepository

interface TypeRepository : CrudRepository<TypeEntity, Int>
