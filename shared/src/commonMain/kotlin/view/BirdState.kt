package view

import model.BirdImage

sealed interface BirdState {
    data object Loading : BirdState
    data class Error(val msg: String) : BirdState
    data class Success(
        val images: List<BirdImage> = emptyList(),
        val selectedCategory: String? = null
    ) : BirdState {
        val categories = images.map { it.category }.toSet()
        val selectedImages = images.filter { it.category == selectedCategory }
    }
}