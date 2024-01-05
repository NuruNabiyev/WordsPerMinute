package com.nurunabiyev.wpmapp.features.wpmcounter.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nurunabiyev.wpmapp.features.wpmcounter.domain.Analytics
import com.nurunabiyev.wpmapp.features.wpmcounter.domain.Keystroke
import com.nurunabiyev.wpmapp.ui.theme.correctLetter
import com.nurunabiyev.wpmapp.ui.theme.incorrectLetter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class TypingViewModel: ViewModel() {
    private val referenceText = """
            He would, do
        """.trimIndent()
//    private val referenceText = """
//            He thought he would light the fire when
//            he got inside, and make himself some
//            breakfast, just to pass away the time;
//            but he did not seem able to handle anything
//            from a scuttleful of coals to a
//        """.trimIndent()

    var text by mutableStateOf(TextFieldValue())
    var currentReference = mutableStateOf(restOfReference(0))

    private val rawKeystrokes = arrayListOf<Keystroke>()
    private var latestKeystroke = MutableSharedFlow<Keystroke>()
        private set

    var analytics = Analytics(referenceText, latestKeystroke, viewModelScope)
        private set

    val inputEnabled
        get()= text.text.length < referenceText.length

    fun registerNewKeystroke(current: TextFieldValue) {
        if (!validate(current)) return
        // todo sometimes multple chars are entered at once
        // todo copy-paste is not disabled
        val generatedKeystroke = Keystroke(
            keyCodeChar = current.text.lastOrNull() ?: return,
            keyEnterTime = System.currentTimeMillis(),
            phoneOrientation = false,// todo
            username = "TODO"
        )
        rawKeystrokes.add(generatedKeystroke)
        text = current
        viewModelScope.launch(Dispatchers.Default) {
            generateNextReferenceText()
            latestKeystroke.emit(generatedKeystroke)
        }
    }

    private fun validate(current: TextFieldValue): Boolean {
        return when {
            current.hasSelection -> false
            current.detectDeletion(text) -> false
            current.cursorNotInTheEnd -> false
            current.text == text.text -> {
                text = current
                false // causes to loop on copy-paste todo fix
            }
            //text.text.length + 1 < current.text.length -> false
            else -> true
        }
    }

    private fun generateNextReferenceText() {
        if (rawKeystrokes.isEmpty() || rawKeystrokes.size > referenceText.length) return
        currentReference.value = buildAnnotatedString {
            append(currentReference.value.subSequence(0, rawKeystrokes.size - 1))
            if (rawKeystrokes.last().keyCodeChar == referenceText[rawKeystrokes.lastIndex]) {
                withStyle(correctLetter) { append(rawKeystrokes.last().keyCodeChar) }
            } else {
                withStyle(incorrectLetter) { append(referenceText[rawKeystrokes.lastIndex]) }
            }
            if (rawKeystrokes.size < referenceText.length) {
                append(restOfReference(rawKeystrokes.size))
            }
        }
    }

    private fun restOfReference(from: Int): AnnotatedString {
        return buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append(referenceText.substring(from, from + 1))
            }
            append(referenceText.substring(from + 1))
        }
    }

    fun reset() {
        text = TextFieldValue()
        rawKeystrokes.clear()
        currentReference.value = restOfReference(0)
        latestKeystroke = MutableSharedFlow()
        analytics = Analytics(referenceText, latestKeystroke, viewModelScope)
    }
}

val TextFieldValue.hasSelection: Boolean
    get() = !selection.collapsed

val TextFieldValue.cursorNotInTheEnd: Boolean
    get() = selection.min < text.length

fun TextFieldValue.detectDeletion(original: TextFieldValue): Boolean {
    return text.length < original.text.length
}