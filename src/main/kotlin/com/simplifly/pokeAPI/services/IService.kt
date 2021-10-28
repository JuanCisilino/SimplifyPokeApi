package com.simplifly.pokeAPI.services

import com.simplifly.pokeAPI.models.Pokemon

interface IService {

    fun findAll(): List<Pokemon>

    fun findByName(pokemon: String): Pokemon

    fun save(pokemon: Pokemon): Pokemon

}