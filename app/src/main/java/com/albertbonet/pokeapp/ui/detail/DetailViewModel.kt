package com.albertbonet.pokeapp.ui.detail

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albertbonet.pokeapp.data.toError
import com.albertbonet.pokeapp.di.PokemonName
import com.albertbonet.pokeapp.domain.Error
import com.albertbonet.pokeapp.domain.Pokemon
import com.albertbonet.pokeapp.usecases.GetPokemonUseCase
import com.albertbonet.pokeapp.usecases.RequestBluetoothConnectionUseCase
import com.albertbonet.pokeapp.usecases.SendPokemonBluetoothUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    @PokemonName private val pokemonName: String,
    private val getPokemonUseCase: GetPokemonUseCase,
    private val sendPokemonBluetoothUseCase: SendPokemonBluetoothUseCase,
    private val requestBluetoothConnectionUseCase: RequestBluetoothConnectionUseCase
): ViewModel() {

    var btLiveData: MutableLiveData<List<BluetoothDevice>> = MutableLiveData()

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    fun onUiReady() {
        viewModelScope.launch {
            getPokemonUseCase(pokemonName)
                .catch { cause -> _state.update { it.copy(error = cause.toError()) } }
                .collect { pokemon -> _state.value = UiState(pokemon = pokemon) }
        }
    }

    fun onRequestBluetoothConnection(macAddress: String) {
        viewModelScope.launch {
            state.value.pokemon?.let { pokemon ->
                val connectionError = requestBluetoothConnectionUseCase(macAddress)
                _state.update { it.copy(error = connectionError) }
                if (connectionError == null) {
                    val error = sendPokemonBluetoothUseCase(pokemon)
                    _state.update { it.copy(error = error) }
                }
            }
        }
    }

    fun onSendPokemon() {
        viewModelScope.launch {
            state.value.pokemon?.let { pokemon ->
                val error = sendPokemonBluetoothUseCase(pokemon)
                _state.update { it.copy(error = error) }
            }
        }
    }

    data class UiState(
        val pokemon: Pokemon? = null,
        val error: Error? = null
    )
}
