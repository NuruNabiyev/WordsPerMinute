package com.nurunabiyev.wpmapp.features.wpmcounter.domain.calculators

import com.nurunabiyev.wpmapp.features.wpmcounter.domain.ReferenceInput
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.roundToInt

class WPM : ICalc {
    private var totalCorrectWords = 0

    override fun calculate(
        currentSOA: List<ReferenceInput>,
        index: Int,
        typingStartTime: Long,
    ): Int? {
        if (currentSOA.getOrNull(index + 1)?.isPartOfWord == true) return null
        // area where this is the end of a word

        val sublist = currentSOA.subList(0, index + 1)
        val iterator = sublist.listIterator(index + 1)

        var wordIsCorrect = false
        do {
            val p = iterator.previous()
            if (p.isPartOfWord && p.isInputCorrect == true) {
                wordIsCorrect = true
                continue
            } else {
                wordIsCorrect = !p.isPartOfWord && wordIsCorrect
                break
            }
        } while (iterator.hasPrevious())

        if (!wordIsCorrect) return null
        val timePassedSinceTypingStarted =
            currentSOA[index].input!!.keyEnterTime - typingStartTime
        if (timePassedSinceTypingStarted == 0L) return null // just started
        val perMinute = 60_000.0 / timePassedSinceTypingStarted
        val wpm = perMinute * ++totalCorrectWords
        return wpm.roundToInt()
    }

    override fun reset() {
        totalCorrectWords = 0
    }
}