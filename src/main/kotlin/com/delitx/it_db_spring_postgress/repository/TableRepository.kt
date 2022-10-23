package com.delitx.it_db_spring_postgress.repository

import com.delitx.it_db_spring_postgress.entity.TableEntity
import org.springframework.data.repository.CrudRepository

interface TableRepository : CrudRepository<TableEntity, Int>
