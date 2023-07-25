package com.albertbonet.pokeapp.ui.main

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.albertbonet.pokeapp.data.datasource.IPokemonBluetoothDataSource
import com.albertbonet.pokeapp.databinding.ActivityMainBinding
import com.albertbonet.pokeapp.ui.common.PermissionRequester
import com.albertbonet.pokeapp.ui.common.launchAndCollect
import com.albertbonet.pokeapp.ui.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var bluetoothPokemon: IPokemonBluetoothDataSource
    private val viewModel: MainViewModel by viewModels()
    private val adapter = PokemonsAdapter { viewModel.onPokemonClicked(it.name) }
    private lateinit var binding: ActivityMainBinding

    private lateinit var mainState: MainState
    private val permissionRequester = PermissionRequester(
        this,
        Manifest.permission.BLUETOOTH_CONNECT
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

        viewModel.onUiReady()
        mainState.preLoadBackgroundImages()

        binding.receive.setOnClickListener {
            mainState.requestBluetoothPermission {
                val requestCode = 1;
                val discoverableIntent: Intent =
                    Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
                        putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 60)
                    }
                startActivityForResult(discoverableIntent, requestCode)
            }
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

    override fun onDestroy() {
        super.onDestroy()
        bluetoothPokemon.stopBluetooth()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            viewModel.onBluetoothDiscovering()
        }
    }
}




