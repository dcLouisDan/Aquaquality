package com.example.aquaquality.data.local

import com.example.aquaquality.data.SuggestionInfo
import com.example.aquaquality.data.SuggestionType

object LocalInfoProvider {
    //TEMPERATURE
    val highTempTreatments = listOf(
        "Increase the flow of water by using pumps, aerators, or fountains. This method can also help in lowering the water temperature by blending it with cooler water that comes from deeper layers or other sources.",
    )
    val highTempConsequences = listOf(
        "When the water temperature increases, the fish's metabolism and respiration rate also increase. This means that they need more oxygen to survive.",
        "Moreover, the amount of oxygen that can dissolve in water decreases as the temperature increases. This can lead to oxygen levels in the water becoming too low for the fish to survive."
    )

    val lowTempTreatments = listOf(
        "Try to reduce the water exchange or circulation the water in the pond. This can help retain the heat in the water and prevent it from being mixed with colder water from outside sources",
        "Increase the feeding rate or use high-protein feeds. Feeding can increase the metabolic rate and heat production of the fish, as well as their immunity and growth. However, feeding should be done carefully and according to the appetite of the fish, as overfeeding can cause water quality problems."
    )
    val lowTempConsequences = listOf(
        "Low temperature can reduce the feed intake and growth rate of catfish. Catfish need a warm water temperature of about 26°C to 32°C to digest their food well and grow fast.",
        "Low temperature can also change the water quality in the fishponds. Low temperature can reduce the oxygen level and increase the ammonia level in the water. Oxygen is needed by catfish to breathe and by bacteria to break down wastes."
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
        "For fishponds, it is recommended to change the water in the pond to normalize its pH level.",
    )
    val highPhConsequences = listOf(
        "When the pH of water is too high, it can make the water less oxygenated, which can suffocate the fish.",
        "It can also make the water more alkaline, which can damage the fish's skin and eyes."
    )

    val lowPhTreatments = listOf(
        "For fishponds, it is recommended to change the water in the pond to normalize its pH level.",
    )
    val lowPhConsequences = listOf(
        "When the pH of water is too low, it can damage the fish's gills and make it difficult for them to breathe.",
        "It can also make the water more toxic to fish."
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
        "For fishponds, it is recommended to change the water in the pond to clear the excess suspend solids in the water.",
    )
    val highTurbConsequences = listOf(
        "If the turbidity of the water in the pond is too high, it can block the sunlight from reaching the plants and algae in the water. These plants and algae are important for producing oxygen and food for the fish. Without enough sunlight, they cannot grow well and may die. This reduces the oxygen level and the food supply for the fish.",
        "High water turbidity can also carry harmful substances, such as sediments, chemicals, bacteria, and parasites, that can damage the gills, skin, eyes, and internal organs of the fish. These substances can also reduce the quality of the water and make it more acidic or alkaline. This can stress the fish and make them more vulnerable to diseases and infections."
    )

    val lowTurbTreatments = listOf(
        "Low water turbidity does not require any treatment."
    )
    val lowTurbConsequences = listOf(
        "There are no known negative consequence for low water turbidity."
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