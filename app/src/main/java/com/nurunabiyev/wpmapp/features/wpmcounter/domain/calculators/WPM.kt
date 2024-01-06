package com.nurunabiyev.wpmapp.features.wpmcounter.domain.calculators

import com.nurunabiyev.wpmapp.features.wpmcounter.domain.ReferenceInput
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.roundToInt

class WPM: ICalc {
    override fun calculate(
        currentSOA: List<ReferenceInput>,
        index: Int,
        typingStartTime: Long,
        totalCorrectWords: AtomicInteger
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
        val wpm = perMinute * totalCorrectWords.incrementAndGet() // todo should not mutate here
        return wpm.roundToInt()
    }
}