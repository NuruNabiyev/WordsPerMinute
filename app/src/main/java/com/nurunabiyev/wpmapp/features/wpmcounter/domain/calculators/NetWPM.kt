package com.nurunabiyev.wpmapp.features.wpmcounter.domain.calculators

import com.nurunabiyev.wpmapp.features.wpmcounter.domain.ReferenceInput
import com.nurunabiyev.wpmapp.features.wpmcounter.domain.Stats
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.roundToInt

class NetWPM : ICalc {

    override fun calculate(
        currentSOA: List<ReferenceInput>,
        index: Int,
        typingStartTime: Long,
        currentStats: Stats
    ): Stats {
        val netWPM = currentStats.wpm * currentStats.wordCharacterAccuracy / 100.0
        return currentStats.copy(wpmWithAccuracy = netWPM.roundToInt())
    }

    override fun reset() {

    }
}