package com.simplify.pokeAPI.database

import org.jetbrains.exposed.sql.Database
import java.sql.Connection
import java.sql.DriverManager

import java.sql.SQLException


class DatabaseConnection {

    companion object{
        private const val dbUrl = "postgres://uqjzaykkfxetus:6f7169a2f6313a8cdeee8550689bd38dca6f89f13ca7fe250d1a309a4da30146@ec2-54-160-103-135.compute-1.amazonaws.com:5432/d3odencv819j1f"
        private const val dbUser = "uqjzaykkfxetus"
        private const val dbPass = "6f7169a2f6313a8cdeee8550689bd38dca6f89f13ca7fe250d1a309a4da30146"
    }

    fun connectDatabase() =
        Database.connect(dbUrl, driver = "org.postgresql.Driver", user = dbUser, password = dbPass)

    @Throws(SQLException::class)
    fun connect(): Connection? = DriverManager.getConnection(dbUrl, dbUser, dbPass)
}