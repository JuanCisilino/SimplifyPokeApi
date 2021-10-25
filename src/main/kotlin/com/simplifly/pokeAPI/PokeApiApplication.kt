package com.simplifly.pokeAPI

import com.simplifly.pokeAPI.services.ServiceImpl
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PokeApiApplication

fun main(args: Array<String>) {
	runApplication<PokeApiApplication>(*args)
	ServiceImpl().getFirstList()
}
