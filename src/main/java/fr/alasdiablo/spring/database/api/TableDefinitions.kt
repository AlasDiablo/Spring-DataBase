package fr.alasdiablo.spring.database.api

interface TableDefinitions {
    fun getDefinitions(): Map<String, Type>
    fun getType(name: String): Type?
    fun setType(name: String, type: Type)
    enum class Type {
        STRING, INTEGER
    }
}