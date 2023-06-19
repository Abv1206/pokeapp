package com.albertbonet.pokeapp.ui.main

import android.animation.Animator
import android.content.Context
import android.view.View
import com.albertbonet.pokeapp.R
import com.albertbonet.pokeapp.domain.Error
import com.albertbonet.pokeapp.ui.common.PermissionRequester
import com.albertbonet.pokeapp.ui.common.rotateAnimation
import com.albertbonet.pokeapp.ui.common.showDialog
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainState(
    private val context: Context,
    private val scope: CoroutineScope,
    private val bluetoothPermissionRequester: PermissionRequester
) {

    private var hasAnimator = false
    private var animator: Animator? = null

    fun requestBluetoothPermission(afterRequest: (Boolean) -> Unit) {
        scope.launch {
            afterRequest(bluetoothPermissionRequester.request())
        }
    }

    fun showError(error: Error) = (when (error) {
        Error.Connectivity -> "Connection error"
        is Error.NullPointer -> "Null pointer error"
        is Error.Server -> "Server error"
        is Error.SocketTimeout -> "Connection timeout error"
        is Error.Unknown -> "Unknown error"
    }).apply {
        showDialog(context, this)
    }

    fun loadingStatus(view: View, status: Boolean) {
        view.visibility = if(status) {
            if (!hasAnimator) animator = view.rotateAnimation()
            animator?.start()
            View.VISIBLE
        } else {
            animator?.let {
                it.removeAllListeners()
                it.end()
                it.cancel()
            }
            View.GONE
        }
    }

    fun preLoadBackgroundImages() {
        Glide.with(context)
            .load(
                listOf(
                    R.drawable.pokemon_fire_route,
                    R.drawable.pokemon_simple_route,
                    R.drawable.pokemon_fly_route,
                    R.drawable.pokemon_grass_route,
                    R.drawable.pokemon_ice_route,
                    R.drawable.pokemon_water_route
                )
            )
            .preload()
    }
}