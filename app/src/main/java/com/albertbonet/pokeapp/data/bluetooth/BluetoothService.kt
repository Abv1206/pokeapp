package com.albertbonet.pokeapp.data.bluetooth

import android.bluetooth.BluetoothSocket

interface BluetoothService {

    class ConnectedThread(private val mmSocket: BluetoothSocket) : Thread()

}