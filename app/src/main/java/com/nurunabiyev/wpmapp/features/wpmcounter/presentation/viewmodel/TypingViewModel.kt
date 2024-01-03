package com.nurunabiyev.wpmapp.features.wpmcounter.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.nurunabiyev.wpmapp.features.wpmcounter.domain.BAD
import com.nurunabiyev.wpmapp.ui.theme.correctLetter
import com.nurunabiyev.wpmapp.ui.theme.incorrectLetter

class TypingViewModel {
    private val referenceText = """
            He thought
        """.trimIndent()
//    private val referenceText = """
//            He thought he would light the fire when
//            he got inside, and make himself some
//            breakfast, just to pass away the time;
//            but he did not seem able to handle anything
//            from a scuttleful of coals to a
//        """.trimIndent()

    var text by mutableStateOf(TextFieldValue())
    val rawBads = mutableStateListOf<BAD>()
    var currentReference = mutableStateOf(restOfReference(0))

    val inputEnabled
        get()= text.text.length < referenceText.length

    fun registerNewKeystroke(current: TextFieldValue) {
        if (!validate(current)) return

        // todo sometimes multple chars are entered at once
        // todo copy-paste is not disabled
        val generatedBAD = BAD(
            keyCodeChar = current.text.lastOrNull() ?: return,
            keyEnterTime = System.currentTimeMillis(),
            phoneOrientation = false,// todo
            username = "TODO"
        )
        rawBads.add(generatedBAD)
        text = current
        generateNextReferenceText()
    }

    private fun validate(current: TextFieldValue): Boolean {
        return when {
            current.hasSelection -> false
            current.detectDeletion(text) -> false
            current.cursorNotInTheEnd -> false
            current.text == text.text -> false // causes to loop on copy-paste todo fix
            //text.text.length + 1 < current.text.length -> false
            else -> true
        }
    }

    private fun generateNextReferenceText() {
        if (rawBads.isEmpty() || rawBads.size > referenceText.length) return
        currentReference.value = buildAnnotatedString {
            append(currentReference.value.subSequence(0, rawBads.size - 1))
            if (rawBads.last().keyCodeChar == referenceText[rawBads.lastIndex]) {
                withStyle(correctLetter) { append(rawBads.last().keyCodeChar) }
            } else {
                withStyle(incorrectLetter) { append(referenceText[rawBads.lastIndex]) }
            }
            if (rawBads.size < referenceText.length) {
                append(restOfReference(rawBads.size))
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
        rawBads.clear()
        currentReference.value = restOfReference(0)
    }
}

val TextFieldValue.hasSelection: Boolean
    get() = !selection.collapsed

val TextFieldValue.cursorNotInTheEnd: Boolean
    get() = selection.min < text.length

fun TextFieldValue.detectDeletion(original: TextFieldValue): Boolean {
    return text.length < original.text.length
}