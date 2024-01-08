package com.nurunabiyev.wpmapp

import com.nurunabiyev.wpmapp.features.wpmcounter.domain.Analytics
import com.nurunabiyev.wpmapp.features.wpmcounter.domain.Keystroke
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.math.absoluteValue

import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.system.measureTimeMillis

/**
 * Analytics local unit test, which will execute on the development machine (host).
 * Checks all the formulas used in [Analytics]
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class AnalyticsTest {
    private val referenceText = """
            This is a reference text, used for testing; 
            words per minute and other s
        """.trimIndent()

    // sprinkle some errors by user
    private val simulatedText = """
            This ** a reference**ext, used for testing* 
            words p*r minute *nd other s
        """.trimIndent()
    private val correctWords = 10
    private val correctAccuracy = (((56 - 5.0) / 56) * 100).roundToInt()

    private var latestKeystroke = MutableSharedFlow<Keystroke>()
    private var analytics = Analytics(referenceText, latestKeystroke, GlobalScope)

    /**
     * Note, this is a flaky test due to nature of [delay] and time calculations
     * Therefore a [passThreshold] was introduced in the end of the test as it is impossible
     * to calculate everything precisely when simulating a random user input
     *
     * The bigger the text, the less error there is
     *
     * Please run this test multiple times; estimated time to run single time is 10 seconds
     */
    @Test
    fun analytics_test() = runBlocking {
        val timeToEnterText = measureTimeMillis {
            simulatedText.forEach {
                delay(Random.nextLong(100, 150)) // imitate human delay
                val generatedKeystroke = Keystroke(
                    keyCodeChar = it,
                    keyEnterTime = System.currentTimeMillis(),
                    phoneOrientation = 1,
                    username = "user"
                )
                latestKeystroke.emit(generatedKeystroke)
            }
        }

        val correctWPM = (60_000.0 / timeToEnterText * correctWords).roundToInt()
        val calculatedWPM = analytics.currentStat.value.wpm
        val wpmDifference = (calculatedWPM - correctWPM).absoluteValue

        val calculatedCharacterAccuracy = analytics.currentStat.value.wordCharacterAccuracy

        val correctNetWPM = (correctWPM * correctAccuracy / 100.0).roundToInt()
        val calculatedNetWPM = analytics.currentStat.value.wpmWithAccuracy
        val netWpmDifference = (calculatedNetWPM - correctNetWPM).absoluteValue

        val log = """
            timeToEnterText=$timeToEnterText
            
            correctWPM=$correctWPM
            calculatedWPM=$calculatedWPM
            
            correctAccuracy = $correctAccuracy
            calculatedCharacterAccuracy = $calculatedCharacterAccuracy
            
            correctNetWPM = $correctNetWPM
            calculatedNetWPM = $calculatedNetWPM
        """.trimIndent()
        println(log)

        val passThreshold = 10
        val isWpmCloseToCorrectWPM = wpmDifference < passThreshold
        val isNetWpmCloseToCorrectWPM = netWpmDifference < passThreshold

        // wpm
        assert(isWpmCloseToCorrectWPM)

        // character accuracy
        assert(correctAccuracy == calculatedCharacterAccuracy)

        // net wpm
        assert(isNetWpmCloseToCorrectWPM)
    }
}