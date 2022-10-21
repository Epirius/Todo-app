package dev.felixkaasa.todo.schema

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

@Serializable
data class UserJson(
    val email: String,
)

@Serializable
data class TabJson(
    val tabName: String,
    val email: String
    )

@Serializable
data class TASKJson(
    val taskName: String,
    val email: String
    )


object User : Table("USERS") {
    val userId: Column<Int> = integer("id").autoIncrement().uniqueIndex()
    val email: Column<String> = text("email").uniqueIndex()
    override val primaryKey = PrimaryKey(email)
}

object Tab : IntIdTable("TABS") {
    var tabName = text("tabName")
    val userId = reference("userId", User.userId)
}

object Task: IntIdTable("TASKS") {
    var taskName = text("taskName")
    val tabId = reference("tabId", Tab)
}