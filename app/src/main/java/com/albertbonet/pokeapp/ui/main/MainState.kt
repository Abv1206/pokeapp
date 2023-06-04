package com.albertbonet.pokeapp.ui.main

import android.app.Activity
import android.content.Context
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.albertbonet.pokeapp.R
import com.albertbonet.pokeapp.model.Error
import com.albertbonet.pokeapp.ui.common.PermissionRequester
import com.albertbonet.pokeapp.ui.common.showDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainState(
    private val context: Context,
    private val scope: CoroutineScope,
    private val bluetoothPermissionRequester: PermissionRequester
) {

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

}