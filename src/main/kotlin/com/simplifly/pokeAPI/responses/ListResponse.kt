package com.simplifly.pokeAPI.responses

data class ListResponse(val results: ArrayList<Results>)
data class Results(val name: String, val url: String)
