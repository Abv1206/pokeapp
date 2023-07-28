package com.albertbonet.pokeapp.appTestShared

import arrow.core.right
import com.albertbonet.pokeapp.data.database.PokemonDao
import com.albertbonet.pokeapp.data.datasource.IPokemonBluetoothDataSource
import com.albertbonet.pokeapp.data.datasource.PokemonLocalDataSource
import com.albertbonet.pokeapp.data.datasource.PokemonRemoteDataSource
import com.albertbonet.pokeapp.data.server.PokemonResult
import com.albertbonet.pokeapp.data.server.PokemonsResult
import com.albertbonet.pokeapp.data.server.RemoteResultList
import com.albertbonet.pokeapp.data.server.RemoteService
import com.albertbonet.pokeapp.datashared.samplePokemon
import com.albertbonet.pokeapp.datashared.samplePokemons
import com.albertbonet.pokeapp.domain.Pokemon
import com.albertbonet.pokeapp.domain.Pokemons
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import com.albertbonet.pokeapp.domain.Error
import com.albertbonet.pokeapp.data.database.Pokemon as DatabasePokemon
import com.albertbonet.pokeapp.data.database.Pokemons as DatabasePokemons


val defaultFakePokemons = listOf(
    samplePokemons.copy(1),
    samplePokemons.copy(2),
    samplePokemons.copy(3)
)

val defaultFakePokemon = samplePokemon.copy(1)

class FakeLocalDataSource : PokemonLocalDataSource {

    val inMemoryPokemons = MutableStateFlow<List<Pokemons>>(emptyList())
    val inMemoryPokemon = MutableStateFlow<List<Pokemon>>(emptyList())

    override val pokemons = inMemoryPokemons

    private lateinit var findPokemonFlow: MutableStateFlow<Pokemon>

    override suspend fun isEmpty() = pokemons.value.isEmpty()

    override fun findByName(name: String): Flow<Pokemon> {
        findPokemonFlow = MutableStateFlow(inMemoryPokemon.value.first { it.name == name })
        return findPokemonFlow
    }

    override fun findById(id: Int): Flow<Pokemon> = flowOf(inMemoryPokemon.value.first { it.id == id })

    override suspend fun exists(name: String): Boolean = inMemoryPokemon.value.any { it.name == name }

    override suspend fun save(pokemons: List<Pokemons>): Error? {
        inMemoryPokemons.value = pokemons
        return null
    }

    override suspend fun save(pokemon: Pokemon): Error? {
        inMemoryPokemon.value = listOf(pokemon)

        if(::findPokemonFlow.isInitialized) {
            listOf(pokemon).firstOrNull { it.name == findPokemonFlow.value.name }
                ?.let { findPokemonFlow.value = it }
        }
        return null
    }
}


class FakePokemonDao(pokemons: List<DatabasePokemons> = emptyList(), pokemon: DatabasePokemon) : PokemonDao {

    val inMemoryPokemons = MutableStateFlow<List<DatabasePokemons>>(emptyList())
    val inMemoryPokemon = MutableStateFlow<List<DatabasePokemon>>(emptyList())

    val pokemons = inMemoryPokemons

    override fun getAll(): Flow<List<DatabasePokemons>> = pokemons

    override suspend fun pokemonCount(): Int {
        TODO("Not yet implemented")
    }

    override suspend fun updatePokemon(pokemon: DatabasePokemon) {
        TODO("Not yet implemented")
    }

    private lateinit var findPokemonFlow: MutableStateFlow<DatabasePokemon>

    override fun findByName(name: String): Flow<DatabasePokemon> {
        findPokemonFlow = MutableStateFlow(inMemoryPokemon.value.first { it.name == name })
        return findPokemonFlow
    }

    override fun findById(id: Int): Flow<DatabasePokemon> = flowOf(inMemoryPokemon.value.first { it.id == id })

    override suspend fun exists(name: String): Boolean = inMemoryPokemon.value.any { it.name == name }

    override suspend fun insertPokemons(pokemons: List<DatabasePokemons>) {
        inMemoryPokemons.value = pokemons
    }

    override suspend fun insertPokemon(pokemon: DatabasePokemon) {
        inMemoryPokemon.value = listOf(pokemon)

        if(::findPokemonFlow.isInitialized) {
            listOf(pokemon).firstOrNull { it.name == findPokemonFlow.value.name }
                ?.let { findPokemonFlow.value = it }
        }
    }
}


class FakeRemoteDataSource : PokemonRemoteDataSource {

    var pokemons = defaultFakePokemons
    var pokemon = defaultFakePokemon

    override suspend fun findPokemons(page: Int) = pokemons.right()

    override suspend fun requestPokemon(name: String) = pokemon.right()
}

class FakeBluetoothDataSource : IPokemonBluetoothDataSource {

    var pokemon = defaultFakePokemon

    override suspend fun startBluetooth(): Pokemon? = pokemon

    override fun stopBluetooth() { }

    override suspend fun connectDevice(mac: String): Error? = null

    override suspend fun sendPokemon(pokemon: Pokemon): Error? = null
}

class FakeRemoteService(private val pokemons: RemoteResultList) : RemoteService {

    override suspend fun listPokemons(limit: Int, offset: Int) = pokemons

    override suspend fun pokemonDetail(pokemonName: String) = PokemonResult(
        6,
        "Charizard",
        1500,
        120,
        210,
        listOf(),
        listOf(),
        PokemonResult.Sprite("")
    )
}

