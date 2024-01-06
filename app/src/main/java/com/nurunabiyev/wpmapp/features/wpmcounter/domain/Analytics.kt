package com.nurunabiyev.wpmapp.features.wpmcounter.domain

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import com.nurunabiyev.wpmapp.core.utils.printLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.roundToInt

class Analytics(
    val referenceText: String,
    val input: MutableSharedFlow<Keystroke>,
    scope: CoroutineScope
) {
    /**
     * Current state of affairs between reference text and input
     */
    private val currentSOA = referenceText.map {
        val isLetter = !it.toString().matches(Regex("[\\p{Punct}\\s]+"))
        ReferenceInput(it, isLetter)
    }
    private val stats = mutableStateListOf<Stats>()
    private var typingStartTime = AtomicLong(0)
    private var lastCharacterReceivedTime = 0L
    private val MAX_WAIT_TIME = 5_000L
    private var totalCorrectWords = AtomicInteger(0)

    val currentStat = derivedStateOf { stats.lastOrNull() ?: Stats() }

    init {
        scope.launch(Dispatchers.Default) {
            input.collectIndexed { index, value ->
                if (value.keyEnterTime - lastCharacterReceivedTime > MAX_WAIT_TIME) {
                    typingStartTime.set(value.keyEnterTime)
                    totalCorrectWords.set(0)
                }
                currentSOA[index].input = value
                currentSOA[index].isInputCorrect = value.keyCodeChar ==  currentSOA[index].referenceChar
                lastCharacterReceivedTime = value.keyEnterTime

                Calculator.values().forEach { calculator ->
                    calculator.calculate(currentSOA, index, typingStartTime, totalCorrectWords)
                        ?.let { result ->
                            val new = when (calculator) {
                                Calculator.WPM -> currentStat.value.copy(wpm = result)
                                Calculator.MISTAKES -> currentStat.value.copy(mistakes = result)
                            }
                            stats.add(new)
                        }
                }
            }
        }
    }
}

data class ReferenceInput(
    val referenceChar: Char,
    val isPartOfWord: Boolean,
    var input: Keystroke? = null,
    var isInputCorrect: Boolean? = null
)

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