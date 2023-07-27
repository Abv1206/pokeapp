package com.albertbonet.pokeapp.data.datasource

import arrow.core.Either
import com.albertbonet.pokeapp.domain.Error
import com.albertbonet.pokeapp.domain.Pokemon

interface IPokemonBluetoothDataSource {

    suspend fun startBluetooth(): Pokemon?
    fun stopBluetooth()
    suspend fun connectDevice(mac: String): Error?
    suspend fun sendPokemon(pokemon: Pokemon): Error?
}