package com.nurunabiyev.wpmapp.features.wpmcounter.domain.calculators

import com.nurunabiyev.wpmapp.features.wpmcounter.domain.ReferenceInput
import com.nurunabiyev.wpmapp.features.wpmcounter.domain.Stats
import kotlin.math.roundToInt

class CharacterAccuracy : ICalc {
    private var mistakenWordCharacters: Int = 0
    private var correctWordCharacters: Int = 0

    override fun calculate(
        currentSOA: List<ReferenceInput>,
        index: Int,
        typingStartTime: Long,
        currentStats: Stats
    ): Stats? {
        if (!currentSOA[index].isPartOfWord || currentSOA[index].isInputCorrect == null) return null
        when (currentSOA[index].isInputCorrect) {
            true -> correctWordCharacters++
            false -> mistakenWordCharacters++
            null -> return null
        }
        val ratio = correctWordCharacters.toFloat() / (correctWordCharacters + mistakenWordCharacters)
        return currentStats.copy(wordCharacterAccuracy = (ratio * 100).roundToInt())
    }
}