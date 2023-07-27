package com.albertbonet.pokeapp.usecases

import com.albertbonet.pokeapp.data.PokemonsRepository
import com.albertbonet.pokeapp.domain.Error
import javax.inject.Inject

class RequestBluetoothConnectionUseCase @Inject constructor(private val pokemonsRepository: PokemonsRepository) {

    suspend operator fun invoke(mac: String): Error? = pokemonsRepository.requestBluetoothConnection(mac)
}