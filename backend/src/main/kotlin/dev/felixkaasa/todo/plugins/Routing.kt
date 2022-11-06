package dev.felixkaasa.todo.plugins

import dev.felixkaasa.todo.schema.*
import dev.felixkaasa.todo.schema.Tab.tabId
import dev.felixkaasa.todo.schema.Tab.tabName
import dev.felixkaasa.todo.schema.Task.date
import dev.felixkaasa.todo.schema.Task.description
import dev.felixkaasa.todo.schema.Task.done
import dev.felixkaasa.todo.schema.Task.taskName
import dev.felixkaasa.todo.schema.User.userId
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.request.*
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


fun getUser(todo: TaskJson): ResultRow? {
    val id = transaction {
        addLogger(StdOutSqlLogger)
        User.select {
            User.email eq todo.email
        }.firstOrNull()
    }
    return id
}

fun getUser(tab: TabJson): ResultRow? {
    val id = transaction {
        addLogger(StdOutSqlLogger)
        User.select {
            User.email eq tab.email
        }.firstOrNull()
    }
    return id
}

fun getUser(user: UserJson): ResultRow? {
    val id = transaction {
        addLogger(StdOutSqlLogger)
        User.select {
            User.email eq user.email
        }.firstOrNull()
    }
    return id
}

