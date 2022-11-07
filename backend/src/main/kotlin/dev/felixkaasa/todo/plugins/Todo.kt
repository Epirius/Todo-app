package dev.felixkaasa.todo.plugins

import dev.felixkaasa.todo.schema.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.todo() {
    post("/todo") {
        try {
            println("[/todo] ")
            val todo = call.receive<TaskJson>()
            println("[/todo] received json")
            val id = getUser(todo)
            println("[/todo] attempted to get user id")
            if (id == null) {
                println("[/todo] id was null")
                call.respond(HttpStatusCode.InternalServerError, "[/todo] Could not find the user")
                return@post
            }
            val tab = transaction {
                addLogger(StdOutSqlLogger)
                Tab.select {
                    Tab.userId eq id[User.userId]
                    Tab.tabName eq todo.tabName
                }.firstOrNull()
            }
            if (tab == null) {
                println("[/todo] could not find the tab with given name")
                call.respond(HttpStatusCode.BadRequest, "could not find the tab with the given name")
                return@post
            }
            println("[/todo] found the correct tab")

            println("name: " + todo.taskName + "  desc: " + todo.description + "  date: " + todo.date + "  done: " + todo.done)

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
            println("[/todo] task was inserted")
            call.respond(HttpStatusCode.OK)

        } catch (e: Exception) {
            println("[/todo] error: $e")
            call.respond(HttpStatusCode.InternalServerError)
        }
    }

    post("/todo/get") {
        try {
            println("[/todo/get] ")
            val tabInput = call.receive<TabJson>()
            println("[/todo/get] received json")
            val id = getUser(tabInput)
            println("[/todo/get] attempted to get user id")
            if (id == null) {
                println("[/todo/get] id was null")
                call.respond(HttpStatusCode.InternalServerError, "[/todo/get] Could not find the user")
                return@post
            }
            println("[/todo/get] attempting to get the tab")
            val tab = getTab(id, tabInput)
            if (tab == null) {
                println("[/todo/get] could not find the tab with given name")
                call.respond(HttpStatusCode.BadRequest, "[/todo/get] could not find the tab with the given name")
                return@post
            }
            println("[/todo/get] found the tab")
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
            println("[/todo/get] created task list")

            call.respond(tasks)
            call.respond(HttpStatusCode.OK)

        } catch (e: Exception) {
            println("[/todo] error: $e")
            call.respond(HttpStatusCode.InternalServerError)
        }
    }

    post("/todo/delete"){
        try{
            println("[/todo/delete] ")
            val delTaskInput = call.receive<DelTaskJson>()
            println("[/todo/delete] received json")
            val id = getUser(delTaskInput)
            println("[/todo/delete] attempted to get user id")
            if (id == null) {
                println("[/todo/delete] id was null")
                call.respond(HttpStatusCode.InternalServerError, "[/todo/delete] Could not find the user")
                return@post
            }
            println("[/todo/delete] attempting to get the tab")
            val tab = getTab(id, delTaskInput)
            if (tab == null) {
                println("[/todo/delete] could not find the tab with given name")
                call.respond(HttpStatusCode.BadRequest, "[/todo/delete] could not find the tab with the given name")
                return@post
            }
            println("[/todo/delete] tab is not null")
            transaction {
                addLogger(StdOutSqlLogger)
                Task.deleteWhere {
                    Task.tabId eq tab[Tab.tabId]
                    Task.taskName eq delTaskInput.taskName
                }
            }
            println("[/todo/delete] deleted task")
            call.respond(HttpStatusCode.OK)
        } catch (e: Exception) {
            println("[/todo/delete] error: $e")
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
}

fun getTab(
    id: ResultRow,
    tabInput: TabJson
): ResultRow? {
    val tab = transaction {
        addLogger(StdOutSqlLogger)
        Tab.select {
            Tab.userId eq id[User.userId]
            Tab.tabName eq tabInput.tabName
        }.firstOrNull()
    }
    println("[/todo/delete]  ----------  tab: $tab")
    return tab
}

fun getTab(
    id: ResultRow,
    tabInput: DelTaskJson
): ResultRow? {
    val tab = transaction {
        addLogger(StdOutSqlLogger)
        Tab.select {
            Tab.userId eq id[User.userId]
            Tab.tabName eq tabInput.tabName
        }.firstOrNull()
    }
    println("[/todo/delete]  ----------  tab: $tab")
    return tab
}

//TODO make sure tabs dont have the same name

