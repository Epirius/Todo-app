package dev.felixkaasa.todo.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dev.felixkaasa.todo.plugins.Person
import dev.felixkaasa.todo.schema.User
import io.ktor.client.utils.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction


class DatabaseHandler {
    private val host = System.getenv("DB_HOST") ?: "postgres"
    private val port = System.getenv("DB_PORT")?.toIntOrNull() ?: 5432
    private val dbName = System.getenv("DB_NAME") ?: "postgres"
    private val dbUser = System.getenv("DB_USER") ?: "postgres"
    private val dbPassword = "password"//System.getenv("DB_PASSWORD")
    private val dbUrl = "jdbc:postgresql://$host:$port/$dbName"

    private fun dataSource(): HikariDataSource {
        return HikariDataSource(HikariConfig().apply {
            jdbcUrl = dbUrl
            username = dbUser
            password = dbPassword
            driverClassName = "org.postgresql.Driver"
            connectionTimeout = 1000
            maximumPoolSize = 10
        })
    }

    private val conn by lazy {
        println("[SERVER] trying to connect to database")
        Database.connect(dataSource())
    }

    fun connect() {
        try {
            transaction(conn) {
                SchemaUtils.create(User, Person)
            }
        } catch (e: Exception){
            System.err.println("[SERVER] error while trying to create tables / connect to server")
        }
    }
}