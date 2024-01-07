package com.nurunabiyev.wpmapp.features.wpmcounter.domain.calculators

import com.nurunabiyev.wpmapp.features.wpmcounter.domain.ReferenceInput
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.roundToInt

class CharacterAccuracy : ICalc {
    private var mistakenWordCharacters: Int = 0
    private var correctWordCharacters: Int = 0

    override fun calculate(
        currentSOA: List<ReferenceInput>,
        index: Int,
        typingStartTime: Long,
        totalCorrectWords: AtomicInteger
    ): Int? {
        if (!currentSOA[index].isPartOfWord || currentSOA[index].isInputCorrect == null) return null
        when (currentSOA[index].isInputCorrect) {
            true -> correctWordCharacters++
            false -> mistakenWordCharacters++
            null -> return null
        }
        val ratio = correctWordCharacters.toFloat() / (correctWordCharacters + mistakenWordCharacters)
        return (ratio * 100).roundToInt()
    }
}