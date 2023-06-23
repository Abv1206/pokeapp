package com.albertbonet.pokeapp.data.database

import com.albertbonet.pokeapp.data.datasource.PokemonLocalDataSource
import com.albertbonet.pokeapp.data.tryCall
import com.albertbonet.pokeapp.domain.Error
import com.albertbonet.pokeapp.domain.Pokemon
import com.albertbonet.pokeapp.domain.Pokemons
import com.albertbonet.pokeapp.data.database.Pokemon as DbPokemon
import com.albertbonet.pokeapp.data.database.Pokemons as DbPokemons
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PokemonRoomDataSource(private val pokemonDao: PokemonDao) : PokemonLocalDataSource {

    override val pokemons: Flow<List<Pokemons>> = pokemonDao.getAll().map { it.toDomainModel() }

    override fun findById(id: Int): Flow<Pokemon> = pokemonDao.findById(id).map { it.toDomainModel() }
    override fun findByName(name: String): Flow<Pokemon> = pokemonDao.findByName(name).map { it.toDomainModel() }
    override suspend fun exists(name: String): Boolean = pokemonDao.exists(name)

    override suspend fun isEmpty(): Boolean = pokemonDao.pokemonCount() == 0
    override suspend fun save(pokemons: List<Pokemons>): Error? = tryCall {
        pokemonDao.insertPokemons(pokemons.fromDomainModel())
    }.fold(
        ifLeft = { it },
        ifRight = { null }
    )

    override suspend fun save(pokemon: Pokemon): Error? = tryCall {
        pokemonDao.insertPokemon(pokemon.fromDomainModel())
    }.fold(
        ifLeft = { it },
        ifRight = { null }
    )
}

/************ TO DOMAIN MODEL ************/

private fun List<DbPokemons>.toDomainModel(): List<Pokemons> = map { it.toDomainModel() }

private fun DbPokemons.toDomainModel(): Pokemons = Pokemons(
    id, name, url, officialUrlImage
)

private fun DbPokemon.toDomainModel(): Pokemon = Pokemon (
    id, name,  weight, height, baseExperience, types.toDomainModelTypeList(), summary, stats.toDomainModelStatList(), sprites.toDomainModel()
)

private fun List<DbPokemon.Type>.toDomainModelTypeList(): List<Pokemon.Type> = map { it.toDomainModel() }

private fun DbPokemon.Type.toDomainModel(): Pokemon.Type = Pokemon.Type (
    slot, type.toDomainModel()
)

private fun DbPokemon.SingularType.toDomainModel(): Pokemon.SingularType = Pokemon.SingularType (
    name
)

private fun List<DbPokemon.Stat>.toDomainModelStatList(): List<Pokemon.Stat> = map { it.toDomainModel() }

private fun DbPokemon.Stat.toDomainModel(): Pokemon.Stat = Pokemon.Stat(
    stat.toDomainModel(), effort, baseStat
)

private fun DbPokemon.SingularStat.toDomainModel(): Pokemon.SingularStat = Pokemon.SingularStat(
    name, url
)

private fun DbPokemon.Sprite.toDomainModel(): Pokemon.Sprite = Pokemon.Sprite(
    frontDefaultUrl
)

/************ FROM DOMAIN MODEL ************/

private fun Pokemon.fromDomainModel(): DbPokemon {
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

private fun MutableList<DbPokemon.Type>.castFromRemoteType(types: List<Pokemon.Type>): MutableList<DbPokemon.Type> {
    for (item in types) {
        val singularType = DbPokemon.SingularType(item.type.name)
        add(DbPokemon.Type(item.slot, singularType))
    }
    return this
}

private fun MutableList<DbPokemon.Stat>.castFromRemoteStat(stats: List<Pokemon.Stat>): MutableList<DbPokemon.Stat> {
    for (item in stats) {
        val singularStat = DbPokemon.SingularStat(item.stat.name, item.stat.url)
        add(DbPokemon.Stat(singularStat, item.effort, item.baseStat))
    }
    return this
}

private fun List<Pokemons>.fromDomainModel(): List<DbPokemons> = map { it.fromDomainModel() }

private fun Pokemons.fromDomainModel(): DbPokemons = DbPokemons(
    id,
    name,
    url,
    officialUrlImage
)