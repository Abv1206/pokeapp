package com.albertbonet.pokeapp.framework.datasource

import com.albertbonet.pokeapp.framework.database.PokemonDao
import com.albertbonet.pokeapp.data.datasource.PokemonLocalDataSource
import com.albertbonet.pokeapp.domain.Pokemon
import com.albertbonet.pokeapp.domain.Pokemons
import com.albertbonet.pokeapp.framework.database.Pokemon as DbPokemon
import com.albertbonet.pokeapp.framework.database.Pokemons as DbPokemons
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PokemonRoomDataSource(private val pokemonDao: PokemonDao) : PokemonLocalDataSource {

    override val pokemons: Flow<List<Pokemons>> = pokemonDao.getAll().map { it.toDomainModel() }

    override fun findById(id: Int): Flow<Pokemon> = pokemonDao.findById(id).map { it.toDomainModel() }
    override fun findByName(name: String): Flow<Pokemon> = pokemonDao.findByName(name).map { it.toDomainModel() }

    override suspend fun isEmpty(): Boolean = pokemonDao.pokemonCount() == 0
    override suspend fun save(pokemons: List<DbPokemons>) {
        pokemonDao.insertPokemons(pokemons)
    }

    override suspend fun save(pokemon: DbPokemon) {
        pokemonDao.insertPokemon(pokemon)
    }
}

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
