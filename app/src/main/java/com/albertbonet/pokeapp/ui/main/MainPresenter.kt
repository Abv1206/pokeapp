package com.albertbonet.pokeapp.ui.main

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainPresenter (
    //private val pokemonsRepository: PokemonsRepository,
    private val scope: CoroutineScope
        ){

    interface View {
        fun showProgress()
        fun hideProgress()
        //TODO Add pokesRepository
        fun updateData()
        //TODO Add pokemon
        fun navigateTo()
    }

    private var view: View? = null

    fun onCreate (view: View) {
        this.view = view

        scope.launch {
            view.showProgress()
            view.updateData()
            view.hideProgress()
        }
    }

    fun onMovieClicked() {
        view?.navigateTo()
    }

    fun onDestroy() {
        this.view = null
    }
}