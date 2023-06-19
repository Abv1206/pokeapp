package com.albertbonet.pokeapp.data

import com.albertbonet.pokeapp.data.datasource.PokemonLocalDataSource
import com.albertbonet.pokeapp.data.datasource.PokemonRemoteDataSource
import com.albertbonet.pokeapp.domain.Error
import com.albertbonet.pokeapp.domain.Pokemon
import com.albertbonet.pokeapp.domain.tryCall
import com.albertbonet.pokeapp.framework.database.Pokemons
import com.albertbonet.pokeapp.framework.server.PokemonResult
import com.albertbonet.pokeapp.framework.server.PokemonsResult
import com.albertbonet.pokeapp.framework.server.RemoteConnection
import com.albertbonet.pokeapp.ui.common.getPokemonImageById
import kotlinx.coroutines.flow.Flow
import com.albertbonet.pokeapp.framework.database.Pokemon as DbPokemon

class PokemonsRepository(private val localDataSource: PokemonLocalDataSource,
                         private val remoteDataSource: PokemonRemoteDataSource) {

    companion object {
        val limit = 1500
        val offset = 0
    }

    val pokemons = localDataSource.pokemons

    suspend fun requestPokemons(): Error? = tryCall {
        if (localDataSource.isEmpty()) {
            val pokemons = remoteDataSource.findPokemons(0)
            localDataSource.save(pokemons.results.map {it.toLocalModel()})
        }
    }

    suspend fun requestPokemon(name: String): Error? = tryCall {
        val pokemon = RemoteConnection.service.pokemonDetail(name)
        localDataSource.save(pokemon.toLocalModel())
    }

    fun getPokemon(name: String): Flow<Pokemon> = localDataSource.findByName(name)

}

private fun PokemonsResult.toLocalModel(): Pokemons = Pokemons(
    id = getId(),
    name = name,
    url = url,
    officialUrlImage = getPokemonImageById(getId().toString())
)

private fun PokemonResult.toLocalModel(): DbPokemon {
    val typesModel = mutableListOf<DbPokemon.Type>().castFromRemoteType(types)
    val statsModel = mutableListOf<DbPokemon.Stat>().castFromRemoteStat(stats)
    val spritesModel = DbPokemon.Sprite(sprites.frontDefaultUrl)

    return DbPokemon(
        id = id,
        name = name,
        weight = weight,
        height = height,
        baseExperience = baseExperience,
        types = typesModel,
        stats = statsModel,
        sprites = spritesModel,
        summary = ""
    )
}

private fun MutableList<DbPokemon.Type>.castFromRemoteType(types: List<PokemonResult.TypeResult>): MutableList<DbPokemon.Type> {
    for (item in types) {
        val singularType = DbPokemon.SingularType(item.type.name)
        add(DbPokemon.Type(item.slot, singularType))
    }
    return this
}

private fun MutableList<DbPokemon.Stat>.castFromRemoteStat(stats: List<PokemonResult.StatResult>): MutableList<DbPokemon.Stat> {
    for (item in stats) {
        val singularStat = DbPokemon.SingularStat(item.stat.name, item.stat.url)
        add(DbPokemon.Stat(singularStat, item.effort, item.baseStat))
    }
    return this
}