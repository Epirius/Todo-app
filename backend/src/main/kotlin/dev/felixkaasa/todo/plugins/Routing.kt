package dev.felixkaasa.todo.plugins

import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        get("/status"){
            call.respond(HttpStatusCode.OK, "Connection is healthy")
        }
    }
}
