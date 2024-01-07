package com.nurunabiyev.wpmapp.features.wpmcounter.domain

data class Stats(
    val wpm: Int = 0,
    // optional part of the assignment
    val wordCharacterAccuracy: Int = 0,
    val wpmWithAccuracy: Int = 0
)