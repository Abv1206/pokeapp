package com.albertbonet.pokeapp.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Query("Select * FROM Pokemon")
    fun getAll(): Flow<List<Pokemon>>

    @Query("Select * FROM Pokemon WHERE id = :id")
    fun findById(id: Int): Flow<Pokemon>

    @Query("SELECT COUNT(id) FROM Pokemon")
    fun pokemonCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPokemons(pokemons: List<Pokemon>)

    @Update
    fun updatePokemon(pokemon: Pokemon)
}