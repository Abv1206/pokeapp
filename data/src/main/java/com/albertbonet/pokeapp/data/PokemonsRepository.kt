package com.albertbonet.pokeapp.data

import com.albertbonet.pokeapp.data.datasource.IPokemonBluetoothDataSource
import com.albertbonet.pokeapp.data.datasource.PokemonLocalDataSource
import com.albertbonet.pokeapp.data.datasource.PokemonRemoteDataSource
import com.albertbonet.pokeapp.domain.Pokemon
import com.albertbonet.pokeapp.domain.Error
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PokemonsRepository @Inject constructor(
    private val localDataSource: PokemonLocalDataSource,
    private val remoteDataSource: PokemonRemoteDataSource,
    private val bluetoothDataSource: IPokemonBluetoothDataSource
) {

    val pokemons = localDataSource.pokemons

    suspend fun requestPokemons(): Error? {
        if (localDataSource.isEmpty()) {
            val pokemons = remoteDataSource.findPokemons(0)
            pokemons.fold(ifLeft = { return it }) {
                localDataSource.save(it)
            }
        }
        return null
    }

    suspend fun requestPokemon(name: String): Error? {
        if (!localDataSource.exists(name)) {
            val pokemon = remoteDataSource.requestPokemon(name)
            pokemon.fold(ifLeft = { return it }) {
                localDataSource.save(it)
            }
        }
        return null
    }

    fun getPokemon(name: String): Flow<Pokemon> = localDataSource.findByName(name)

    suspend fun sendBluetoothPokemon(pokemon: Pokemon): Error? = bluetoothDataSource.sendPokemon(pokemon)

    suspend fun requestBluetoothPokemon(): Pokemon? {
        val pokemon = bluetoothDataSource.startBluetooth()
        pokemon?.let {
            if (!localDataSource.exists(pokemon.name)) {
                localDataSource.save(pokemon)
            }
        }
        return pokemon
    }

    suspend fun requestBluetoothConnection(mac: String): Error? = bluetoothDataSource.connectDevice(mac)

}

