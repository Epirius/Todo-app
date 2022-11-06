package dev.felixkaasa.todo.plugins

import dev.felixkaasa.todo.schema.Tab
import dev.felixkaasa.todo.schema.TabJson
import dev.felixkaasa.todo.schema.User
import dev.felixkaasa.todo.schema.UserJson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.tab(){

    post("/tab/getTabs") {
        try {
            println("[/tab/getTabs] received request")
            val user = call.receive<UserJson>()
            println("[/tab/getTabs] received json")
            val id = getUser(user)
            println("[/tab/getTabs] got id from Users")
            if (id != null) {

                println("[/tab/getTabs] id is not null")
                val tabs = transaction {
                    Tab.select {
                        Tab.userId eq id[User.userId]
                    }.toList().map { t ->
                        TabJson(
                            t[Tab.tabName],
                            id[User.email]
                        )
                    }
                }
                println("[/tab/getTabs] created list")
                call.respond(tabs)
                call.respond(HttpStatusCode.OK)
            } else {

                println("[/tab/getTabs] id is null")
                call.respond(HttpStatusCode.BadRequest, "[/tab] could not find email from jwt token")
            }
        } catch (e: Exception) {
            println("[/tab] error: $e")
            call.respond(HttpStatusCode.InternalServerError)
        }
    }

    post("/tab") {
        try {
            println("[/tab] ")
            val tab = call.receive<TabJson>()
            println("[/tab] received json")
            val id = getUser(tab)
            println("[/tab] attempted to get user id")
            if (id == null) {
                println("[/tab] id was null")
                call.respond(HttpStatusCode.InternalServerError, "[/tab] Could not find the user")
                return@post
            }
            val userID = id[User.userId]
            transaction {
                addLogger(StdOutSqlLogger)
                Tab.insert {
                    it[tabName] = tab.tabName //todo maybe regex check this
                    it[userId] = userID
                }
            }
            println("[/tab] tab inserted")
            call.respond(HttpStatusCode.OK)
            return@post
        } catch (e: Exception) {
            println("[/tab] error: $e")
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
}