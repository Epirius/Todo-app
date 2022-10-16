package dev.felixkaasa.todo.plugins

import dev.felixkaasa.todo.schema.User
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction


object Person : Table() {
    val name: Column<String> = text("name").uniqueIndex()
    val age: Column<Int> = integer("age")

    override val primaryKey = PrimaryKey(name);
}

@Serializable
data class PersonJson(
    val name: String,
    val age: Int,
)

fun Application.configureRouting() {

    routing {

        get("/") {
            call.respondText("Hello World!")
        }

        get("/status"){
            call.respond(HttpStatusCode.OK, "Connection is healthy")
        }

        post("/{email?}"){
            val email = call.parameters["email"]
            if (email == null){
                call.respond(HttpStatusCode.BadRequest, "hei. du sendte ikke et navn")
                return@post
            }
            try {
                transaction {
                    addLogger(StdOutSqlLogger)
                    User.insert {
                        it[User.email] = email
                    }
                }

            } catch (e : Exception){
                println(e)
                call.respond(HttpStatusCode.InternalServerError, e)
            }
        }

        get("/person"){
            try {
                val data = Person.selectAll()
                call.respond(HttpStatusCode.OK, data)
            } catch (e: Exception) {
                println(e)
                call.respond(HttpStatusCode.InternalServerError, "something went wrong!")
            }
        }

        post("/person") {
            try {
                val person = call.receive<PersonJson>()

                transaction {
                    addLogger(StdOutSqlLogger)

                    Person.insert {
                        it[name] = person.name
                        it[age] = person.age
                    }
                }

                call.respond(HttpStatusCode.OK, "Person inserted")

            } catch (e: Exception) {
                println(e)
                call.respond(HttpStatusCode.InternalServerError, "something went wrong!")
            }
        }
    }
}
