package com.simplifly.pokeAPI.requests

import javax.validation.constraints.NotEmpty

data class PokemonRequest(
    @field:NotEmpty
    var name: String,
    @field:NotEmpty
    val nickName: String
)
