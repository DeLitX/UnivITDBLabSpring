package com.delitx.it_db_spring_postgress.repository

import com.delitx.it_db_spring_postgress.entity.DatabaseEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface DatabaseRepository : MongoRepository<DatabaseEntity, String>
