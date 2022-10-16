package dev.felixkaasa.todo

import dev.felixkaasa.todo.db.DatabaseHandler
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import dev.felixkaasa.todo.plugins.*
import org.jetbrains.exposed.sql.Database

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        DatabaseHandler().connect()
        //configureSecurity()
        configureSerialization()
        configureRouting()

    }.start(wait = true)
}
