package fr.alasdiablo.spring.database.controller

import fr.alasdiablo.spring.database.api.Database
import fr.alasdiablo.spring.database.api.TableDefinitions
import fr.alasdiablo.spring.database.api.function.BiSupplier
import fr.alasdiablo.spring.database.api.function.IntSupplier
import fr.alasdiablo.spring.database.api.function.StringSupplier
import fr.alasdiablo.spring.database.dao.database.TableDefinitionsDao
import org.json.JSONObject
import org.json.JSONTokener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class DatabaseController {
    @Autowired
    private val database: Database? = null

    @GetMapping(value = ["/"])
    fun getDefaultRoute(): Map<String, String> {
        return buildMap {
            put("[GET] /def/table", "Show all table definitions")
            put("[GET] /def/table/{id}", "Show the table definitions of the specified table")
            put("[POST] /def/table/{id}", "Create a table definitions")
            put("[GET] /table/{id}", "Get all values from a table")
            put("[POST] /table/{id}", "Save values to a table")
        }
    }

    @GetMapping(value = ["/def/table"])
    fun getTables(): Map<String, TableDefinitions> {
        return this.database!!.getTables()
    }

    @PostMapping(value = ["/def/table/{id}"])
    fun createTableDef(@PathVariable id: String?, @RequestBody body: String?): TableDefinitions {
        val definition = TableDefinitionsDao(JSONObject(JSONTokener(body)).toMap())
        this.database!!.createTable(id!!, definition)
        return definition
    }

    @GetMapping(value = ["/def/table/{id}"])
    fun getTableDef(@PathVariable id: String?): TableDefinitions? {
        return database!!.getTables()[id]
    }

    @GetMapping(value = ["/table/{id}"])
    fun getTable(@PathVariable id: String?): Map<String, *>? {
        val table = database!!.getTable(id!!) ?: return null
        return serialize(table.getValues())
    }

    @PostMapping(value = ["/table/{id}"])
    fun getTable(@PathVariable id: String?, @RequestBody body: String?): Map<String, *> {
        val data = JSONObject(JSONTokener(body)).toMap()
        val table = database!!.getTable(id!!)!!
        val def = database.getTables()[id]
        data.forEach { (s: String?, o: Any?) ->
            if (def!!.getType(s!!) === TableDefinitions.Type.INTEGER) {
                val value = o as Int
                table.setValue(s!!, IntSupplier(value))
            }
            if (def!!.getType(s!!) === TableDefinitions.Type.STRING) {
                val value = o as String
                table.setValue(s!!, StringSupplier(value))
            }
        }
        database.updateTable(id, table)
        return serialize(table.getValues())
    }

    private fun serialize(toSerialize: Map<String, BiSupplier<*>>): Map<String, *> {
        val toReturn: MutableMap<String, Any> = HashMap()
        toSerialize.forEach { (s: String, biSupplier: BiSupplier<*>) -> toReturn[s] = biSupplier.get().second!! }
        return toReturn
    }
}