package com.pabloginerbarrios.evaluable_2

data class ColorData (
    val color: String,
    val name: String,
    val saveDate: String,
    var isSelected: Boolean = false
) {
    fun toggleSelection() {
        isSelected = !isSelected
    }
}