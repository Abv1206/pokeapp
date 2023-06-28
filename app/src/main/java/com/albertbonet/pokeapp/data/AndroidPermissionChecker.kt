package com.albertbonet.pokeapp.data

import android.app.Application
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class AndroidPermissionChecker(private val application: Application): PermissionChecker {

    override fun check(permission: String): Boolean = ContextCompat.checkSelfPermission(
        application,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}