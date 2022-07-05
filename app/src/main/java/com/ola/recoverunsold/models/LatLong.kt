package com.ola.recoverunsold.models

data class LatLong(val latitude: Double, val longitude: Double) {
    companion object {
        fun zero(): LatLong = LatLong(0.0, 0.0)

        fun fromString(string: String): LatLong {
            val components = string.split(',')
            require(components.size == 2) { "You must give at and long with ',' as separator" }

            return LatLong(
                latitude = components[0].toDouble(),
                longitude = components[1].toDouble()
            )
        }
    }

    override fun toString(): String {
        return "$latitude,$longitude"
    }
}