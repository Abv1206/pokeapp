package com.albertbonet.pokeapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.albertbonet.pokeapp.model.Pokemon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DetailViewModel (val pokemon: Pokemon): ViewModel() {

    private val _state = MutableStateFlow(UiState(pokemon))
    val state: StateFlow<UiState> = _state.asStateFlow()

    class UiState(val pokemon: Pokemon)
}

@Suppress("UNCHECKED_CAST")
class DetailViewModelFactory(private val pokemon: Pokemon): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailViewModel(pokemon) as T
    }
}
