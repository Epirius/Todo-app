package dev.felixkaasa.todo.plugins

import dev.felixkaasa.todo.schema.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.tab(){

    get("/tab") {
        println("------------------< tab/getTabs >-----------------------")

        val email: String? = call.principal<JWTPrincipal>()?.payload?.getClaim("email")?.asString()?.lowercase()
        if (email == null){
            call.respond(HttpStatusCode.BadRequest, "could not find the eamil inside the jwt token")
            return@get
        }

        val id = getUser(email)
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "[/tab] could not find email from jwt token")
            return@get
        }

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
        call.respond(tabs)
        call.respond(HttpStatusCode.OK)

        println("------------------</ tab/getTabs >-----------------------")

    }

    post("/tab/{tab}") {
        println("------------------< tab/create >-----------------------")
        val tab = call.parameters["tab"]
        if (tab == null){
            call.respond(HttpStatusCode.BadRequest, "[tab create] tabname was null")
            return@post
        }

        val email: String? = call.principal<JWTPrincipal>()?.payload?.getClaim("email")?.asString()?.lowercase()
        if (email == null){
            call.respond(HttpStatusCode.BadRequest, "could not find the eamil inside the jwt token")
            return@post
        }

        val id = getUser(email)
        if (id == null) {
            call.respond(HttpStatusCode.InternalServerError, "[/tab] Could not find the user")
            return@post
        }

        transaction {
            addLogger(StdOutSqlLogger)
            Tab.insert {
                it[tabName] = tab //todo maybe regex check this
                it[userId] = id[User.userId]
            }
        }
        call.respond(HttpStatusCode.OK)
        println("------------------</ tab/create >-----------------------")
    }

    delete("/tab/{tab}"){
        println("------------------< tab/delete >-----------------------")
        val tabName = call.parameters["tab"]
        if (tabName == null){
            call.respond(HttpStatusCode.BadRequest, "[tab create] tabname was null")
            return@delete
        }

        val email: String? = call.principal<JWTPrincipal>()?.payload?.getClaim("email")?.asString()?.lowercase()
        if (email == null){
            call.respond(HttpStatusCode.BadRequest, "could not find the eamil inside the jwt token")
            return@delete
        }

        val id = getUser(email)
        if (id == null) {
            call.respond(HttpStatusCode.InternalServerError, "[tab delete] Could not find the user")
            return@delete
        }

        val tab = getTab(id, tabName)
        if (tab == null){
            call.respond(HttpStatusCode.InternalServerError, "[tab delete] Could not find the tab")
            return@delete
        }

        transaction {
            addLogger(StdOutSqlLogger)
            Task.deleteWhere {
                tabId eq tab[Tab.tabId]
            }
            Tab.deleteWhere {
                tabId eq tab[tabId]
            }
        }
        call.respond(HttpStatusCode.OK)
        println("------------------</ tab/delete >-----------------------")

    }
}