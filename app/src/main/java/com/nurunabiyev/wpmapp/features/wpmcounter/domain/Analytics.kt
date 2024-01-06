package com.nurunabiyev.wpmapp.features.wpmcounter.domain

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

class Analytics(
    referenceText: String,
    private val input: MutableSharedFlow<Keystroke>,
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
                currentSOA[index].isInputCorrect =
                    value.keyCodeChar == currentSOA[index].referenceChar
                lastCharacterReceivedTime = value.keyEnterTime

                runCalculators(index)
            }
        }
    }

    private fun runCalculators(index: Int) {
        Calculator.values().forEach { calculator ->
            val result = calculator.calculate(currentSOA, index, typingStartTime, totalCorrectWords)
                ?: return@forEach
            val newStat = when (calculator) {
                Calculator.WPM -> currentStat.value.copy(wpm = result)
                Calculator.MISTAKES -> currentStat.value.copy(mistakes = result)
            }
            stats.add(newStat)
        }
    }
}

data class ReferenceInput(
    val referenceChar: Char,
    val isPartOfWord: Boolean,
    var input: Keystroke? = null,
    var isInputCorrect: Boolean? = null
)