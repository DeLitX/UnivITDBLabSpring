package com.delitx.it_db_spring_postgress.repository

import com.delitx.it_db_spring_postgress.entity.DatabaseEntity
import org.springframework.data.repository.CrudRepository

interface DatabaseRepository : CrudRepository<DatabaseEntity, Int>
