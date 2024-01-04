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
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class Analytics(
    val referenceText: String,
    val input: MutableSharedFlow<Keystroke>,
    viewModelScope: CoroutineScope
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
        viewModelScope.launch(Dispatchers.Default) {
            input.collectIndexed { index, value ->
                if (value.keyEnterTime - lastCharacterReceivedTime > MAX_WAIT_TIME) {
                    typingStartTime.set(value.keyEnterTime)
                    totalCorrectWords.set(0)
                }
                currentSOA[index].input = value
                currentSOA[index].isInputCorrect = value.keyCodeChar ==  currentSOA[index].referenceChar
                lastCharacterReceivedTime = value.keyEnterTime

                calculateWPM(index)
            }
        }
    }

    private fun calculateWPM(index: Int) {
        if (currentSOA.getOrNull(index + 1)?.isPartOfWord == true) return
        // area where this is the end of a word

        val iterator = currentSOA.subList(0, index + 1).listIterator(index + 1)
        var wordIsCorrect = true
        do {
            val p = iterator.previous()
            wordIsCorrect = when {
                p.isPartOfWord && p.isInputCorrect == true -> continue
                else -> !p.isPartOfWord
            }
            break
        } while (iterator.hasPrevious())

        if (!wordIsCorrect) return
        val timePassedSinceTypingStarted = currentSOA[index].input!!.keyEnterTime - typingStartTime.get()
        if (timePassedSinceTypingStarted == 0L) return // just started
        val wpm = 60_000 / timePassedSinceTypingStarted * totalCorrectWords.incrementAndGet()

        stats.add(currentStat.value.copy(wpm = wpm.toInt()))
    }

    ////// OPTIONAL

    fun getCurrentWordAccuracy(): Int {
        TODO("Not implemented")
    }

    fun getCurrentMistakes(): Int {
        TODO("Not implemented")
    }

    fun getCurrentAdjustedWPM(): Int {
        TODO("Not implemented")
    }
}

private data class ReferenceInput(
    val referenceChar: Char,
    val isPartOfWord: Boolean,
    var input: Keystroke? = null,
    var isInputCorrect: Boolean? = null
)