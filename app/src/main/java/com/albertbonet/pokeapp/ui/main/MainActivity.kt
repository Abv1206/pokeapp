package com.albertbonet.pokeapp.ui.main

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.albertbonet.pokeapp.databinding.ActivityMainBinding
import com.albertbonet.pokeapp.usecases.GetPokemonsListUseCase
import com.albertbonet.pokeapp.usecases.RequestPokemonUseCase
import com.albertbonet.pokeapp.usecases.RequestPokemonsListUseCase
import com.albertbonet.pokeapp.data.PokemonsRepository
import com.albertbonet.pokeapp.data.datasource.PokemonLocalDataSource
import com.albertbonet.pokeapp.data.datasource.PokemonRemoteDataSource
import com.albertbonet.pokeapp.framework.datasource.PokemonRoomDataSource
import com.albertbonet.pokeapp.framework.datasource.PokemonServerDataSource
import com.albertbonet.pokeapp.ui.common.PermissionRequester
import com.albertbonet.pokeapp.ui.common.app
import com.albertbonet.pokeapp.ui.common.launchAndCollect
import com.albertbonet.pokeapp.ui.detail.DetailActivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map


class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels {
        val localDataSource = PokemonRoomDataSource(app.db.pokemonDao())
        val remoteDataSource = PokemonServerDataSource(PokemonsRepository.limit, PokemonsRepository.offset)
        val repository = PokemonsRepository(localDataSource, remoteDataSource)
        MainViewModelFactory(
            RequestPokemonsListUseCase(repository),
            RequestPokemonUseCase(repository),
            GetPokemonsListUseCase(repository)
        )
    }
    private val adapter = PokemonsAdapter { viewModel.onPokemonClicked(it.name) }
    private lateinit var binding: ActivityMainBinding

    private lateinit var mainState: MainState
    private val permissionRequester = PermissionRequester(
        this,
        Manifest.permission.BLUETOOTH
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recycler.adapter = adapter
        mainState = MainState(this, lifecycleScope, permissionRequester)

        with(viewModel.state) {
            diff({it.pokemons}, adapter::submitList)
            diff({it.loading}) { mainState.loadingStatus(binding.progress, it) }
            diff({it.error}) { it?.let { mainState.showError(it) } }
        }
        launchAndCollect(viewModel.events) { event ->
            when (event) {
                is MainViewModel.UiEvent.NavigateTo -> navigateTo(event.pokemonName)
            }
        }

        mainState.requestBluetoothPermission {
            viewModel.onUiReady()
            mainState.preLoadBackgroundImages()
        }
    }

    private fun navigateTo(pokemonName: String) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.POKEMON, pokemonName)
        startActivity(intent)
    }

    private fun <T, U> Flow<T>.diff(mapf: (T) -> U, body: (U) -> Unit) {
        launchAndCollect(
            flow = map(mapf).distinctUntilChanged(),
            body = body
        )
    }
}




