package com.albertbonet.pokeapp.usecases

import com.albertbonet.pokeapp.data.PokemonsRepository
import javax.inject.Inject

class GetPokemonUseCase @Inject constructor(private val pokemonsRepository: PokemonsRepository) {

    operator fun invoke(name: String) = pokemonsRepository.getPokemon(name)
}