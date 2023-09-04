package com.example.aquaquality.data.local

import com.example.aquaquality.data.SuggestionInfo
import com.example.aquaquality.data.SuggestionType

object LocalInfoProvider {
    //TEMPERATURE
    val highTempTreatments = listOf(
        "This is a sentence that narrates a possible treatment solution that the farmer can implement in order to manage the fishpond’s high water temperature.",
        "This is also a sentence that narrates another possible treatment solution that the farmer can implement in order to manage the fishpond’s high water temperature."
    )
    val highTempConsequences = listOf(
        "This is a paragraph that narrates a possible consequence that might happen if the water temperature is left unmanaged.",
        "This is also another paragraph that narrates a possible consequence that might happen if the water temperature is left unmanaged."
    )

    val lowTempTreatments = listOf(
        "This is a sentence that narrates a possible treatment solution that the farmer can implement in order to manage the fishpond’s low water temperature.",
        "This is also a sentence that narrates another possible treatment solution that the farmer can implement in order to manage the fishpond’s low water temperature."
    )
    val lowTempConsequences = listOf(
        "This is a paragraph that narrates a possible consequence that might happen if the water temperature is left unmanaged.",
        "This is also another paragraph that narrates a possible consequence that might happen if the water temperature is left unmanaged."
    )

    val highTempSuggestionInfo = SuggestionInfo(
        suggestionType = SuggestionType.HIGH,
        headline = "The water temperature of one of the fishponds has exceeded the recommended range.",
        solutionList = highTempTreatments,
        consequenceList = highTempConsequences
    )

    val lowTempSuggestionInfo = SuggestionInfo(
        suggestionType = SuggestionType.LOW,
        headline = "The temperature of one of the fishponds is below the recommended range.",
        solutionList = lowTempTreatments,
        consequenceList = lowTempConsequences
    )

    //PH
    val highPhTreatments = listOf(
        "This is a sentence that narrates a possible treatment solution that the farmer can implement in order to manage the fishpond’s high water pH level.",
        "This is also a sentence that narrates another possible treatment solution that the farmer can implement in order to manage the fishpond’s high water pH level."
    )
    val highPhConsequences = listOf(
        "This is a paragraph that narrates a possible consequence that might happen if the water pH level is left unmanaged.",
        "This is also another paragraph that narrates a possible consequence that might happen if the water pH level is left unmanaged."
    )

    val lowPhTreatments = listOf(
        "This is a sentence that narrates a possible treatment solution that the farmer can implement in order to manage the fishpond’s low water pH level.",
        "This is also a sentence that narrates another possible treatment solution that the farmer can implement in order to manage the fishpond’s low water pH level."
    )
    val lowPhConsequences = listOf(
        "This is a paragraph that narrates a possible consequence that might happen if the water pH level is left unmanaged.",
        "This is also another paragraph that narrates a possible consequence that might happen if the water pH level is left unmanaged."
    )

    val highPhSuggestionInfo = SuggestionInfo(
        suggestionType = SuggestionType.HIGH,
        headline = "The pH level of one of the fishponds has exceeded the recommended range.",
        solutionList = highPhTreatments,
        consequenceList = highPhConsequences
    )

    val lowPhSuggestionInfo = SuggestionInfo(
        suggestionType = SuggestionType.LOW,
        headline = "The pH level of one of the fishponds is below the recommended range.",
        solutionList = lowPhTreatments,
        consequenceList = lowPhConsequences
    )

    //TURBIDITY
    val highTurbTreatments = listOf(
        "This is a sentence that narrates a possible treatment solution that the farmer can implement in order to manage the fishpond’s high water turbidity.",
        "This is also a sentence that narrates another possible treatment solution that the farmer can implement in order to manage the fishpond’s high water turbidity."
    )
    val highTurbConsequences = listOf(
        "This is a paragraph that narrates a possible consequence that might happen if the water turbidity is left unmanaged.",
        "This is also another paragraph that narrates a possible consequence that might happen if the water turbidity is left unmanaged."
    )

    val lowTurbTreatments = listOf(
        "This is a sentence that narrates a possible treatment solution that the farmer can implement in order to manage the fishpond’s low water turbidity.",
        "This is also a sentence that narrates another possible treatment solution that the farmer can implement in order to manage the fishpond’s low water turbidity."
    )
    val lowTurbConsequences = listOf(
        "This is a paragraph that narrates a possible consequence that might happen if the water turbidity is left unmanaged.",
        "This is also another paragraph that narrates a possible consequence that might happen if the water turbidity is left unmanaged."
    )

    val highTurbSuggestionInfo = SuggestionInfo(
        suggestionType = SuggestionType.HIGH,
        headline = "The turbidity of one of the fishponds has exceeded the recommended range.",
        solutionList = highTurbTreatments,
        consequenceList = highTurbConsequences
    )

    val lowTurbSuggestionInfo = SuggestionInfo(
        suggestionType = SuggestionType.LOW,
        headline = "The turbidity of one of the fishponds is below the recommended range.",
        solutionList = lowTurbTreatments,
        consequenceList = lowTurbConsequences
    )
}