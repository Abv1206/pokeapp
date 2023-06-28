package com.albertbonet.pokeapp.data


interface PermissionChecker {

    fun check(permission: String): Boolean
}