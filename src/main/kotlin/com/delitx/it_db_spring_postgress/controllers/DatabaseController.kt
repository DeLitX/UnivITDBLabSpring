package com.delitx.it_db_spring_postgress.controllers

import com.delitx.it_db_spring_postgress.db.database.Database
import com.delitx.it_db_spring_postgress.db.table.Attribute
import com.delitx.it_db_spring_postgress.db.table.Table
import com.delitx.it_db_spring_postgress.network_dto.DatabaseDto
import com.delitx.it_db_spring_postgress.network_dto.toDto
import com.delitx.it_db_spring_postgress.services.DatabaseService
import com.delitx.it_db_spring_postgress.services.IdGenerationService
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/databases")
class DatabaseController {

    @Autowired
    private lateinit var service: DatabaseService

    @Autowired
    private lateinit var idGenerator: IdGenerationService

    @GetMapping
    fun getAll(): ResponseEntity<List<DatabaseDto>> {
        val result = service.getAll()
        return ResponseEntity.ok(result.map { it.toDto() })
    }

    @RequestMapping(value = ["/create"], method = [RequestMethod.POST])
    fun insert(): ResponseEntity<String> {
        return ResponseEntity.ok(service.create())
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): ResponseEntity<DatabaseDto?> {
        return ResponseEntity.ok(service.getById(id)?.toDto())
    }

    @RequestMapping(value = ["/add_table"], method = [RequestMethod.POST])
    fun addTable(@RequestBody addTable: AddTableDto): ResponseEntity<String> {
        val database = service.getById(addTable.databaseId)
            ?: return ResponseEntity.badRequest().body("Database not available")
        return try {
            val table = addTable.tableModel()
            val newDatabase = Database.create(database.id, database.tables + table)
            service.update(newDatabase)
            ResponseEntity.ok("Saved")
        } catch (e: IllegalStateException) {
            ResponseEntity.badRequest().body("Data invalid")
        }
    }

    @RequestMapping(value = ["/delete_table"], method = [RequestMethod.DELETE])
    fun deleteTable(@RequestBody deleteTable: DeleteTableDto): ResponseEntity<String> {
        val database = service.getById(deleteTable.databaseId)
            ?: return ResponseEntity.badRequest().body("Database not available")
        return try {
            val tableId = deleteTable.tableId
            val newDatabase = Database.create(database.id, database.tables.filter { it.id != tableId })
            service.update(newDatabase)
            ResponseEntity.ok("Deleted")
        } catch (e: IllegalStateException) {
            ResponseEntity.badRequest().body("Data invalid")
        }
    }

    @RequestMapping(value = ["/delete"], method = [RequestMethod.DELETE])
    fun delete(@RequestBody id: String): ResponseEntity<String> {
        service.deleteById(id)
        return ResponseEntity.ok("Deleted")
    }

    class DeleteTableDto(
        @field:JsonProperty("databaseId")
        val databaseId: String,
        @field:JsonProperty("tableId")
        val tableId: String,
    )

    class AddTableDto(
        @field:JsonProperty("databaseId")
        val databaseId: String,
        @field:JsonProperty("name")
        val name: String,
        @field:JsonProperty("attributes")
        val attributes: List<AddTableAttribute>,
    ) {

        class AddTableAttribute(
            @field:JsonProperty("name")
            val name: String,
            @field:JsonProperty("type")
            val type: String,
        )
    }

    private fun AddTableDto.AddTableAttribute.toModel(): Attribute =
        Attribute.create(idGenerator.newId(), name, typeName = type)

    private fun AddTableDto.tableModel(): Table =
        Table.create(idGenerator.newId(), name, attributes.map { it.toModel() }, emptyList())
}
