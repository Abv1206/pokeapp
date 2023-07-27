package com.albertbonet.pokeapp.data.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.util.Log
import arrow.core.Either
import com.albertbonet.pokeapp.data.AndroidPermissionChecker
import com.albertbonet.pokeapp.data.datasource.IPokemonBluetoothDataSource
import com.albertbonet.pokeapp.data.toError
import com.albertbonet.pokeapp.data.tryCall
import com.albertbonet.pokeapp.domain.Error
import com.albertbonet.pokeapp.domain.Pokemon
import com.albertbonet.pokeapp.ui.common.pokedexPrefix
import com.albertbonet.pokeapp.ui.detail.BluetoothConnectionReceiver
import java.io.IOException
import java.io.InputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.OutputStream
import java.util.UUID
import javax.inject.Inject

class PokemonBluetoothDataSource @Inject constructor(
    private val application: Application
): IPokemonBluetoothDataSource {

    //Nexus 5 CC:FA:00:52:33:99
    private val TAG = "BluetoothManager"
    private val APP_NAME = "PokeApp"
    private val UUID_STRING = "00001101-0000-1000-8000-00805F9B34FB" // UUID para SPP (Serial Port Profile)

    private var error: Error? = null
    private var stillWaiting = true
    private var pokemonReceived: Pokemon? = null

    private val androidPermissionChecker = AndroidPermissionChecker(application)
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var acceptThread: AcceptThread? = null
    private var connectThread: ConnectThread? = null
    private var connectedThread: ConnectedThread? = null

    override suspend fun startBluetooth(): Pokemon? {
        error = null
        stillWaiting = true
        startAcceptThread()
        while (stillWaiting) {  }
        return pokemonReceived
    }

    override fun stopBluetooth() {
        stopThreads()
    }

    override suspend fun connectDevice(mac: String): Error? {
        error = null
        val device = bluetoothAdapter?.getRemoteDevice(mac)
        device?.let { startConnectThread(it) }
        return error
    }

    override suspend fun sendPokemon(pokemon: Pokemon): Error? = tryCall {
        connectedThread?.sendData(pokemon)
    }.fold(
        ifLeft = { it },
        ifRight = { null }
    )

    @SuppressLint("MissingPermission")
    private fun startAcceptThread() {
        if (!androidPermissionChecker.check(Manifest.permission.BLUETOOTH_SCAN)) {
            bluetoothAdapter?.let { if (!bluetoothAdapter.isEnabled) bluetoothAdapter.enable() }
            acceptThread = AcceptThread()
            acceptThread?.start()
        }
    }

    private fun startConnectThread(device: BluetoothDevice) {
        connectThread = ConnectThread(device)
        connectThread?.start()
    }

    private fun startConnectedThread(socket: BluetoothSocket) {
        connectedThread = ConnectedThread(socket)
        connectedThread?.start()
        application.sendBroadcast(Intent(BluetoothConnectionReceiver.CONNECTION_PAIRED))
    }

    private fun stopThreads() {
        stillWaiting = false
        acceptThread?.cancel()
        connectThread?.cancel()
        connectedThread?.cancel()
        error = null
    }

    @SuppressLint("MissingPermission")
    inner class AcceptThread : Thread() {
        private val serverSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE) {
            bluetoothAdapter?.listenUsingRfcommWithServiceRecord(APP_NAME, UUID.fromString(UUID_STRING))
        }

        override fun run() {
            //var socket: BluetoothSocket? = null

            var shouldLoop = true
            while (shouldLoop) {
                val socket: BluetoothSocket? = try {
                    serverSocket?.accept()
                } catch (e: IOException) {
                    error = e.toError()
                    Log.e(TAG, "Socket's accept() method failed", e)
                    shouldLoop = false
                    null
                }
                socket?.also {
                    startConnectedThread(it)
                    serverSocket?.close()
                    shouldLoop = false
                }
            }
        }

        fun cancel() {
            serverSocket?.close()
        }
    }

    @SuppressLint("MissingPermission")
    inner class ConnectThread(private val device: BluetoothDevice) : Thread() {
        private val socket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            device.createInsecureRfcommSocketToServiceRecord(UUID.fromString(UUID_STRING))
        }

        override fun run() {
            bluetoothAdapter?.cancelDiscovery()
            socket?.let {
                try {
                    it.connect()
                    startConnectedThread(it)
                } catch (e: IOException) {
                    error = e.toError()
                    Log.e(TAG, "Error connecting with the device: ${e.message}")
                    try {
                        socket?.close()
                    } catch (e: IOException) {
                        Log.e(TAG, "Error while closing connection socket: ${e.message}")
                    }
                }
            }
        }

        fun cancel() {
            socket?.close()
        }
    }

    inner class ConnectedThread(private val socket: BluetoothSocket) : Thread() {
        private val inputStream: InputStream = socket.inputStream
        private val outputStream: OutputStream = socket.outputStream
        private val objectOutputStream = ObjectOutputStream(outputStream)
        private val objectInputStream = ObjectInputStream(inputStream)

        override fun run() {
            var data: Any?

            while (true) {
                try {
                    data = objectInputStream.readObject()
                    data?.let {
                        if (it is Pokemon) {
                            Log.d(TAG, "Pokemon recibido: ${it.name}, ${pokedexPrefix(it.id)}")
                            pokemonReceived = data as Pokemon
                            stillWaiting = false
                            stopThreads()
                        }
                    }
                } catch (e: IOException) {
                    error = e.toError()
                    Log.e(TAG, "Error receiving data: ${e.message}")
                    break
                } catch (e: ClassNotFoundException) {
                    Log.e(TAG, "Class data not found: ${e.message}")
                    break
                }
            }
        }

        fun sendData(data: Pokemon) {
            try {
                objectOutputStream.writeObject(data)
                objectOutputStream.flush()
                application.sendBroadcast(Intent(BluetoothConnectionReceiver.POKEMON_SENT))
            } catch (e: IOException) {
                error = e.toError()
                Log.e(TAG, "Error sending data: ${e.message}")
            }
        }

        fun cancel() {
            try {
                inputStream.close()
                outputStream.close()
                objectInputStream.close()
                objectOutputStream.close()
                socket.close()
            } catch (e: IOException) {
                error = e.toError()
                Log.e(TAG, "Error closing connection: ${e.message}")
            }
        }
    }

}

/*override suspend fun sendPokemon(pokemon: Any): Error? = tryCall {
    withContext(Dispatchers.IO) {
        objectOutputStream.writeObject(pokemon)
        objectOutputStream.flush()
    }
}.fold(
    ifLeft = { it },
    ifRight = { null }
)*/
