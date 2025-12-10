package com.facesore.english.vocabulary

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class VocabularyTestViewModel : ViewModel() {

    private val words = getWordList()
    private val knownWords = mutableListOf<String>()

    private val _uiState = MutableStateFlow(VocabularyTestUiState(words[0]))
    val uiState = _uiState.asStateFlow()

    private var currentIndex = 0

    fun answer(knowsWord: Boolean) {
        if (knowsWord) {
            knownWords.add(words[currentIndex].word)
        }

        currentIndex++

        if (currentIndex < words.size) {
            _uiState.value = VocabularyTestUiState(words[currentIndex])
        } else {
            _uiState.value = VocabularyTestUiState(isFinished = true, score = knownWords.size)
        }
    }
}

data class VocabularyTestUiState(
    val currentWord: VocabularyWord? = null,
    val isFinished: Boolean = false,
    val score: Int = 0
)
