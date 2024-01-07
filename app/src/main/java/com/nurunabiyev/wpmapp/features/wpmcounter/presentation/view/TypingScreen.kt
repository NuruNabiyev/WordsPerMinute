package com.nurunabiyev.wpmapp.features.wpmcounter.presentation.view

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nurunabiyev.wpmapp.features.wpmcounter.domain.Analytics
import com.nurunabiyev.wpmapp.features.wpmcounter.presentation.viewmodel.TypingViewModel
import com.nurunabiyev.wpmapp.ui.theme.Pink40
import com.nurunabiyev.wpmapp.ui.theme.Typography
import com.nurunabiyev.wpmapp.ui.theme.WpmAppTheme
import kotlinx.coroutines.delay

private val vm = TypingViewModel()

@Composable
fun TypingScreen() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = { vm.reset() }) { Text(text = "Reset") }
        ReferenceParagraph()
        UserEditText()
        Stats()
    }
}

@Composable
private fun ReferenceParagraph() {
    Text(text = "Text to copy:", style = Typography.headlineSmall)
    Text(
        vm.currentReference.value,
        style = Typography.bodyLarge,
        fontSize = 15.sp,
        modifier = Modifier
            .padding(top = 8.dp)
            .background(Pink40.copy(0.1f), shape = RoundedCornerShape(8.dp))
            .padding(18.dp)
            .fillMaxWidth()
    )
}

@Composable
private fun UserEditText() {
    OutlinedTextField(
        value = vm.text,
        shape = RoundedCornerShape(8.dp),
        enabled = vm.inputEnabled,
        onValueChange = { vm.registerNewKeystroke(it) },
        keyboardOptions = KeyboardOptions(autoCorrect = false),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        label = { Text("Start typing") }
    )
}

@Composable
private fun Stats() {
    var statsAreLive by remember { mutableStateOf(false) }

    val fontWeight by remember {
        derivedStateOf {
            if (statsAreLive) FontWeight.Bold else FontWeight.Light
        }
    }

    val animatedSp by animateIntAsState(
        targetValue = if (statsAreLive) 16 else 14,
        label = "dp anim"
    )

    LaunchedEffect(key1 = fontWeight, block = {
        while (true) {
            delay(50)
            val diff = System.currentTimeMillis() - vm.analytics.lastTypeTime.value
            statsAreLive = when {
                diff < Analytics.MAX_WAIT_TIME -> true
                else -> false
            }
        }
    })

    Box(
        modifier = Modifier
            .padding(top = 8.dp)
            .background(Pink40.copy(0.1f), shape = RoundedCornerShape(8.dp))
            .padding(18.dp)
            .fillMaxWidth()
    ) {
        val wpmCount = """
                  WPM: ${vm.analytics.currentStat.value.wpm}
                  Character accuracy: ${vm.analytics.currentStat.value.wordCharacterAccuracy}%
                  Net WPM: ${vm.analytics.currentStat.value.wpmWithAccuracy}
                  """.trimIndent()
        Text(
            wpmCount,
            style = Typography.bodyMedium,
            fontWeight = fontWeight,
            fontSize = animatedSp.sp
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ParagraphPreview() {
    WpmAppTheme {
        TypingScreen()
    }
}