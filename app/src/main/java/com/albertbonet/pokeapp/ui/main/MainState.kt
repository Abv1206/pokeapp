package com.albertbonet.pokeapp.ui.main

import com.albertbonet.pokeapp.ui.common.PermissionRequester
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainState(
    private val scope: CoroutineScope,
    private val bluetoothPermissionRequester: PermissionRequester
) {

    fun requestBluetoothPermission(afterRequest: (Boolean) -> Unit) {
        scope.launch {
            afterRequest(bluetoothPermissionRequester.request())
        }
    }

}