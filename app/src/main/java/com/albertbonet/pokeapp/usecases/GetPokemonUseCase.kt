package com.albertbonet.pokeapp.usecases

import com.albertbonet.pokeapp.data.PokemonsRepository

class GetPokemonUseCase(private val pokemonsRepository: PokemonsRepository) {

    operator fun invoke(name: String) = pokemonsRepository.getPokemon(name)
}