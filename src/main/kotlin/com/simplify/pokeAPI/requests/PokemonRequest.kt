package com.simplify.pokeAPI.requests

import javax.validation.constraints.NotEmpty

data class PokemonRequest(
    @field:NotEmpty
    var name: String,
    val nickName: String
)
