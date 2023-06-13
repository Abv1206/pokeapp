package com.albertbonet.pokeapp.model

import com.albertbonet.pokeapp.App
import com.albertbonet.pokeapp.model.database.Pokemon
import com.albertbonet.pokeapp.model.database.Pokemons
import com.albertbonet.pokeapp.model.datasource.PokemonLocalDataSource
import com.albertbonet.pokeapp.model.datasource.PokemonRemoteDataSource
import com.albertbonet.pokeapp.ui.common.getPokemonImageById
import kotlinx.coroutines.flow.Flow

class PokemonsRepository(application: App) {

    val limit = 1500
    val offset = 0

    private val localDataSource = PokemonLocalDataSource(application.db.pokemonDao())
    private val remoteDataSource = PokemonRemoteDataSource(limit, offset)
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

private fun PokemonResult.toLocalModel(): Pokemon {
    val typesModel = mutableListOf<Pokemon.Type>().castFromRemoteType(types)
    val statsModel = mutableListOf<Pokemon.Stat>().castFromRemoteStat(stats)
    val spritesModel = Pokemon.Sprite(sprites.frontDefaultUrl)

    return Pokemon(
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

private fun MutableList<Pokemon.Type>.castFromRemoteType(types: List<PokemonResult.TypeResult>): MutableList<Pokemon.Type> {
    for (item in types) {
        val singularType = Pokemon.SingularType(item.type.name)
        add(Pokemon.Type(item.slot, singularType))
    }
    return this
}

private fun MutableList<Pokemon.Stat>.castFromRemoteStat(stats: List<PokemonResult.StatResult>): MutableList<Pokemon.Stat> {
    for (item in stats) {
        val singularStat = Pokemon.SingularStat(item.stat.name, item.stat.url)
        add(Pokemon.Stat(singularStat, item.effort, item.baseStat))
    }
    return this
}