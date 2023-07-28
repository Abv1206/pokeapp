package com.albertbonet.pokeapp.appTestShared

import com.albertbonet.pokeapp.data.PokemonsRepository
import com.albertbonet.pokeapp.data.database.Pokemon
import com.albertbonet.pokeapp.data.database.PokemonRoomDataSource
import com.albertbonet.pokeapp.data.server.PokemonServerDataSource
import com.albertbonet.pokeapp.data.server.PokemonsResult
import com.albertbonet.pokeapp.data.server.RemoteResultList
import com.albertbonet.pokeapp.ui.common.getPokemonImageById
import com.albertbonet.pokeapp.data.database.Pokemon as DatabasePokemon
import com.albertbonet.pokeapp.data.database.Pokemons as DatabasePokemons


fun buildRepositoryWith(
    localDataList: List<DatabasePokemons>,
    localData: DatabasePokemon,
    remoteData: RemoteResultList
): PokemonsRepository {
    val bluetoothDataSource = FakeBluetoothDataSource()
    val localDataSource = PokemonRoomDataSource(FakePokemonDao(localDataList, localData))
    val remoteDataSource = PokemonServerDataSource(1500, 0, FakeRemoteService(remoteData))
    return PokemonsRepository(localDataSource, remoteDataSource, bluetoothDataSource)
}

fun buildDatabasePokemons(vararg id: Int) = id.map {
    DatabasePokemons(
        id = it,
        name = "Name $it",
        "",
        ""
    )
}

fun buildDatabasePokemon(vararg id: Int) = id.map {
    DatabasePokemon(
        id = it,
        name = "Name $it",
        weight = 100,
        height = 20,
        124,
        listOf(),
        "",
        listOf(),
        Pokemon.Sprite("")
    )
}

fun buildRemotePokemons(vararg id: Int) = RemoteResultList(
        count = id.size,
        page = 0,
        id.map { PokemonsResult("Pokemon $it", ""/*getPokemonImageById(it.toString())*/) }
    )