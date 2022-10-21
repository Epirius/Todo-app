package dev.felixkaasa.todo.plugins

import com.auth0.jwt.JWT
import dev.felixkaasa.todo.schema.Tab
import dev.felixkaasa.todo.schema.TabJson
import dev.felixkaasa.todo.schema.User
import dev.felixkaasa.todo.schema.UserJson
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
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

        get("/status"){
            call.respond(HttpStatusCode.OK, "Connection is healthy")
        }

        authenticate ("auth-jwt") {
            get("/authstatus"){
                call.respond(HttpStatusCode.OK, "Connection is healthy, with auth")
            }

            post("/tab"){
                try {
                    val tab = call.receive<TabJson>()
                    val id = transaction {
                        addLogger(StdOutSqlLogger)
                        User.select{
                            User.email eq tab.email
                        }.firstOrNull()
                    }
                    if (id == null) {
                        call.respond(HttpStatusCode.InternalServerError, "[/tab] Could not find the user")
                        return@post
                    }
                    transaction {
                        addLogger(StdOutSqlLogger)
                        Tab.insert {
                            it[Tab.tabName] = tab.tabName //todo maybe regex check this
                            it[Tab.userId] = id[userId]
                        }
                    }
                    call.respond(HttpStatusCode.OK)
                    return@post
                } catch (e: Exception){
                    println("[/tab] error: $e")
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }

            get("/tab"){
                try {
                    val id = call.receive<UserJson>()
                    val tabs = transaction {
                        (User innerJoin Tab)
                            .select{User.email eq id.email}
                    }
                    call.respond(tabs)
                    call.respond(HttpStatusCode.OK)
                } catch (e: Exception){
                    println("[/tab] error: $e")
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }
        }

        post("/login/token"){
            try {
                //TODO VERIFY USER FIRST
                val user = call.receive<UserJson>()
                println("[/login/token] recived request")
                val token = createToken(user.email)
                println("[/login/token] token created")
                call.respond(hashMapOf("token" to token))
                call.respond(HttpStatusCode.OK, "[/login/token] token created")
            } catch (e :Exception){
                println("[/login/token] error: $e")
                call.respond(HttpStatusCode.InternalServerError)
            }
        }

        post("/login") {
            println("[/login] login was called")
            try {
                val user = call.receive<UserJson>()
                println("[/login] user json was received")
                val dbUser = transaction { User.select {
                    User.email eq user.email
                }.firstOrNull()}
                println("[/login] user was selected from database" + dbUser.toString())

                //val token = createToken(user.email)
                if (dbUser != null){
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
            } catch (e : Exception){

                println("[/login] request failed with: $e")
                call.respond(HttpStatusCode.InternalServerError, "[/login] login was not successful")
            }
        }

        post("/addtab/{tabname?}") {
            val tabname = call.parameters["tabname"]
            println("-1-1-1-1-1-1-1-1" + call.attributes.allKeys)
            println("token -------" + call.parameters.get("token"))
            if (tabname == null) { //TODO maybe add regex to check if tabname is valid
                call.respond(HttpStatusCode.BadRequest, "tabname is null")
            }
        }




        // ------------------------------------------------------------------------------------------------------------

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
