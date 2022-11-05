package com.delitx.it_db_spring_postgress.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "idsCollection")
class IdDocumentEntity {
    @Id
    var id: String? = null
    var newId: Int = 0
}
