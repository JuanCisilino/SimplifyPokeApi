package com.simplifly.pokeAPI.models

data class PokeLocal(
    var id: Long?,
    var name: String?,
    var nickName: String?,
    var baseUrl: String?,
    var listimg: String?,
    var detimg: String?,
    var type: String?,
    var evolvesTo: String?,
    var evolvesFrom: String?,
    var flavor: String?,
    var favorite: Boolean?,
    var strongAgainst: String?,
    var weakAgainst: String?,
    var noDamageTo: String?,
    var noDamageFrom: String?
)
