package com.simplifly.pokeAPI.responses

data class TypeResponse(val names: ArrayList<Names>, val damage_relations: DamageRelations)
data class DamageRelations(val double_damage_from: ArrayList<Type>, val double_damage_to: ArrayList<Type>,
                            val no_damage_from: ArrayList<Type>, val no_damage_to: ArrayList<Type>)
