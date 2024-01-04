package com.nurunabiyev.wpmapp.features.wpmcounter.domain

data class Stats(
    val wpm: Int,
    val accuracy: Int,
    val mistakes: Int,
    val wpmWithAccuracy: Int
)