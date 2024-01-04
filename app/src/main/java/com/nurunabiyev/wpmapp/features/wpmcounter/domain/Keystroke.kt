package com.nurunabiyev.wpmapp.features.wpmcounter.domain

/**
 * Behavioral Analytics Data
 * Notes:
 *  - It is not possible to detect Enter and Release time with soft keyboard, so [keyEnterTime] is used
 *  - It is not possible to get keyCode from soft keyboard, so [keyCodeChar] is used
 */
data class Keystroke(
    val keyEnterTime: Long,
    val keyCodeChar: Char,
    val phoneOrientation: Boolean,
    val username: String
)