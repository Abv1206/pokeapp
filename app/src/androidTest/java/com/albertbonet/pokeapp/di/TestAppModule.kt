package com.albertbonet.pokeapp.di

import android.app.Application
import androidx.room.Room
import com.albertbonet.pokeapp.appTestShared.FakeRemoteService
import com.albertbonet.pokeapp.appTestShared.buildRemotePokemons
import com.albertbonet.pokeapp.data.bluetooth.PokemonBluetoothDataSource
import com.albertbonet.pokeapp.data.database.PokemonDatabase
import com.albertbonet.pokeapp.data.datasource.IPokemonBluetoothDataSource
import com.albertbonet.pokeapp.data.server.RemoteService
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [AppModule::class])
object TestAppModule {

    @Provides
    @Singleton
    @Limit
    fun provideLimit(): Int = 1500

    @Provides
    @Singleton
    @Offset
    fun provideOffset(): Int = 0

    @Provides
    @Singleton
    fun provideDatabase(app: Application) = Room.inMemoryDatabaseBuilder(
        app,
        PokemonDatabase::class.java
    ).build()

    @Provides
    @Singleton
    fun providePokemonDao(db: PokemonDatabase) = db.pokemonDao()

    @Provides
    @Singleton
    fun provideRemoteService(): RemoteService = FakeRemoteService(buildRemotePokemons(1, 2, 3, 4))

    @Provides
    @Singleton
    fun provideBluetoothService(app: Application): IPokemonBluetoothDataSource {
        return PokemonBluetoothDataSource(app)
    }

}
