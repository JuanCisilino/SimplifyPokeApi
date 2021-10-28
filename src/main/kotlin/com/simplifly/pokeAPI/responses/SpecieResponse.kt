package com.simplifly.pokeAPI.responses

data class SpecieResponse(val evolution_chain: Evolution, val flavor_text_entries: ArrayList<Flavors>,
                          val evolves_from_species: EvolvesFrom?, val genera: ArrayList<Genero>)
data class Flavors(val flavor_text:String, val language: Language)
data class Language(val name: String)
data class EvolvesFrom(val name: String?)
data class Evolution(val url: String)
data class Genero(val genus: String, val language: Language)
