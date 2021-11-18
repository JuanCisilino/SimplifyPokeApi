package com.simplify.pokeAPI.responses


data class PokeResponse(val moves: ArrayList<Moves>, val species: Species, val sprites: Sprites, val types: ArrayList<Types>)
data class Moves(val move: Move)
data class Move(val url: String)
data class Species(val name: String, val url: String)
data class Sprites(val front_default: String?, val artWork: String = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/")
data class Types(val type: Type)
data class Type(val name: String, val  url: String)
