package com.delitx.it_db_spring_postgress.controllers

import com.delitx.it_db_spring_postgress.DateParseException
import com.delitx.it_db_spring_postgress.db.database.Database
import com.delitx.it_db_spring_postgress.db.row.Row
import com.delitx.it_db_spring_postgress.db.table.Table
import com.delitx.it_db_spring_postgress.db.table.mergeTablesByField
import com.delitx.it_db_spring_postgress.db.type.*
import com.delitx.it_db_spring_postgress.network_dto.TableDto
import com.delitx.it_db_spring_postgress.network_dto.TypeDto
import com.delitx.it_db_spring_postgress.network_dto.toDto
import com.delitx.it_db_spring_postgress.services.DatabaseService
import com.delitx.it_db_spring_postgress.services.TableService
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/database")
class TableController {

    @Autowired
    private lateinit var service: TableService

    @Autowired
    private lateinit var databaseService: DatabaseService

    @GetMapping("/{databaseId}/table/{id}")
    fun getById(@PathVariable databaseId: Int, @PathVariable id: Int): ResponseEntity<TableDto?> {
        return ResponseEntity.ok(service.getById(id)?.toDto())
    }

    @RequestMapping(value = ["/{databaseId}/table/{id}/row"], method = [RequestMethod.POST])
    fun addTable(@PathVariable id: Int, @RequestBody addTable: AddRowDto): ResponseEntity<String> {
        val table = service.getById(id)
            ?: return ResponseEntity.badRequest().body("Table not available")
        return try {
            val rowValues = addTable.values
            val row = Row.create(
                0,
                table.attributes.zip(rowValues) { attribute, value ->
                    TypeDto(0, attribute.type.name, value).toModel()
                }
            )
            val newTable = Table.create(table.id, table.name, table.attributes, table.rows + row)
            service.update(newTable)
            ResponseEntity.ok("Saved")
        } catch (e: IllegalStateException) {
            ResponseEntity.badRequest().body("Data invalid")
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body("Data invalid")
        } catch (e: DateParseException) {
            ResponseEntity.badRequest().body("Data invalid")
        }
    }

    @RequestMapping(value = ["/{databaseId}/merge"], method = [RequestMethod.POST])
    fun merge(@PathVariable databaseId: Int, @RequestBody mergeTables: MergeTablesDto): ResponseEntity<String> {
        val database = databaseService.getById(databaseId)
            ?: return ResponseEntity.badRequest().body("Database not available")
        val table1 = database.tables.find { it.id == mergeTables.firstTableId }
            ?: return ResponseEntity.badRequest().body("First table not available")
        val table2 = database.tables.find { it.id == mergeTables.secondTableId }
            ?: return ResponseEntity.badRequest().body("Second table not available")
        val attribute1Index = table1.attributes.indexOfFirst { it.id == mergeTables.firstTableAttributeId }
        if (attribute1Index < 0) {
            return ResponseEntity.badRequest().body("FirstAttributeNotAvailable")
        }
        val attribute2Index = table2.attributes.indexOfFirst { it.id == mergeTables.secondTableAttributeId }
        if (attribute2Index < 0) {
            return ResponseEntity.badRequest().body("SecondAttributeNotAvailable")
        }
        return try {
            val newTable = mergeTablesByField(
                table1,
                table2,
                attribute1Index,
                attribute2Index,
                mergeTables.resultAttributeName,
                mergeTables.resultTableName,
            )
            val newDatabase = Database.create(database.id, database.tables + newTable)
            databaseService.update(newDatabase)
            ResponseEntity.ok(newTable.toDto().toString())
        } catch (e: IllegalStateException) {
            ResponseEntity.badRequest().body("Data invalid")
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body("Data invalid")
        } catch (e: DateParseException) {
            ResponseEntity.badRequest().body("Data invalid")
        }
    }

    @RequestMapping(value = ["/{databaseId}/table/{tableId}/row/{rowId}"], method = [RequestMethod.DELETE])
    fun deleteRow(
        @PathVariable databaseId: Int,
        @PathVariable tableId: Int,
        @PathVariable rowId: Int,
    ): ResponseEntity<String> {
        val database = databaseService.getById(databaseId)
            ?: return ResponseEntity.badRequest().body("Database not found")
        val table = database.tables.find { it.id == tableId }
            ?: return ResponseEntity.badRequest().body("Table not found")
        val newTable =
            Table.create(table.id, table.name, table.attributes, table.rows.filter { it.id != rowId })
        service.update(newTable)
        return ResponseEntity.ok("Success")
    }

    class MergeTablesDto(
        @field:JsonProperty("firstTableId")
        val firstTableId: Int,
        @field:JsonProperty("secondTableId")
        val secondTableId: Int,
        @field:JsonProperty("firstTableAttributeId")
        val firstTableAttributeId: Int,
        @field:JsonProperty("secondTableAttributeId")
        val secondTableAttributeId: Int,
        @field:JsonProperty("resultTableName")
        val resultTableName: String,
        @field:JsonProperty("resultAttributeName")
        val resultAttributeName: String,
    )

    class AddRowDto(
        @field:JsonProperty("values")
        val values: List<String>,
    )
}
