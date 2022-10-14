package dev.felixkaasa.todo

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import dev.felixkaasa.todo.plugins.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        //configureSecurity()
        configureSerialization()
        configureRouting()
    }.start(wait = true)
}
