package com.albertbonet.pokeapp.di

import android.app.Application
import android.bluetooth.BluetoothSocket
import android.os.Handler
import androidx.room.Room
import com.albertbonet.pokeapp.data.bluetooth.BluetoothService
import com.albertbonet.pokeapp.data.bluetooth.PokemonBluetoothDataSource
import com.albertbonet.pokeapp.data.database.PokemonDatabase
import com.albertbonet.pokeapp.data.database.PokemonRoomDataSource
import com.albertbonet.pokeapp.data.datasource.IPokemonBluetoothDataSource
import com.albertbonet.pokeapp.data.datasource.PokemonLocalDataSource
import com.albertbonet.pokeapp.data.datasource.PokemonRemoteDataSource
import com.albertbonet.pokeapp.data.server.PokemonServerDataSource
import com.albertbonet.pokeapp.data.server.RemoteService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.io.InputStream
import java.io.OutputStream
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

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
    fun provideDatabase(app: Application) = Room.databaseBuilder(
        app,
        PokemonDatabase::class.java,
        "pokemon-db"
    ).build()

    @Provides
    @Singleton
    fun providePokemonDao(db: PokemonDatabase) = db.pokemonDao()

    @Provides
    @Singleton
    fun provideRemoteService(): RemoteService {
        val okHttpClient = HttpLoggingInterceptor().run {
            level = HttpLoggingInterceptor.Level.BODY
            OkHttpClient.Builder()
                .connectTimeout(8, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(this).build()
        }

        return Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()

    }

    @Provides
    @Singleton
    fun provideBluetoothService(app: Application): IPokemonBluetoothDataSource {
        return PokemonBluetoothDataSource(app)
    }

}

@Module
@InstallIn(SingletonComponent::class)
abstract class AppDataModule {

    @Binds
    abstract fun bindLocalDataSource(localDataSource: PokemonRoomDataSource): PokemonLocalDataSource

    @Binds
    abstract fun bindRemoteDataSource(remoteDataSource: PokemonServerDataSource): PokemonRemoteDataSource
}