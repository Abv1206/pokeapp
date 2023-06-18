package com.albertbonet.pokeapp.domain

import com.albertbonet.pokeapp.model.PokemonsRepository

class GetPokemonUseCase(private val pokemonsRepository: PokemonsRepository) {

    operator fun invoke(name: String) = pokemonsRepository.getPokemon(name)
}