package com.nurunabiyev.wpmapp.features.wpmcounter.domain.calculators

import com.nurunabiyev.wpmapp.features.wpmcounter.domain.ReferenceInput
import java.util.concurrent.atomic.AtomicInteger

sealed interface ICalc {
    fun calculate(
        currentSOA: List<ReferenceInput>,
        index: Int,
        typingStartTime: Long,
        totalCorrectWords: AtomicInteger
    ): Int?
}