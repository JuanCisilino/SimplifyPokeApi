package com.simplify.pokeAPI.controller

import com.simplify.pokeAPI.database.DatabaseConnection
import com.simplify.pokeAPI.database.Pokemon
import com.simplify.pokeAPI.models.PokeLocal
import com.simplify.pokeAPI.requests.PokemonRequest
import com.simplify.pokeAPI.responses.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import java.sql.SQLException
import javax.validation.Valid

@RestController
@RequestMapping("/pokemon", produces = [MediaType.APPLICATION_JSON_VALUE])
class Controller {

    companion object{ var realId = 1 }
    private val restTemplate = RestTemplate()

    @GetMapping("/{pokemon}")
    @ResponseBody
    suspend fun getByName(@PathVariable pokemon: String): ResponseEntity<PokeLocal>{
        val selectedPokemon = Pokemon.getByName(pokemon)
        return ResponseEntity(selectedPokemon, HttpStatus.OK)
    }

    @GetMapping("/")
    suspend fun paginatedList(@RequestParam pageNo: Int, @RequestParam pageSize: Int): PagingResponse<PokeLocal> {
        val offset = pageSize * pageNo
        val list = Pokemon.getPage(pageSize, offset)
        val total = Pokemon.getAll().size.toLong()
        return PagingResponse(total, list)
    }

    @PostMapping("/")
    suspend fun updateList(): ResponseEntity<Any?> {
        val response : ResponseEntity<ListResponse> =
            restTemplate.exchange("https://pokeapi.co/api/v2/pokemon?offset=0&limit=1500",
                HttpMethod.GET, null)
        val listSize = Pokemon.getAll().size
        if (listSize == response.body?.results?.size) return ResponseEntity("Up to Date", HttpStatus.OK)
        updateList(response)
        return ResponseEntity("Updated Successfully", HttpStatus.OK)
    }

    @PatchMapping("")
    suspend fun updatePokemon(@RequestBody @Valid pokemonRequest: PokemonRequest): ResponseEntity<Any> {
        val sql = "UPDATE pokemon SET nick_name = ? WHERE name = ?"
        try {
            DatabaseConnection().connect().use { conn ->
                conn?.prepareStatement(sql).use { pstmt ->
                    pstmt?.setString(1, pokemonRequest.nickName)
                    pstmt?.setString(2, pokemonRequest.name)
                    pstmt?.executeUpdate()
                }
            }
        } catch (ex: SQLException) {
            return ResponseEntity(ex.message, HttpStatus.NOT_MODIFIED)
        }
        return ResponseEntity(Pokemon.getByName(pokemonRequest.name), HttpStatus.OK)
    }

    @PatchMapping("/{name}")
    suspend fun updatePokemon(@PathVariable name: String): ResponseEntity<Any> {
        val boolean = Pokemon.getByName(name).favorite?.let { !it }?:true
        val sql = "UPDATE pokemon SET favorite = ? WHERE name = ?"
        try {
            DatabaseConnection().connect().use { conn ->
                conn?.prepareStatement(sql).use { pstmt ->
                    pstmt?.setBoolean(1, boolean)
                    pstmt?.setString(2, name)
                    pstmt?.executeUpdate()
                }
            }
        } catch (ex: SQLException) {
            return ResponseEntity(ex.message, HttpStatus.NOT_MODIFIED)
        }
        return ResponseEntity(Pokemon.getByName(name), HttpStatus.OK)
    }

    private fun updateList(response: ResponseEntity<ListResponse>) {
        transaction {
            addLogger(StdOutSqlLogger)
            // IMPORTANTE --- La primera vez debe estar comentada la linea de drop para que pueda crear el
            //                objeto pokemon en la base de datos, tambien cambiar el pedido del metodo updateList()
            //                cambiar Pokemon.getAll().size por 1
            SchemaUtils.drop (Pokemon)
            SchemaUtils.create (Pokemon)
            response.body?.results?.forEach { result ->
                realId = result.url.takeLast(10).filter { it.isDigit() }.toInt()
                val pokemon = setPokemon(result)
                insertToDb(pokemon)
            }
        }
    }

