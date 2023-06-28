package com.albertbonet.pokeapp.data.server

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RemoteService {

    @GET("pokemon")
    suspend fun listPokemons(@Query("limit") limit: Int, @Query("offset") offset: Int): RemoteResultList

    @GET("pokemon/{pokemonName}")
    suspend fun pokemonDetail(@Path(value = "pokemonName", encoded = true) pokemonName: String): PokemonResult
}