package view

import api.ImageApi
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BirdViewModel(
    private val api: ImageApi
) : ViewModel() {
    private val _uiState = MutableStateFlow<BirdState>(BirdState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        updateImages()
    }

    fun selectCategory(category: String) {
        _uiState.update {
            if (it is BirdState.Success)
                it.copy(selectedCategory = category)
            else it
        }
    }

    fun updateImages() {
        _uiState.update { BirdState.Loading }
        viewModelScope.launch {
            try {
                val images = api.getImages()
                _uiState.update {
                    if (it is BirdState.Success)
                        it.copy(images = images, selectedCategory = null)
                    else BirdState.Success(images = images, selectedCategory = null)
                }
            } catch (exception: Exception) {
                _uiState.update { BirdState.Error("${exception.message}") }
            }
        }
    }
}