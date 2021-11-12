package com.simplifly.pokeAPI.database

import org.jetbrains.exposed.sql.Database
import java.sql.Connection
import java.sql.DriverManager

import java.sql.SQLException

class DatabaseConnection {

    companion object{
        private const val dbUrl = "jdbc:postgresql://0.0.0.0:5439/pokemon"
        private const val dbUser = "postgres"
        private const val dbPass = "sa"
    }

    fun connectDatabase() =
        Database.connect(dbUrl, driver = "org.postgresql.Driver", user = dbUser, password = dbPass)

    @Throws(SQLException::class)
    fun connect(): Connection? = DriverManager.getConnection(dbUrl, dbUser, dbPass)
}