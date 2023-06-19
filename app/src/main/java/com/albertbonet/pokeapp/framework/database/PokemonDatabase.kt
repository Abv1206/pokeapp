package com.albertbonet.pokeapp.framework.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Pokemon::class, Pokemons::class], version = 1, exportSchema = false)
abstract class PokemonDatabase: RoomDatabase() {

    abstract fun pokemonDao(): PokemonDao
}