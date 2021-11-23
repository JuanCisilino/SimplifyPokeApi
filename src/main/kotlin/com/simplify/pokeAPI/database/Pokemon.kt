package com.simplify.pokeAPI.database

import com.simplify.pokeAPI.models.PokeLocal
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Pokemon: Table() {
    val id = integer("id")
    val name = varchar("name", 100)
    val nickName = varchar("nick_name", 3000)
    val favorite = bool("favorite")
    val types = varchar("types", 3000)
    val evolvesTo = varchar("evolves_to", 3000)
    val evolvesFrom = varchar("evolves_from", 3000)
    val baseUrl = varchar("base_url", 3000)
    val listimg = varchar("list_img", 3000)
    val detimg = varchar("det_img", 3000)
    val flavor = varchar("flavor", 30000)
    var strongAgainst = varchar("strong_against", 3000)
    var weakAgainst = varchar("weak_against", 3000)
    var noDamageTo = varchar("no_damage_to", 3000)
    var noDamageFrom = varchar("no_damage_from", 3000)

    fun getAll(): List<Any> = transaction {
        Pokemon.selectAll().map { convertToPokemon(it) }.sortedBy { it.id } }

    fun getByName(nombre: String): PokeLocal = transaction {
        val selected = Pokemon.selectAll().find { it[name] == nombre }
        return@transaction convertToPokemon(selected)
    }

    fun getFavorites(): List<Any> = transaction {
        Pokemon.selectAll().filter { it[favorite] }.map { convertToPokemon(it) }.sortedBy { it.id } }

    private fun convertToPokemon(raw: ResultRow?): PokeLocal {
        return PokeLocal(
            id = raw?.get(id),
            name = raw?.get(name),
            baseUrl = raw?.get(baseUrl),
            nickName = raw?.get(nickName),
            detimg = raw?.get(detimg),
            listimg = raw?.get(listimg),
            evolvesFrom = raw?.get(evolvesFrom),
            evolvesTo = raw?.get(evolvesTo),
            flavor = raw?.get(flavor),
            type = raw?.get(types),
            favorite = raw?.get(favorite),
            strongAgainst = raw?.get(strongAgainst),
            weakAgainst = raw?.get(weakAgainst),
            noDamageTo = raw?.get(noDamageTo),
            noDamageFrom = raw?.get(noDamageFrom),
        )
    }

    fun getPage(limit: Int, offset: Int): List<PokeLocal> = transaction {
        val list = Pokemon.selectAll().map { convertToPokemon(it) }.sortedBy { it.id }
        return@transaction list.take(offset).takeLast(limit)
    }
}