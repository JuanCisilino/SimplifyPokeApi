package com.simplify.pokeAPI.responses

data class EvolutionResponse(val chain: Chain?)
data class Chain(val evolves_to: ArrayList<SecondForm>?)
data class SecondForm(val species: Species?, val evolves_to: ArrayList<ThirdForm>?)
data class ThirdForm(val species: Species?)