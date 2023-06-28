package com.albertbonet.pokeapp.data.server

import arrow.core.Either
import com.albertbonet.pokeapp.data.datasource.PokemonRemoteDataSource
import com.albertbonet.pokeapp.data.tryCall
import com.albertbonet.pokeapp.di.Limit
import com.albertbonet.pokeapp.di.Offset
import com.albertbonet.pokeapp.domain.Error
import com.albertbonet.pokeapp.domain.Pokemon
import com.albertbonet.pokeapp.domain.Pokemons
import com.albertbonet.pokeapp.ui.common.getPokemonImageById
import javax.inject.Inject

class PokemonServerDataSource @Inject constructor(
    @Limit private val limit: Int,
    @Offset private val offset: Int,
    private val remoteService: RemoteService) :
    PokemonRemoteDataSource {

    override suspend fun findPokemons(page: Int): Either<Error, List<Pokemons>> = tryCall {
        remoteService
            .listPokemons(
                limit,
                (offset * page)
            )
            .results
            .toDomainModel()
    }

    override suspend fun requestPokemon(name: String): Either<Error, Pokemon> = tryCall {
        remoteService
            .pokemonDetail(name)
            .toDomainModel()
    }
}

private fun List<PokemonsResult>.toDomainModel(): List<Pokemons> = map { it .toDomainModel() }

private fun PokemonsResult.toDomainModel(): Pokemons = Pokemons(
    id = getId(),
    name = name,
    url = url,
    officialUrlImage = getPokemonImageById(getId().toString())
)

private fun PokemonResult.toDomainModel(): Pokemon {
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