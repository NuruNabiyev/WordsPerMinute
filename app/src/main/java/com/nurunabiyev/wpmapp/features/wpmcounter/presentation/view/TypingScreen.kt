package com.nurunabiyev.wpmapp.features.wpmcounter.presentation.view

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nurunabiyev.wpmapp.core.utils.printLog
import com.nurunabiyev.wpmapp.features.wpmcounter.presentation.viewmodel.TypingViewModel
import com.nurunabiyev.wpmapp.ui.theme.Pink40
import com.nurunabiyev.wpmapp.ui.theme.Typography
import com.nurunabiyev.wpmapp.ui.theme.WpmAppTheme

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
    Box(
        modifier = Modifier
            .padding(top = 8.dp)
            .background(Pink40.copy(0.1f), shape = RoundedCornerShape(8.dp))
            .padding(18.dp)
            .fillMaxWidth()
    ) {
        val wpmCount = """
                  WPM: ${vm.analytics.currentStat.value.wpm}
                  Accuracy: 49
                  Mistakes 28
                  AdjustedWPM: 35
                  """.trimIndent()
        Text(
            wpmCount,
            style = Typography.bodyMedium,

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