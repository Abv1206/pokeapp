package com.albertbonet.pokeapp.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Query("Select * FROM Pokemons")
    fun getAll(): Flow<List<Pokemons>>

    @Query("Select * FROM Pokemon WHERE id = :id")
    fun findById(id: Int): Flow<Pokemon>

    @Query("Select * FROM Pokemon WHERE name = :name")
    fun findByName(name: String): Flow<Pokemon>

    @Query("SELECT COUNT(id) FROM Pokemons")
    suspend fun pokemonCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemons(pokemons: List<Pokemons>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemon: Pokemon)

    @Update
    suspend fun updatePokemon(pokemon: Pokemon)
}