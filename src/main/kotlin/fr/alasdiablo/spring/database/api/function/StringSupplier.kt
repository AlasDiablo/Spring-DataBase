package fr.alasdiablo.spring.database.api.function

import fr.alasdiablo.spring.database.api.TableDefinitions
import fr.alasdiablo.spring.database.data.Pair

class StringSupplier(private val value: String) : BiSupplier<String> {
    override fun get(): Pair<TableDefinitions.Type, String> {
        return Pair(TableDefinitions.Type.STRING, this.value)
    }

    override fun toString(): String {
        return this.value
    }
}