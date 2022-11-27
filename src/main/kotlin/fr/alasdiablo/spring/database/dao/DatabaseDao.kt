package fr.alasdiablo.spring.database.dao

import fr.alasdiablo.spring.database.api.Database
import fr.alasdiablo.spring.database.api.Table
import fr.alasdiablo.spring.database.api.TableDefinitions
import fr.alasdiablo.spring.database.dao.database.TableDao
import fr.alasdiablo.spring.database.dao.database.TableDefinitionsDao
import org.apache.commons.io.FileUtils
import org.json.JSONObject
import org.json.JSONTokener
import org.springframework.stereotype.Repository
import java.io.File
import java.io.FileInputStream
import java.nio.charset.StandardCharsets

@Repository
class DatabaseDao : Database {
    private val tableDefinitions: MutableMap<String, TableDefinitions>
    private val table: MutableMap<String, Table>

    init {
        this.tableDefinitions = HashMap()
        this.table = HashMap()

        val database = JSONObject(JSONTokener(FileInputStream("database.json")))

        val tablesDefinitions = database.getJSONObject("tables_definitions")

        tablesDefinitions.toMap().forEach { (name: String, o: Any?) ->
            val json = o as Map<*, *>
            this.tableDefinitions[name] = TableDefinitionsDao(json)
        }

        val tables = database.getJSONObject("tables")

        tables.toMap().forEach { (name: String, o: Any?) ->
            val json = o as Map<*, *>
            this.table[name] = TableDao(json, this.tableDefinitions[name]!!)
        }
    }

    fun save() {
        val json = JSONObject()

        val tablesDefinitions = JSONObject()

        this.tableDefinitions.forEach { (name, definitionsObject) ->
            val definitions = JSONObject()
            definitionsObject.getDefinitions().forEach { (defName, defType) ->
                definitions.put(defName, defType.name)
            }
            tablesDefinitions.put(name, definitions)
        }

        val tables = JSONObject()

        this.table.forEach { (name, table) ->
            val values = JSONObject()
            table.getValues().forEach { (valueKey, valueSupllier) ->
                values.put(valueKey, valueSupllier.get().second)
            }
            tables.put(name, values)
        }

        json.put("tables_definitions", tablesDefinitions)
        json.put("tables", tables)

        FileUtils.writeStringToFile(File("database.json"), json.toString(2), StandardCharsets.UTF_8)
    }

    override fun getTables(): Map<String, TableDefinitions> {
        return this.tableDefinitions
    }

    override fun createTable(name: String, definitions: TableDefinitions) {
        this.tableDefinitions[name] = definitions
        this.table.put(name, TableDao(definitions))
        this.save()
    }

    override fun getTable(name: String): Table? {
        return this.table[name]
    }

    override fun updateTable(name: String, table: Table) {
        this.table.replace(name, table)
        this.save()
    }
}