package fr.alasdiablo.spring.database.api

interface Database {
    fun getTables(): Map<String, TableDefinitions>
    fun createTable(name: String, definitions: TableDefinitions)
    fun getTable(name: String): Table?
    fun updateTable(name: String, table: Table)
}