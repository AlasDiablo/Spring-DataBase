package fr.alasdiablo.spring.database

import fr.alasdiablo.spring.database.dao.DatabaseDao
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringDataBaseApplication

fun main(args: Array<String>) {
    val context = runApplication<SpringDataBaseApplication>(*args)
    context.getBean(DatabaseDao::class.java)
}