package fr.alasdiablo.spring.database.dao.database

import fr.alasdiablo.spring.database.api.TableDefinitions

class TableDefinitionsDao(json: Map<*, *>) : TableDefinitions {
    private val definitions: MutableMap<String, TableDefinitions.Type>

    init {
        this.definitions = HashMap()

        json.forEach { entry ->
            val name = entry.key as String
            val value = entry.value as String

            this.definitions[name] = TableDefinitions.Type.valueOf(value)
        }
    }

    override fun getDefinitions(): Map<String, TableDefinitions.Type> {
        return this.definitions
    }

    override fun getType(name: String): TableDefinitions.Type? {
        return this.definitions[name]
    }

    override fun setType(name: String, type: TableDefinitions.Type) {
        this.definitions[name] = type
    }
}