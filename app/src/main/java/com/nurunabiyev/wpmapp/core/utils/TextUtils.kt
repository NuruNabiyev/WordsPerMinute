package com.nurunabiyev.wpmapp.core.utils

import androidx.compose.ui.text.input.TextFieldValue

val TextFieldValue.hasSelection: Boolean
    get() = !selection.collapsed

val TextFieldValue.cursorNotInTheEnd: Boolean
    get() = selection.min < text.length

fun TextFieldValue.detectDeletion(original: TextFieldValue): Boolean {
    return text.length < original.text.length
}