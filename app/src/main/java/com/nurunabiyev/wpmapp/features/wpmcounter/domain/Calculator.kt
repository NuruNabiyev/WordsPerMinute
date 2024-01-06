package com.nurunabiyev.wpmapp.features.wpmcounter.domain

import com.nurunabiyev.wpmapp.core.utils.printLog
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.roundToInt

enum class Calculator {
    WPM {
        override fun calculate(
            currentSOA: List<ReferenceInput>,
            index: Int,
            typingStartTime: AtomicLong,
            totalCorrectWords: AtomicInteger
        ): Int? { // todo Monad
            if (currentSOA.getOrNull(index + 1)?.isPartOfWord == true) return null
            // area where this is the end of a word

            val sublist = currentSOA.subList(0, index + 1)
            val iterator = sublist.listIterator(index + 1)
            printLog("  ${sublist.map { it.referenceChar }}")

            var wordIsCorrect = false
            do {
                val p = iterator.previous()
                printLog("  p = ${p.referenceChar}")
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
                currentSOA[index].input!!.keyEnterTime - typingStartTime.get()
            if (timePassedSinceTypingStarted == 0L) return null // just started
            val perMinute = 60_000.0 / timePassedSinceTypingStarted
            val wpm = perMinute * totalCorrectWords.incrementAndGet() // todo should not mutate here
            return wpm.roundToInt()
        }
    },

    ////// OPTIONAL PART OF ASSIGNMENT

    MISTAKES {
        override fun calculate(
            currentSOA: List<ReferenceInput>,
            index: Int,
            typingStartTime: AtomicLong,
            totalCorrectWords: AtomicInteger
        ): Int {
            val sublist = currentSOA.subList(0, index + 1)
            val iterator = sublist.listIterator(index + 1)
            printLog("  ${sublist.map { it.referenceChar }}")

            var c = 0
            var prevCharIsWord = false
            do {
                val p = iterator.previous()
                if (p.isPartOfWord && !prevCharIsWord) {
                    c++
                    prevCharIsWord = true
                } else if (!p.isPartOfWord) {
                    prevCharIsWord = false
                }
            } while (iterator.hasPrevious())

            val totalNumberOfReferenceWordsUntilNow = c
            return totalNumberOfReferenceWordsUntilNow - totalCorrectWords.get()
        }
    },

    ;

    abstract fun calculate(
        currentSOA: List<ReferenceInput>,
        index: Int,
        typingStartTime: AtomicLong,
        totalCorrectWords: AtomicInteger
    ): Int?
}