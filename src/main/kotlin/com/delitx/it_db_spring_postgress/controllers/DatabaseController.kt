package com.delitx.it_db_spring_postgress.controllers

import com.delitx.it_db_spring_postgress.db.database.Database
import com.delitx.it_db_spring_postgress.db.table.Attribute
import com.delitx.it_db_spring_postgress.db.table.Table
import com.delitx.it_db_spring_postgress.network_dto.DatabaseDto
import com.delitx.it_db_spring_postgress.network_dto.toDto
import com.delitx.it_db_spring_postgress.services.DatabaseService
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/database")
class DatabaseController {

    @Autowired
    private lateinit var service: DatabaseService

    @GetMapping
    fun getAll(): ResponseEntity<List<DatabaseDto>> {
        val result = service.getAll()
        return ResponseEntity.ok(result.map { it.toDto() })
    }

    @RequestMapping(value = ["/create"], method = [RequestMethod.POST])
    fun insert(): ResponseEntity<Int> {
        return ResponseEntity.ok(service.create())
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Int): ResponseEntity<DatabaseDto?> {
        return ResponseEntity.ok(service.getById(id)?.toDto())
    }

    @RequestMapping(value = ["/{id}/table"], method = [RequestMethod.POST])
    fun addTable(@PathVariable id: Int, @RequestBody addTable: AddTableDto): ResponseEntity<String> {
        val database = service.getById(id)
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

    @RequestMapping(value = ["/{id}/table/{tableId}"], method = [RequestMethod.DELETE])
    fun deleteTable(@PathVariable id: Int, @PathVariable tableId: Int): ResponseEntity<String> {
        val database = service.getById(id)
            ?: return ResponseEntity.badRequest().body("Database not available")
        return try {
            val tableId = tableId
            val newDatabase = Database.create(database.id, database.tables.filter { it.id != tableId })
            service.update(newDatabase)
            ResponseEntity.ok("Deleted")
        } catch (e: IllegalStateException) {
            ResponseEntity.badRequest().body("Data invalid")
        }
    }

    @RequestMapping(value = ["/{id}"], method = [RequestMethod.DELETE])
    fun delete(@PathVariable id: Int): ResponseEntity<String> {
        service.deleteById(id)
        return ResponseEntity.ok("Deleted")
    }

    class AddTableDto(
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
        ) {
            fun toModel(): Attribute = Attribute.create(0, name, typeName = type)
        }

        fun tableModel(): Table = Table.create(0, name, attributes.map { it.toModel() }, emptyList())
    }
}
