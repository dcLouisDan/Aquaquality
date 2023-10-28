package com.example.aquaquality.data

data class SuggestionInfo(
    val suggestionType: SuggestionType,
    val headline: Int,
    val solutionList: List<Int>,
    val consequenceList: List<Int>,
)

enum class SuggestionType {
    LOW, HIGH
}