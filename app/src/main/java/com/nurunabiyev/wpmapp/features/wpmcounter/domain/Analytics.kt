package com.nurunabiyev.wpmapp.features.wpmcounter.domain

import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.AnnotatedString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class Analytics(
    val input: MutableSharedFlow<BAD>,
    val currentReference: MutableState<AnnotatedString>,
    viewModelScope: CoroutineScope
) {
    val currentWPM: MutableStateFlow<Int> = MutableStateFlow(0)

    private var inputStartTime: Long = 0

    init {
        viewModelScope.launch(Dispatchers.Default) {
            input.collectLatest {
                println("COLLECTED: bad=${it}")
//                currentReference.value.spanStyles.forEach {
//                    println(" ref=${it}")
//                }

            }
        }
    }

    private fun calculateWPM() {

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