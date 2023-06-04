package com.albertbonet.pokeapp.ui.detail

import android.content.Context
import com.albertbonet.pokeapp.model.Error
import com.albertbonet.pokeapp.ui.common.PermissionRequester
import com.albertbonet.pokeapp.ui.common.showDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DetailState(
    private val context: Context
) {


    fun showError(error: Error) = (when (error) {
        Error.Connectivity -> "Connection error"
        is Error.NullPointer -> "Null pointer error"
        is Error.Server -> "Server error"
        is Error.SocketTimeout -> "Connection timeout error"
        is Error.Unknown -> "Unknown error"
    }).apply {
        showDialog(context, this)
    }
}