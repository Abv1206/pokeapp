package com.albertbonet.pokeapp.usecases

import com.albertbonet.pokeapp.domain.Error
import com.albertbonet.pokeapp.data.PokemonsRepository
import javax.inject.Inject

class RequestPokemonUseCase @Inject constructor(private val pokemonsRepository: PokemonsRepository) {

    suspend operator fun invoke(name: String): Error? = pokemonsRepository.requestPokemon(name)
}