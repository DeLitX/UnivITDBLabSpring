package com.delitx.it_db_spring_postgress.services

import com.delitx.it_db_spring_postgress.entity.IdDocumentEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.FindAndModifyOptions.options
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service

@Service
class IdGenerationService {
    @Autowired
    lateinit var mongo: MongoOperations

    fun newId(): String {
        val counter: IdDocumentEntity? = mongo.findAndModify(
            query(where("_id").`is`("idsCollection")),
            Update().inc("newId", 1),
            options().returnNew(true).upsert(true),
            IdDocumentEntity::class.java
        )
        return counter?.newId?.toString() ?: ""
    }
}
