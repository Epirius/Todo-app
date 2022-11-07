package dev.felixkaasa.todo.schema

import kotlinx.serialization.Serializable
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
data class TaskJson(
    val taskName: String,
    val tabName: String,
    val email: String,
    val description: String,
    val date: String,
    val done: Boolean
    )

@Serializable
data class DelTaskJson(
    val tabName: String,
    val taskName: String,
    val email: String
)


object User : Table("USERS") {
    val userId: Column<Int> = integer("id").autoIncrement().uniqueIndex()
    val email: Column<String> = text("email").uniqueIndex()
    override val primaryKey = PrimaryKey(email)
}

object Tab : Table("TABS") {
    var tabName = text("tabName")
    val userId = reference("userId", User.userId)
    val tabId: Column<Int> = integer("tabId").autoIncrement().uniqueIndex()
    override  val primaryKey = PrimaryKey(tabId)
}

object Task: Table("TASKS") {
    var taskName: Column<String> =text("taskName")
    val description: Column<String> = varchar("description", 255)
    val date: Column<String> = varchar("date",255)
    val done: Column<Boolean> = bool("done")
    val tabId = reference("tabId", Tab.tabId)
    private val taskId: Column<Int> = integer("taskId").autoIncrement().uniqueIndex()
    override  val primaryKey = PrimaryKey(taskId)
}