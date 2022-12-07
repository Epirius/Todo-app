package dev.felixkaasa.todo.plugins

import dev.felixkaasa.todo.schema.*
import dev.felixkaasa.todo.schema.Task.done
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

fun Route.todo() {
    post("/todo") {
        println("------------------< todo/create >-----------------------")
        val todo = call.receive<TaskJson>()
        val email: String? = call.principal<JWTPrincipal>()?.payload?.getClaim("email")?.asString()?.lowercase()
        if (email == null){
            call.respond(HttpStatusCode.BadRequest, "could not find the email inside the jwt token")
            return@post
        }
        val id = getUser(email)
        if (id == null) {
            call.respond(HttpStatusCode.InternalServerError, "[/todo] Could not find the user")
            return@post
        }

        val tab = getTab(id, todo.tabName)
        if (tab == null) {
            call.respond(HttpStatusCode.BadRequest, "could not find the tab with the given name")
            return@post
        }

        transaction {
            addLogger(StdOutSqlLogger)
            Task.insert {
                it[taskName] = todo.taskName
                it[description] = todo.description
                it[date] = todo.date
                it[done] = todo.done
                it[tabId] = tab[Tab.tabId]
            }
        }
        call.respond(HttpStatusCode.OK)
        println("------------------</ todo/create >-----------------------")
    }

    get("/todo/{tab}") {
        println("------------------< todo/get >-----------------------")
        val tabName = call.parameters["tab"]
        if (tabName == null){
            call.respond(HttpStatusCode.BadRequest, "[get todo] tab name was null")
            return@get
        }

        val email: String? = call.principal<JWTPrincipal>()?.payload?.getClaim("email")?.asString()?.lowercase()
        if (email == null){
            call.respond(HttpStatusCode.BadRequest, "could not find the email inside the jwt token")
            return@get
        }

        val id = getUser(email)
        if (id == null) {
            call.respond(HttpStatusCode.InternalServerError, "[/todo/get] Could not find the user")
            return@get
        }

        val tab = getTab(id, tabName)
        if (tab == null) {
            call.respond(HttpStatusCode.BadRequest, "[/todo/get] could not find the tab with the given name")
            return@get
        }
        val tasks = transaction {
            Task.select {
                Task.tabId eq tab[Tab.tabId]
            }.toList().map { t ->
                TaskJson(
                    t[Task.taskName],
                    tab[Tab.tabName],
                    id[User.email],
                    t[Task.description],
                    t[Task.date],
                    t[Task.done]
                )
            }
        }
        call.respond(tasks)
        call.respond(HttpStatusCode.OK)
        println("------------------</ todo/get >-----------------------")
    }

    delete("/todo/{tab}/{task}"){
        println("------------------< todo/delete >-----------------------")
        val tabName = call.parameters["tab"]
        val taskName = call.parameters["task"]
        val email: String? = call.principal<JWTPrincipal>()?.payload?.getClaim("email")?.asString()?.lowercase()
        if (tabName == null || taskName == null || email == null){
            call.respond(HttpStatusCode.BadRequest, "[delete todo] some input was null")
            return@delete
        }

        val id = getUser(email)
        if (id == null) {
            call.respond(HttpStatusCode.InternalServerError, "[/todo/delete] Could not find the user")
            return@delete
        }

        val tab = getTab(id, tabName)
        if (tab == null) {
            call.respond(HttpStatusCode.BadRequest, "[/todo/delete] could not find the tab with the given name")
            return@delete
        }
        transaction {
            addLogger(StdOutSqlLogger)
            Task.deleteWhere {
                tabId eq tab[Tab.tabId]
                Task.taskName eq taskName
            }
        }
        call.respond(HttpStatusCode.OK)
        println("------------------</ todo/delete >-----------------------")
    }

    post("/todo/checkbox/{tab}/{task}/{bool}"){
        println("------------------< todo/checkbox >-----------------------")
        val tabName = call.parameters["tab"]
        val taskName = call.parameters["task"]
        val checkboxValue = call.parameters["bool"]

        if (tabName == null || taskName == null || checkboxValue == null){
            call.respond(HttpStatusCode.BadRequest, "[todo/checkbox] some of the url input variables was null")
            return@post
        }

        val email: String? = call.principal<JWTPrincipal>()?.payload?.getClaim("email")?.asString()?.lowercase()
        if (email == null){
            call.respond(HttpStatusCode.BadRequest, "could not find the email inside the jwt token")
            return@post
        }

        val id = getUser(email)
        if (id == null) {
            call.respond(HttpStatusCode.InternalServerError, "[todo] Could not find the user")
            return@post
        }

        val tab = getTab(id, tabName)
        if (tab == null){
            call.respond(HttpStatusCode.InternalServerError, "[todo] Could not find the tab")
            return@post
        }

        transaction {
            addLogger(StdOutSqlLogger)
            Task.update ({
                Task.tabId eq tab[Tab.tabId] and
                (Task.taskName eq taskName)
            }){
                it[done] = checkboxValue.contains("true")
            }
        }
        call.respond(HttpStatusCode.OK,)
        println("------------------</ todo/checkbox >-----------------------")
    }

    get("/todo/checkbox/{tab}/{task}"){
        println("------------------< get/todo/checkbox >-----------------------")
        val tabName = call.parameters["tab"]
        val taskName = call.parameters["task"]

        if (tabName == null || taskName == null){
            call.respond(HttpStatusCode.BadRequest, "[todo/checkbox] some of the url input variables was null")
            return@get
        }

        val email: String? = call.principal<JWTPrincipal>()?.payload?.getClaim("email")?.asString()?.lowercase()
        if (email == null){
            call.respond(HttpStatusCode.BadRequest, "could not find the email inside the jwt token")
            return@get
        }

        val id = getUser(email)
        if (id == null) {
            call.respond(HttpStatusCode.InternalServerError, "[todo] Could not find the user")
            return@get
        }

        val tab = getTab(id, tabName)
        if (tab == null){
            call.respond(HttpStatusCode.InternalServerError, "[todo] Could not find the tab")
            return@get
        }

        val checkboxValue = transaction {
            addLogger(StdOutSqlLogger)
            Task.select {
                Task.tabId eq tab[Tab.tabId] and
                        (Task.taskName eq taskName)
            }.firstOrNull()?.get(done)
        }

        if (checkboxValue === null){
            call.respond(HttpStatusCode.InternalServerError, "the result of the checkbox value was null")
            return@get
        }
        call.respond(HttpStatusCode.OK, checkboxValue)
        println("------------------</ get/todo/checkbox >-----------------------")
    }
}



