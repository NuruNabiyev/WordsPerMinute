package com.nurunabiyev.wpmapp.features.wpmcounter.domain

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import com.nurunabiyev.wpmapp.features.wpmcounter.domain.calculators.CharacterAccuracy
import com.nurunabiyev.wpmapp.features.wpmcounter.domain.calculators.ICalc
import com.nurunabiyev.wpmapp.features.wpmcounter.domain.calculators.NetWPM
import com.nurunabiyev.wpmapp.features.wpmcounter.domain.calculators.WPM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

/**
 * Maps each character in the reference text to input.
 *
 * In order to optimize calculations, additional pre-calculations are done,s.a. [isPartOfWord]
 *
 */
data class ReferenceInput(
    val referenceChar: Char,
    val isPartOfWord: Boolean,
    var input: Keystroke? = null,
    var isInputCorrect: Boolean? = null
)

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

    private val calculators: List<ICalc> by lazy {
        listOf(
            WPM(),
            CharacterAccuracy(),
            NetWPM(),
        )
    }

    val currentStat = derivedStateOf { stats.lastOrNull() ?: Stats() }
    val lastTypeTime = derivedStateOf {
        ((stats.lastOrNull() ?: Stats()) to System.currentTimeMillis()).second
    }

    init {
        scope.launch(Dispatchers.Default) {
            input.collectIndexed { index, value ->
                if (value.keyEnterTime - lastCharacterReceivedTime > MAX_WAIT_TIME) {
                    typingStartTime.set(value.keyEnterTime)
                    calculators.forEach { it.reset() }
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
        calculators.forEach { calculator ->
            val result = calculator.calculate(
                currentSOA,
                index,
                typingStartTime.get(),
                currentStat.value
            ) ?: return@forEach
            stats.add(result)
        }
    }

    companion object {
        const val MAX_WAIT_TIME = 3_000L
    }
}