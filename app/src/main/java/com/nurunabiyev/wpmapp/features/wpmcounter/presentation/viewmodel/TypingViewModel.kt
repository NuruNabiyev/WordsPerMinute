package com.nurunabiyev.wpmapp.features.wpmcounter.presentation.viewmodel

import android.content.res.Configuration
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
import com.nurunabiyev.wpmapp.core.user.domain.User
import com.nurunabiyev.wpmapp.core.utils.cursorNotInTheEnd
import com.nurunabiyev.wpmapp.core.utils.detectDeletion
import com.nurunabiyev.wpmapp.core.utils.hasSelection
import com.nurunabiyev.wpmapp.features.wpmcounter.domain.Analytics
import com.nurunabiyev.wpmapp.features.wpmcounter.domain.Keystroke
import com.nurunabiyev.wpmapp.ui.theme.correctLetter
import com.nurunabiyev.wpmapp.ui.theme.incorrectLetter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TypingViewModel @Inject constructor() : ViewModel() {
    private val referenceText = """
            He thought he would light the fire when
            he got inside, and make himself some
            breakfast, just to pass away the time;
            but he did not seem able to handle anything
            from a scuttleful of coals to a
        """.trimIndent()

    var text by mutableStateOf(TextFieldValue())
    var currentReference = mutableStateOf(restOfReference(0))

    private val rawKeystrokes = arrayListOf<Keystroke>()
    private var latestKeystroke = MutableSharedFlow<Keystroke>()

    var analytics = Analytics(referenceText, latestKeystroke, viewModelScope)
        private set

    val inputEnabled
        get() = text.text.length < referenceText.length

    var user: User? = null
    var currentOrientation: Int = Configuration.ORIENTATION_PORTRAIT

    fun registerNewKeystroke(current: TextFieldValue) {
        if (!validate(current)) return

        val diffText = getDiffText(current)

        diffText.forEach {
            val generatedKeystroke = Keystroke(
                keyCodeChar = it,
                keyEnterTime = System.currentTimeMillis(),
                phoneOrientation = currentOrientation,
                username = user!!.username
            )
            rawKeystrokes.add(generatedKeystroke)
            generateNextReferenceText()
            viewModelScope.launch(Dispatchers.Default) {
                latestKeystroke.emit(generatedKeystroke)
            }
        }

        text = current
    }

    private fun getDiffText(new: TextFieldValue): String {
        return new.text.substring(text.text.length)
    }

    private fun validate(current: TextFieldValue): Boolean {
        return when {
            current.hasSelection -> false
            current.detectDeletion(text) -> false
            current.cursorNotInTheEnd -> false
            current.text == text.text -> {
                text = current
                false
            }

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

    fun reset() = viewModelScope.launch {
        text = TextFieldValue()
        currentReference.value = restOfReference(0)
        rawKeystrokes.clear()
        analytics = Analytics(referenceText, latestKeystroke, viewModelScope)
    }
}