package dev.aerin.managerspecials.models

data class SpecialsPage(val canvasUnit: Int, val managerSpecials: List<Special>) {

    fun getUnitSize(overallSize: Int): Int {
        return overallSize / canvasUnit
    }
}
