package com.delitx.it_db_spring_postgress.controllers

import com.delitx.it_db_spring_postgress.DateParseException
import com.delitx.it_db_spring_postgress.db.row.Row
import com.delitx.it_db_spring_postgress.db.table.Table
import com.delitx.it_db_spring_postgress.db.type.*
import com.delitx.it_db_spring_postgress.network_dto.TableDto
import com.delitx.it_db_spring_postgress.network_dto.TypeDto
import com.delitx.it_db_spring_postgress.network_dto.toDto
import com.delitx.it_db_spring_postgress.services.TableService
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tables")
class TableController {

    @Autowired
    private lateinit var service: TableService

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Int): ResponseEntity<TableDto?> {
        return ResponseEntity.ok(service.getById(id)?.toDto())
    }

    @RequestMapping(value = ["/add_row"], method = [RequestMethod.POST])
    fun addTable(@RequestBody addTable: AddRowDto): ResponseEntity<String> {
        val table = service.getById(addTable.tableId)
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

    @RequestMapping(value = ["/delete_row"], method = [RequestMethod.DELETE])
    fun deleteRow(@RequestBody id: Int): ResponseEntity<String> {
        service.deleteById(id)
        return ResponseEntity.ok("Success")
    }

    class AddRowDto(
        @field:JsonProperty("tableId")
        val tableId: Int,
        @field:JsonProperty("values")
        val values: List<String>,
    )
}
