package com.simplifly.pokeAPI.services

import com.simplifly.pokeAPI.models.Movimiento
import com.simplifly.pokeAPI.models.Pokemon
import com.simplifly.pokeAPI.models.Tipo
import com.simplifly.pokeAPI.responses.*
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlux
import reactor.core.publisher.Flux

@Service
class ServiceImpl: IService {

    companion object{ val pokemonList = ArrayList<Pokemon>() }
    private val restTemplate = RestTemplate()

    override fun findAll() = pokemonList

    override fun findByName(pokemon: String): Pokemon = pokemonList.find { it.name == pokemon }?:Pokemon()

    override fun save(pokemon: Pokemon) = pokemon

    fun getFirstList() {
        val fluxList: Flux<ListResponse> = WebClient.create()
            .get()
            .uri("https://pokeapi.co/api/v2/pokemon?offset=0&limit=1118")
            .retrieve()
            .bodyToFlux()
        fluxList.subscribe{ response ->
            response.results.forEach { setPokemon(it) }
            pokemonList.size
        }
    }

    private fun setPokemon(results: Results) {
        val pokemon = Pokemon()
        pokemon.id = pokemonList.size + 1
        pokemon.name = results.name
        pokemon.baseUrl = results.url
        getParts(pokemon)
        pokemonList.add(pokemon)
    }

    private fun getParts(pokemon: Pokemon) {
        val response : ResponseEntity<PokeResponse>? = pokemon.baseUrl?.let { restTemplate.exchange(it, HttpMethod.GET, null) }
        pokemon.listimg = response?.body?.sprites?.front_default
        pokemon.detimg = "${response?.body?.sprites?.artWork}${pokemon.id}.png"
        response?.body?.moves?.forEach { move -> setMovimiento(move, pokemon) }
        response?.body?.types?.forEach { type -> setTipo(type, pokemon) }
        response?.body?.species?.url?.let { setSpecie(it, pokemon) }
    }

    private fun setMovimiento(move: Moves, pokemon: Pokemon) {
        val response : ResponseEntity<MoveResponse> = restTemplate.exchange(move.move.url, HttpMethod.GET, null)
        val movimiento = Movimiento()
        movimiento.name = response.body?.names?.first { it.language.name == "es" }?.name
        movimiento.detail = response.body?.flavor_text_entries?.first { it.language.name == "es" }?.flavor_text
        pokemon.moves.add(movimiento)
    }

    private fun setTipo(type: Types, pokemon: Pokemon) {
        val response : ResponseEntity<TypeResponse> = restTemplate.exchange(type.type.url, HttpMethod.GET, null)
        val tipo = Tipo()
        tipo.name = type.type.name
        response.body?.damage_relations?.double_damage_from?.forEach { tipo.doubleDamageFrom.add(it.name) }
        response.body?.damage_relations?.double_damage_to?.forEach { tipo.doubleDamageTo.add(it.name) }
        response.body?.damage_relations?.half_damage_from?.forEach { tipo.halfDamageFrom.add(it.name) }
        response.body?.damage_relations?.half_damage_to?.forEach { tipo.halfDamageTo.add(it.name) }
        response.body?.damage_relations?.no_damage_from?.forEach { tipo.noDamageFrom.add(it.name) }
        response.body?.damage_relations?.no_damage_to?.forEach { tipo.noDamageTo.add(it.name) }
        pokemon.type.add(tipo)
    }

    private fun setSpecie(url: String, pokemon: Pokemon) {
        val response : ResponseEntity<SpecieResponse> = restTemplate.exchange(url, HttpMethod.GET, null)
        pokemon.evolvesFrom = response.body?.evolves_from_species?.name
        response.body?.flavor_text_entries?.forEach { if (it.language.name == "es") pokemon.genere.add(it.flavor_text) }
        response.body?.evolution_chain?.url?.let { setEvolution(it, pokemon) }
    }

    private fun setEvolution(url: String, pokemon: Pokemon) {
        val response : ResponseEntity<EvolutionResponse> = restTemplate.exchange(url, HttpMethod.GET, null)
        val secondForm = if (response.body?.chain?.evolves_to?.isEmpty() == true) null
        else response.body?.chain?.evolves_to?.first()?.species?.name
        val thirdForm = if (secondForm == null ||
            response.body?.chain?.evolves_to?.first()?.evolves_to?.isEmpty() == true ||
            pokemon.evolvesTo == pokemon.evolvesFrom ) null
        else response.body?.chain?.evolves_to?.first()?.evolves_to?.first()?.species?.name
        pokemon.evolvesFrom
            ?.let { pokemon.evolvesTo = if (thirdForm == pokemon.name) null else thirdForm }
            ?:run { pokemon.evolvesTo = secondForm }
    }
}