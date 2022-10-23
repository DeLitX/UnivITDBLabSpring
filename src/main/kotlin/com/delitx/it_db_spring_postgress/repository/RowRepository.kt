package com.delitx.it_db_spring_postgress.repository

import com.delitx.it_db_spring_postgress.entity.RowEntity
import org.springframework.data.repository.CrudRepository

interface RowRepository : CrudRepository<RowEntity, Int>