    private fun insertToDb(pokemon: PokeLocal) {
        Pokemon.insert {
            it[id] = realId
            it[name] = pokemon.name?:""
            it[nickName] = ""
            it[baseUrl] = pokemon.baseUrl?:""
            it[listimg] = pokemon.listimg?:""
            it[detimg] = pokemon.detimg?:""
            it[evolvesTo] = pokemon.evolvesTo?:""
            it[evolvesFrom] = pokemon.evolvesFrom?:""
            it[flavor] = pokemon.flavor?:""
            it[types] = pokemon.type?:""
            it[favorite] = pokemon.favorite?:false
            it[strongAgainst] = pokemon.strongAgainst?:""
            it[weakAgainst] = pokemon.weakAgainst?:""
            it[noDamageTo] = pokemon.noDamageTo?:""
            it[noDamageFrom] = pokemon.noDamageFrom?:""
        }
    }

    private fun setPokemon(results: Results): PokeLocal {
        val pokemon = PokeLocal(
            id = realId,
            name = results.name,
            baseUrl = results.url,
            nickName = null,
            detimg = null,
            listimg = null,
            evolvesFrom = null,
            evolvesTo = null,
            flavor = null,
            type = null,
            favorite = null,
            noDamageFrom = null,
            noDamageTo = null,
            strongAgainst = null,
            weakAgainst = null)
        getParts(pokemon)
        return pokemon
    }

    private fun getParts(pokemon: PokeLocal): PokeLocal {
        val response : ResponseEntity<PokeResponse>? = pokemon.baseUrl?.let { restTemplate.exchange(it, HttpMethod.GET, null) }
        pokemon.listimg = response?.body?.sprites?.front_default
        pokemon.detimg = "${response?.body?.sprites?.artWork}${realId}.png"
        setTypes(response, pokemon)
        response?.body?.species?.url?.let { setSpecie(it, pokemon) }
        return pokemon
    }

    private fun setTypes(response: ResponseEntity<PokeResponse>?, pokemon: PokeLocal){
        val noDamageTo = ArrayList<String>()
        val noDamageFrom = ArrayList<String>()
        val strongAgainst = ArrayList<String>()
        val weakAgainst = ArrayList<String>()
        val types = ArrayList<String>()
        response?.body?.types?.forEach { eachType(it, types, noDamageTo, noDamageFrom, strongAgainst, weakAgainst) }
        pokemon.type = types.joinToString(";")
        pokemon.noDamageFrom = noDamageFrom.joinToString(";")
        pokemon.noDamageTo = noDamageTo.joinToString(";")
        pokemon.strongAgainst = strongAgainst.joinToString(";")
        pokemon.weakAgainst = weakAgainst.joinToString(";")
    }

    private fun eachType(type: Types, types: ArrayList<String>, noDamageTo: ArrayList<String>,
        noDamageFrom: ArrayList<String>, strongAgainst: ArrayList<String>, weakAgainst: ArrayList<String>){
        val response : ResponseEntity<TypeResponse> = restTemplate.exchange(type.type.url, HttpMethod.GET, null)
        types.add(type.type.name)
        response.body?.damage_relations?.double_damage_from?.forEach { weakAgainst.add(it.name) }
        response.body?.damage_relations?.double_damage_to?.forEach { strongAgainst.add(it.name) }
        response.body?.damage_relations?.no_damage_from?.forEach { noDamageFrom.add(it.name) }
        response.body?.damage_relations?.no_damage_to?.forEach { noDamageTo.add(it.name) }
    }

    private fun setSpecie(url: String, pokemon: PokeLocal) {
        val response : ResponseEntity<SpecieResponse> = restTemplate.exchange(url, HttpMethod.GET, null)
        pokemon.evolvesFrom = response.body?.evolves_from_species?.name
        pokemon.flavor = getFlavorString(response)
        response.body?.evolution_chain?.url?.let { setEvolution(it, pokemon) }
    }

    private fun getFlavorString(response: ResponseEntity<SpecieResponse>): String{
        val flavor = ArrayList<String>()
        response.body?.flavor_text_entries?.forEach { if (it.language.name == "es") flavor.add(it.flavor_text) }
        return flavor.joinToString(";")
    }

    private fun setEvolution(url: String, pokemon: PokeLocal) {
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