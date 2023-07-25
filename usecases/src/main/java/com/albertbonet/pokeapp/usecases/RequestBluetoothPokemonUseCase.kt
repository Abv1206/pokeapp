package com.albertbonet.pokeapp.usecases

import com.albertbonet.pokeapp.data.PokemonsRepository
import com.albertbonet.pokeapp.domain.Pokemon
import javax.inject.Inject

class RequestBluetoothPokemonUseCase @Inject constructor(private val pokemonsRepository: PokemonsRepository) {

    suspend operator fun invoke(): Pokemon? = pokemonsRepository.requestBluetoothPokemon()
}