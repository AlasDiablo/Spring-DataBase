package fr.alasdiablo.spring.database.api

import fr.alasdiablo.spring.database.api.function.BiSupplier

interface Table {
    fun getValues(): Map<String, BiSupplier<*>>
    fun getValue(name: String): BiSupplier<*>?
    fun setValue(name: String, value: BiSupplier<*>): Int
}