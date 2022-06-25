package com.ola.recoverunsold.models

data class LatLong(val latitude: Double, val longitude: Double) {
    companion object {
        fun zero(): LatLong = LatLong(0.0, 0.0)
    }
}