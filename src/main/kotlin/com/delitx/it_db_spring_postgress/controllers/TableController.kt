package com.delitx.it_db_spring_postgress.controllers

import com.delitx.it_db_spring_postgress.DateParseException
import com.delitx.it_db_spring_postgress.db.database.Database
import com.delitx.it_db_spring_postgress.db.row.Row
import com.delitx.it_db_spring_postgress.db.table.Table
import com.delitx.it_db_spring_postgress.db.table.mergeTablesByField
import com.delitx.it_db_spring_postgress.db.type.*
import com.delitx.it_db_spring_postgress.network_dto.TypeDto
import com.delitx.it_db_spring_postgress.network_dto.toDto
import com.delitx.it_db_spring_postgress.services.DatabaseService
import com.delitx.it_db_spring_postgress.services.IdGenerationService
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tables")
class TableController {

    @Autowired
    private lateinit var databaseService: DatabaseService

    @Autowired
    private lateinit var idGenerator: IdGenerationService

    @RequestMapping(value = ["/add_row"], method = [RequestMethod.POST])
    fun addTable(@RequestBody addTable: AddRowDto): ResponseEntity<String> {
        val database = databaseService.getById(addTable.databaseId)
            ?: return ResponseEntity.badRequest().body("Database not available")
        val table = database.tables.find { it.id == addTable.tableId }
            ?: return ResponseEntity.badRequest().body("Table not available")
        return try {
            val rowValues = addTable.values
            val row = Row.create(
                idGenerator.newId(),
                table.attributes.zip(rowValues) { attribute, value ->
                    TypeDto(idGenerator.newId(), attribute.type.name, value).toModel()
                }
            )
            val newTable = Table.create(table.id, table.name, table.attributes, table.rows + row)
            val newTablesList = database.tables.filter { it.id != addTable.tableId } + newTable
            val newDatabase = Database.create(database.id, newTablesList)
            databaseService.update(newDatabase)
            ResponseEntity.ok("Saved")
        } catch (e: IllegalStateException) {
            ResponseEntity.badRequest().body("Data invalid")
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body("Data invalid")
        } catch (e: DateParseException) {
            ResponseEntity.badRequest().body("Data invalid")
        }
    }

    @RequestMapping(value = ["/merge"], method = [RequestMethod.POST])
    fun merge(@RequestBody mergeTables: MergeTablesDto): ResponseEntity<String> {
        val database = databaseService.getById(mergeTables.databaseId)
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
                idGenerator::newId
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

    @RequestMapping(value = ["/delete_row"], method = [RequestMethod.DELETE])
    fun deleteRow(@RequestBody deleteRowDto: DeleteRowDto): ResponseEntity<String> {
        val database = databaseService.getById(deleteRowDto.databaseId)
            ?: return ResponseEntity.badRequest().body("Database not found")
        val table = database.tables.find { it.id == deleteRowDto.tableId }
            ?: return ResponseEntity.badRequest().body("Table not found")
        val newTable =
            Table.create(table.id, table.name, table.attributes, table.rows.filter { it.id != deleteRowDto.rowId })
        val newTablesList = database.tables.filter { it.id != deleteRowDto.tableId } + newTable
        val newDatabase = Database.create(database.id, newTablesList)
        databaseService.update(newDatabase)
        return ResponseEntity.ok("Success")
    }

    class MergeTablesDto(
        @field:JsonProperty("databaseId")
        val databaseId: String,
        @field:JsonProperty("firstTableId")
        val firstTableId: String,
        @field:JsonProperty("secondTableId")
        val secondTableId: String,
        @field:JsonProperty("firstTableAttributeId")
        val firstTableAttributeId: String,
        @field:JsonProperty("secondTableAttributeId")
        val secondTableAttributeId: String,
        @field:JsonProperty("resultTableName")
        val resultTableName: String,
        @field:JsonProperty("resultAttributeName")
        val resultAttributeName: String,
    )

    class AddRowDto(
        @field:JsonProperty("databaseId")
        val databaseId: String,
        @field:JsonProperty("tableId")
        val tableId: String,
        @field:JsonProperty("values")
        val values: List<String>,
    )

    class DeleteRowDto(
        @field:JsonProperty("databaseId")
        val databaseId: String,
        @field:JsonProperty("tableId")
        val tableId: String,
        @field:JsonProperty("rowId")
        val rowId: String,
    )
}
