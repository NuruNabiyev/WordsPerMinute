package com.nurunabiyev.wpmapp.features.wpmcounter.domain

data class Stats(
    val wpm: Int = 0,
    val accuracy: Int = 0,
    val mistakes: Int = 0,
    val wpmWithAccuracy: Int = 0
)