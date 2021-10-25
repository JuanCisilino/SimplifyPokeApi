package com.simplifly.pokeAPI.responses

data class MoveResponse(val names: ArrayList<Names>, val flavor_text_entries: ArrayList<Flavors>)
data class Names(val name: String, val language: Language)
