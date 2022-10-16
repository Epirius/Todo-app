package dev.felixkaasa.todo.schema

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

@Serializable
data class UserJson(
    val email: String,

)


object User : Table() {
    val email: Column<String> = text("email").uniqueIndex()
    override val primaryKey = PrimaryKey(email)
}
/*
object Tab : IntIdTable(){
    val parent = reference("parent_node_id", User)
}
*/