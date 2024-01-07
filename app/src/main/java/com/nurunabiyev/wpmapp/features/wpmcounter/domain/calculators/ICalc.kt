package com.nurunabiyev.wpmapp.features.wpmcounter.domain.calculators

import com.nurunabiyev.wpmapp.features.wpmcounter.domain.ReferenceInput
import com.nurunabiyev.wpmapp.features.wpmcounter.domain.Stats

sealed interface ICalc {
    fun calculate(
        currentSOA: List<ReferenceInput>,
        index: Int,
        typingStartTime: Long,
        currentStats: Stats
    ): Stats?

    /**
     * Some calculators need to be reset, e.g. when user does not type for some time
     */
    fun reset() {}
}