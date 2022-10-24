package com.delitx.it_db_spring_postgress.controllers

import com.delitx.it_db_spring_postgress.db.database.Database
import com.delitx.it_db_spring_postgress.network_dto.DatabaseDto
import com.delitx.it_db_spring_postgress.network_dto.TableDto
import com.delitx.it_db_spring_postgress.network_dto.toDto
import com.delitx.it_db_spring_postgress.services.DatabaseService
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/databases")
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

    @RequestMapping(value = ["/add_table"], method = [RequestMethod.POST])
    fun addTable(@RequestBody addTable: AddTableDto): ResponseEntity<String> {
        val database = service.getById(addTable.databaseId)
            ?: return ResponseEntity.badRequest().body("Database not available")
        return try {
            val table = addTable.table.toModel()
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
    fun delete(@RequestBody id: Int): ResponseEntity<String> {
        service.deleteById(id)
        return ResponseEntity.ok("Deleted")
    }

    class DeleteTableDto(
        @field:JsonProperty("databaseId")
        val databaseId: Int,
        @field:JsonProperty("tableId")
        val tableId: Int,
    )

    class AddTableDto(
        @field:JsonProperty("databaseId")
        val databaseId: Int,
        @field:JsonProperty("table")
        val table: TableDto,
    )
}
