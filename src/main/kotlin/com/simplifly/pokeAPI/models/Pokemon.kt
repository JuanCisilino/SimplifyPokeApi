package com.simplifly.pokeAPI.models

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("pokemon")
data class Pokemon(
    var id: Int?= null,
    @Id
    var name: String?= null,
    var nickName: String?= null,
    var baseUrl: String?= null,
    var moves : ArrayList<Movimiento> = ArrayList(),
    var listimg: String?= null,
    var detimg: String?= null,
    var type: ArrayList<Tipo> = ArrayList(),
    var evolvesTo: String?= null,
    var evolvesFrom: String?= null,
    var genere: ArrayList<String> = ArrayList(),
    var favorite: Boolean = false
)
data class Movimiento(var name: String?= null, var detail: String?= null)
data class Tipo(
    var name: String?= null, val doubleDamageTo: ArrayList<String> = ArrayList(),
    var doubleDamageFrom: ArrayList<String> = ArrayList(), val halfDamageTo:ArrayList<String> = ArrayList(),
    val halfDamageFrom: ArrayList<String> = ArrayList(), val noDamageTo:ArrayList<String> = ArrayList(),
    val noDamageFrom: ArrayList<String> = ArrayList())
