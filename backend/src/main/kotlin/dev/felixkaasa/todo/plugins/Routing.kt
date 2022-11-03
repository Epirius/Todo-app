package dev.felixkaasa.todo.plugins

import dev.felixkaasa.todo.schema.*
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

        get("/status") {
            call.respond(HttpStatusCode.OK, "Connection is healthy")
        }

        authenticate("auth-jwt") {
            get("/authstatus") {
                call.respond(HttpStatusCode.OK, "Connection is healthy, with auth")
            }

            post("/todo"){
                try{
                    println("[/todo] ")
                    val todo = call.receive<TaskJson>()
                    println("[/todo] received json")
                    val id = transaction {
                        addLogger(StdOutSqlLogger)
                        User.select {
                            User.email eq todo.email
                        }.firstOrNull()
                    }
                    println("[/todo] attempted to get user id")
                    if (id == null) {
                        println("[/todo] id was null")
                        call.respond(HttpStatusCode.InternalServerError, "[/todo] Could not find the user")
                        return@post
                    }
                    val tab = transaction {
                        addLogger(StdOutSqlLogger)
                        Tab.select{
                            Tab.userId eq id[userId]
                            Tab.tabName eq todo.tabName
                        }.firstOrNull()
                    }
                    if (tab ==  null){
                        println("[/todo] could not find the tab with given name")
                        call.respond(HttpStatusCode.BadRequest, "could not find the tab with the given name")
                        return@post
                    }
                    println("[/todo] found the correct tab")

                    println("name: "+ todo.taskName + "  desc: " + todo.description +"  date: " +todo.date+"  done: "+todo.done)

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

                } catch (e: Exception){
                    println("[/todo] error: $e")
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }
/*
            post("/todo/get"){
                try{
                    println("[/todo/get] ")
                    val tabInput = call.receive<TabJson>()
                    println("[/todo/get] received json")
                    val id = transaction {
                        addLogger(StdOutSqlLogger)
                        User.select {
                            User.email eq tabInput.email
                        }.firstOrNull()
                    }
                    println("[/todo/get] attempted to get user id")
                    if (id == null) {
                        println("[/todo/get] id was null")
                        call.respond(HttpStatusCode.InternalServerError, "[/todo/get] Could not find the user")
                        return@post
                    }
                    println("[/todo/get] attempting to get the tab")
                    val tab = transaction {
                        addLogger(StdOutSqlLogger)
                        Tab.select{
                            Tab.userId eq id[userId]
                            Tab.tabName eq tabInput.tabName
                        }.firstOrNull()
                    }
                    if (tab ==  null){
                        println("[/todo/get] could not find the tab with given name")
                        call.respond(HttpStatusCode.BadRequest, "[/todo/get] could not find the tab with the given name")
                        return@post
                    }
                    println("[/todo/get] found the tab")
                    val tasks = transaction {
                        Task.select{
                            Task.tabId eq tab[tabId]
                        }.toList().map { t ->
                            TASKJson(
                                t[taskName],
                                tab[tabName],
                                id[User.email],
                                t[description],
                                t[date],
                                t[done]
                           )
                        }
                    }
                    println("[/todo/get] created task list")

                    call.respond(tasks)
                    call.respond(HttpStatusCode.OK)

                } catch (e: Exception){
                    println("[/todo] error: $e")
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }

 */

            post("/tab") {
                try {
                    println("[/tab] ")
                    val tab = call.receive<TabJson>()
                    println("[/tab] received json")
                    val id = transaction {
                        addLogger(StdOutSqlLogger)
                        User.select {
                            User.email eq tab.email
                        }.firstOrNull()
                    }
                    println("[/tab] attempted to get user id")
                    if (id == null) {
                        println("[/tab] id was null")
                        call.respond(HttpStatusCode.InternalServerError, "[/tab] Could not find the user")
                        return@post
                    }
                    val userID = id[userId]
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

            post("/tab/getTabs") {
                try {
                    println("[/tab/getTabs] received request")
                    val user = call.receive<UserJson>()
                    println("[/tab/getTabs] received json")
                    val id = transaction {
                        addLogger(StdOutSqlLogger)
                        User.select {
                            User.email eq user.email
                        }.firstOrNull()
                    }
                    println("[/tab/getTabs] got id from Users")
                    if (id != null) {

                        println("[/tab/getTabs] id is not null")
                        val tabs = transaction {
                            Tab.select{
                                Tab.userId eq id[userId]
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
                        return@post
                        /*
                        println(tabList.joinToString(" "))
                        call.respond(tabList)
                        call.respond(HttpStatusCode.OK)

                         */
                    } else {

                        println("[/tab/getTabs] id is null")
                        call.respond(HttpStatusCode.BadRequest, "[/tab] could not find email from jwt token")
                    }
                } catch (e: Exception) {
                    println("[/tab] error: $e")
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }

            // end of auth
        }

        post("/login/token") {
            try {
                //TODO VERIFY USER FIRST
                val user = call.receive<UserJson>()
                println("[/login/token] recived request")
                val token = createToken(user.email)
                println("[/login/token] token created")
                call.respond(hashMapOf("token" to token))
                call.respond(HttpStatusCode.OK, "[/login/token] token created")
            } catch (e: Exception) {
                println("[/login/token] error: $e")
                call.respond(HttpStatusCode.InternalServerError)
            }
        }

        post("/login") {
            println("[/login] login was called")
            try {
                val user = call.receive<UserJson>()
                println("[/login] user json was received")
                val dbUser = transaction {
                    User.select {
                        User.email eq user.email
                    }.firstOrNull()
                }
                println("[/login] user was selected from database" + dbUser.toString())

                //val token = createToken(user.email)
                if (dbUser != null) {
                    //call.respond(hashMapOf("token" to token))
                    call.respond(HttpStatusCode.OK, "[/login] user exists")
                    return@post
                }


                println("[/login] user is not null, but not in database")

                // at this point the user is new, and needs to be added to the database.
                transaction {

                    println("[/login] transaction started")
                    addLogger(StdOutSqlLogger)
                    User.insert {
                        it[User.email] = user.email
                    }
                }
                //call.respond(hashMapOf("token" to token))
                call.respond(HttpStatusCode.OK, "[/login] user was created")
            } catch (e: Exception) {

                println("[/login] request failed with: $e")
                call.respond(HttpStatusCode.InternalServerError, "[/login] login was not successful")
            }
        }
    }
}
