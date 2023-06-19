package com.albertbonet.pokeapp.usecases

import com.albertbonet.pokeapp.data.Error
import com.albertbonet.pokeapp.data.PokemonsRepository

class RequestPokemonUseCase(private val pokemonsRepository: PokemonsRepository) {

    suspend operator fun invoke(name: String): Error? = pokemonsRepository.requestPokemon(name)
}