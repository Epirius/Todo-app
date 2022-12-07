package dev.felixkaasa.todo.plugins

import dev.felixkaasa.todo.schema.*
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction


fun Application.configureRouting() {

    routing {
        login()
        authenticate("auth-jwt") {
            todo()
            tab()
            get("/authstatus") {
                call.respond(HttpStatusCode.OK, "Connection is healthy, with auth")
            }
        }
        get("/status") {
            call.respond(HttpStatusCode.OK, "Connection is healthy")
        }
    }
}

fun getUser(email: String): ResultRow? {
    val id = transaction {
        addLogger(StdOutSqlLogger)
        User.select {
            User.email eq email
        }.firstOrNull()
    }
    return id
}

fun getTab(
    id: ResultRow,
    tabName: String
): ResultRow? {
    val tab = transaction {
        addLogger(StdOutSqlLogger)
        Tab.select {
            Tab.userId eq id[User.userId]
            Tab.tabName eq tabName
        }.firstOrNull()
    }
    return tab
}

//TODO make sure tabs dont have the same name