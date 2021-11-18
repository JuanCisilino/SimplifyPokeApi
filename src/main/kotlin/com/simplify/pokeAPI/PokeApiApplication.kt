package com.simplify.pokeAPI

import com.simplify.pokeAPI.database.DatabaseConnection
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class PokeApiApplication

fun main(args: Array<String>) {
	DatabaseConnection().connectDatabase()
	runApplication<PokeApiApplication>(*args)
}

