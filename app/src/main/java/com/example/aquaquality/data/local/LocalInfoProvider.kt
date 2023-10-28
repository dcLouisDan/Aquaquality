package com.example.aquaquality.data.local

import com.example.aquaquality.R
import com.example.aquaquality.data.SuggestionInfo
import com.example.aquaquality.data.SuggestionType

object LocalInfoProvider {
    //TEMPERATURE
    val highTempTreatments = listOf(
        R.string.highTempTreatment1,
    )
    val highTempConsequences = listOf(
        R.string.highTempConsequences1,
        R.string.highTempConsequences2
    )

    val lowTempTreatments = listOf(
        R.string.lowTempTreatments1,
        R.string.lowTempTreatments2
    )
    val lowTempConsequences = listOf(
        R.string.lowTempConsequence1,
        R.string.lowTempConsequence2
    )

    val highTempSuggestionInfo = SuggestionInfo(
        suggestionType = SuggestionType.HIGH,
        headline = R.string.high_temp_headline,
        solutionList = highTempTreatments,
        consequenceList = highTempConsequences
    )

    val lowTempSuggestionInfo = SuggestionInfo(
        suggestionType = SuggestionType.LOW,
        headline = R.string.low_temp_headline,
        solutionList = lowTempTreatments,
        consequenceList = lowTempConsequences
    )

    //PH
    val highPhTreatments = listOf(
        R.string.highPhTreatments1,
    )
    val highPhConsequences = listOf(
        R.string.highPhConsequences1,
        R.string.highPhConsequences2
    )

    val lowPhTreatments = listOf(
        R.string.lowPhTreatments1,
    )
    val lowPhConsequences = listOf(
        R.string.lowPhConsequences1,
        R.string.lowPhConsequences2
    )

    val highPhSuggestionInfo = SuggestionInfo(
        suggestionType = SuggestionType.HIGH,
        headline = R.string.high_ph_headline,
        solutionList = highPhTreatments,
        consequenceList = highPhConsequences
    )

    val lowPhSuggestionInfo = SuggestionInfo(
        suggestionType = SuggestionType.LOW,
        headline = R.string.low_ph_headline,
        solutionList = lowPhTreatments,
        consequenceList = lowPhConsequences
    )

    //TURBIDITY
    val highTurbTreatments = listOf(
        R.string.highTurbTreatments1,
    )
    val highTurbConsequences = listOf(
        R.string.highTurbConsequences1,
        R.string.highTurbConsequences2
    )

    val lowTurbTreatments = listOf(
        R.string.lowTurbTreatments1
    )
    val lowTurbConsequences = listOf(
        R.string.lowTurbConsequences1
    )

    val highTurbSuggestionInfo = SuggestionInfo(
        suggestionType = SuggestionType.HIGH,
        headline = R.string.high_turb_headline,
        solutionList = highTurbTreatments,
        consequenceList = highTurbConsequences
    )

    val lowTurbSuggestionInfo = SuggestionInfo(
        suggestionType = SuggestionType.LOW,
        headline = R.string.low_turb_headline,
        solutionList = lowTurbTreatments,
        consequenceList = lowTurbConsequences
    )
}