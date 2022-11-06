package dev.felixkaasa.todo.plugins

import dev.felixkaasa.todo.schema.User
import dev.felixkaasa.todo.schema.UserJson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.login() {
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
                    it[email] = user.email
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