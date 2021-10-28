package com.simplifly.pokeAPI.responses

data class TypeResponse(val damage_relations: DoubleDamageFrom)
data class DoubleDamageFrom(val double_damage_from: ArrayList<Type>, val double_damage_to: ArrayList<Type>,
                            val half_damage_from: ArrayList<Type>, val half_damage_to: ArrayList<Type>,
                            val no_damage_from: ArrayList<Type>, val no_damage_to: ArrayList<Type>)
