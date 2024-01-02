package com.nurunabiyev.wpmapp.features.wpmcounter.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue

class TypingViewModel {
    var text by mutableStateOf(TextFieldValue())

    fun registerNewKeystroke(current: TextFieldValue) {
        if (!validate(current)) return

        text = current
    }

    private fun validate(current: TextFieldValue): Boolean {
        return when {
            current.selected -> false
            current.detectDeletion(text) -> false
            current.cursorNotInTheEnd -> false
            else -> return true
        }
    }
}

val TextFieldValue.selected: Boolean
    get() = !selection.collapsed

val TextFieldValue.cursorNotInTheEnd: Boolean
    get() = selection.min < text.length

fun TextFieldValue.detectDeletion(original: TextFieldValue): Boolean {
    return text.length < original.text.length
}