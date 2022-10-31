package dev.felixkaasa.todo.schema

import dev.felixkaasa.todo.schema.User.autoIncrement
import dev.felixkaasa.todo.schema.User.uniqueIndex
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
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

object Tab : Table("TABS") {
    var tabName = text("tabName")
    val userId = reference("userId", User.userId)
    val tabId: Column<Int> = integer("tabId").autoIncrement().uniqueIndex()
    override  val primaryKey = PrimaryKey(tabId)
}

object Task: IntIdTable("TASKS") {
    var taskName = text("taskName")
    val tabId = reference("tabId", Tab.tabId)
}