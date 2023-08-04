package com.example.aquaquality.data

data class SuggestionInfo(
    val suggestionType: SuggestionType,
    val headline: String,
    val solutionList: List<String>,
    val consequenceList: List<String>,
)

enum class SuggestionType {
    LOW, HIGH
}