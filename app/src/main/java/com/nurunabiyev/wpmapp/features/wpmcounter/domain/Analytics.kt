package com.nurunabiyev.wpmapp.features.wpmcounter.domain

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import com.nurunabiyev.wpmapp.core.utils.printLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch

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

    val currentStats = derivedStateOf { stats.lastOrNull() ?: Stats() }

    init {
        viewModelScope.launch(Dispatchers.Default) {
            input.collectIndexed { index, value ->
                currentSOA[index].input = value
                currentSOA[index].isInputCorrect = value.keyCodeChar ==  currentSOA[index].referenceChar
                printLog(currentSOA[index])
                calculateWPM(index)
            }
        }
    }

    private fun calculateWPM(index: Int) {
        var canCalculate = true
        if (currentSOA.size == index + 1) canCalculate = true
        else if (currentSOA[index + 1].isPartOfWord) canCalculate = false  // todo last (single) letter

        if (!canCalculate) return
        // area where this is the end of a word


        val i: Int = currentSOA.indexOfFirstLetterOfTheWord(index)
        val word = currentSOA.subList(i, index + 1)
        val allLettersOfThisWordAreCorrect = word.all { it.isInputCorrect == true }

        printLog("allLettersOfThisWordAreCorrect = $allLettersOfThisWordAreCorrect; " +
                "indexOfFirstLetterInThisWord = $i")
        /*
        if next one is aux, then check that all previous letters of a word are correct
        start = time of first correct word letter
        this_wpm = 60 / (this wc - start)
        new_stat = (stats.add { wpm } + this_wpm) / stats.size+1
        stats.add(new_stat)
         */
    }

    private inline fun List<ReferenceInput>.indexOfFirstLetterOfTheWord(
        indexOfLastCharacterOfTheWord: Int,
    ): Int {
        val i = this.subList(0, indexOfLastCharacterOfTheWord)
            .listIterator(indexOfLastCharacterOfTheWord)
        while (i.hasPrevious()) {
            if (!i.previous().isPartOfWord) {
                return i.nextIndex() + 1
            }
        }
        return 0
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