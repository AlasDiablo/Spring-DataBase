package fr.alasdiablo.spring.database.api.function

import fr.alasdiablo.spring.database.api.TableDefinitions
import fr.alasdiablo.spring.database.data.Pair

class IntSupplier(private val value: Int) : BiSupplier<Int> {
    override fun get(): Pair<TableDefinitions.Type, Int> {
        return Pair(TableDefinitions.Type.INTEGER, this.value)
    }

    override fun toString(): String {
        return this.value.toString()
    }
}