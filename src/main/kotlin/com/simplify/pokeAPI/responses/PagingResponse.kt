package com.simplify.pokeAPI.responses

data class PagingResponse<T>(
    val total: Long,
    val items: List<T>
)