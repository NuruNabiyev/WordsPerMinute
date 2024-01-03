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
import com.nurunabiyev.wpmapp.features.wpmcounter.presentation.view.vm

class TypingViewModel {
    val referenceText = """
            He thought he would light the fire when
            he got inside, and make himself some
            breakfast, just to pass away the time;
            but he did not seem able to handle anything
            from a scuttleful of coals to a
        """.trimIndent()

    var text by mutableStateOf(TextFieldValue())
    val rawBads = mutableStateListOf<BAD>()

    fun registerNewKeystroke(current: TextFieldValue) {
        if (!validate(current)) return

        val generatedBAD = BAD(
            keyCodeChar = current.text.lastOrNull() ?: return,
            keyEnterTime = System.currentTimeMillis(),
            phoneOrientation = false,// todo
            username = "TODO"
        )
        rawBads.add(generatedBAD)

        text = current
    }

    private fun validate(current: TextFieldValue): Boolean {
        return when {
            current.hasSelection -> false
            current.detectDeletion(text) -> false
            current.cursorNotInTheEnd -> false
            current.text == text.text -> false
            else -> true
        }
    }

    fun nextReferenceText(): AnnotatedString {
        return try {
            buildAnnotatedString {
                vm.rawBads.forEachIndexed { index, it ->
                    if (it.keyCodeChar == vm.referenceText[index]) {
                        withStyle(style = SpanStyle(color = Color(0xFF3A923C))) {
                            append(it.keyCodeChar)
                        }
                    } else {
                        withStyle(style = SpanStyle(color = Color(0xFFC02318))) {
                            append(vm.referenceText[index])
                        }
                    }
                }
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    append(vm.referenceText.substring(vm.rawBads.size, vm.rawBads.size + 1))
                }
                append(vm.referenceText.substring(vm.rawBads.size + 1))
            }
        } catch (e: StringIndexOutOfBoundsException) {
            reset()
            nextReferenceText()
        }
    }

    fun reset() {
        vm.text = TextFieldValue()
        rawBads.clear()
    }
}

val TextFieldValue.hasSelection: Boolean
    get() = !selection.collapsed

val TextFieldValue.cursorNotInTheEnd: Boolean
    get() = selection.min < text.length

fun TextFieldValue.detectDeletion(original: TextFieldValue): Boolean {
    return text.length < original.text.length
}