package com.albertbonet.pokeapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albertbonet.pokeapp.data.toError
import com.albertbonet.pokeapp.domain.Error
import com.albertbonet.pokeapp.domain.Pokemons
import com.albertbonet.pokeapp.usecases.GetPokemonsListUseCase
import com.albertbonet.pokeapp.usecases.RequestPokemonUseCase
import com.albertbonet.pokeapp.usecases.RequestPokemonsListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val requestPokemonsListUseCase: RequestPokemonsListUseCase,
    private val requestPokemonUseCase: RequestPokemonUseCase,
    private val getPokemonsListUseCase: GetPokemonsListUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    private val _events = Channel<UiEvent>()
    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            getPokemonsListUseCase()
                .catch { cause -> _state.update { it.copy(error = cause.toError()) } }
                .collect { pokemons -> _state.update { UiState(pokemons = pokemons) } }
        }
    }

    fun onUiReady() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }
            val error = requestPokemonsListUseCase()
            _state.update { it.copy(loading = false, error = error) }
        }
    }

    fun onPokemonClicked(pokemonName: String) {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }
            val error = requestPokemonUseCase(pokemonName)
            _state.update { it.copy(loading = false, error = error) }
            if (error == null) {
                _events.send(UiEvent.NavigateTo(pokemonName))
            }
        }
    }

    data class UiState(
        val loading: Boolean = false,
        val pokemons: List<Pokemons>? = null,
        val error: Error? = null
    )

    sealed interface UiEvent {
        data class NavigateTo(val pokemonName: String) : UiEvent
    }
}
