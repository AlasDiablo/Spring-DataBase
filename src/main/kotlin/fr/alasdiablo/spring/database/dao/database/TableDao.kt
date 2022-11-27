package fr.alasdiablo.spring.database.dao.database

import fr.alasdiablo.spring.database.api.Table
import fr.alasdiablo.spring.database.api.TableDefinitions
import fr.alasdiablo.spring.database.api.function.BiSupplier
import fr.alasdiablo.spring.database.api.function.IntSupplier
import fr.alasdiablo.spring.database.api.function.StringSupplier
import java.util.*

class TableDao : Table {
    private val definitions: TableDefinitions
    private val values: MutableMap<String, BiSupplier<*>>

    constructor(definitions: TableDefinitions) {
        this.definitions = definitions
        this.values = HashMap()
    }

    constructor(json: Map<*, *>, definitions: TableDefinitions) {
        this.definitions = definitions
        this.values = HashMap()

        json.keys.forEach{ nameObj ->
            val name = nameObj as String
            when (this.definitions.getType(name)!!) {
                TableDefinitions.Type.STRING -> {
                    val value = json[name] as String
                    this.values[name] = StringSupplier(value)
                }

                TableDefinitions.Type.INTEGER -> {
                    val value = json[name] as Int
                    this.values[name] = IntSupplier(value)
                }
            }
        }
    }

    override fun getValues(): Map<String, BiSupplier<*>> {
        return this.values
    }

    override fun getValue(name: String): BiSupplier<*>? {
        return this.values[name]
    }

    override fun setValue(name: String, value: BiSupplier<*>): Int {
        if (this.definitions.getType(name) == null) {
            return 1
        }
        if (value.get().first === this.definitions.getType(name)) {
            this.values[name] = value
            return 0
        }
        return -1
    }
}