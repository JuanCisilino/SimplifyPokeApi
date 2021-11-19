package com.simplify.pokeAPI.controller

import com.simplify.pokeAPI.requests.PokemonRequest
import com.simplify.pokeAPI.service.ServiceImpl
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/pokemon", produces = [MediaType.APPLICATION_JSON_VALUE])
class Controller {

    private val service = ServiceImpl()

    @GetMapping("/{pokemon}")
    @ResponseBody
    suspend fun getByName(@PathVariable pokemon: String) =
        ResponseEntity(service.getByName(pokemon), HttpStatus.OK)

    @GetMapping("/")
    suspend fun paginatedList(@RequestParam pageNo: Int, @RequestParam pageSize: Int) =
        service.getPaginated(pageNo, pageSize)

    @PostMapping("/")
    suspend fun updateList() = service.updateList()

    @PatchMapping("")
    suspend fun updateNickNamePokemon(@RequestBody @Valid pokemonRequest: PokemonRequest) =
        service.updateNickNamePokemon(pokemonRequest)

    @PatchMapping("/{name}")
    suspend fun updateFavoritePokemon(@PathVariable name: String) =
        service.updateFavoritePokemon(name)
}