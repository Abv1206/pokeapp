package com.albertbonet.pokeapp

import android.app.Application
import androidx.room.Room
import com.albertbonet.pokeapp.model.database.Converters
import com.albertbonet.pokeapp.model.database.PokemonDatabase

class App: Application() {

    lateinit var db: PokemonDatabase
        private set //Setter is declared as private, so external calls can obtain info but not modify

    override fun onCreate() {
        super.onCreate()

        db = Room.databaseBuilder(
            this,
            PokemonDatabase::class.java, "pokemon-db"
        ).build()
    }
}