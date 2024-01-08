package com.nurunabiyev.wpmapp.features.wpmcounter.presentation.view

import android.content.res.Configuration
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nurunabiyev.wpmapp.core.user.domain.User
import com.nurunabiyev.wpmapp.core.utils.printLog
import com.nurunabiyev.wpmapp.features.wpmcounter.domain.Analytics
import com.nurunabiyev.wpmapp.features.wpmcounter.presentation.viewmodel.TypingViewModel
import com.nurunabiyev.wpmapp.ui.theme.Pink40
import com.nurunabiyev.wpmapp.ui.theme.Typography
import com.nurunabiyev.wpmapp.ui.theme.WpmAppTheme
import kotlinx.coroutines.delay

@Composable
fun TypingScreen(user: User, typingVM: TypingViewModel = hiltViewModel()) {
    typingVM.user = user

    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val configuration = LocalConfiguration.current
        typingVM.currentOrientation = configuration.orientation
        when (configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                Row {
                    ReferenceParagraph(Modifier.weight(2f), typingVM.currentReference.value)
                    UserEditText(Modifier.weight(1f), typingVM)
                    Stats(typingVM.analytics)
                }
            }

            else -> {
                Column {
                    Stats(typingVM.analytics)
                    ReferenceParagraph(Modifier.fillMaxWidth(1f), typingVM.currentReference.value)
                    UserEditText(Modifier.fillMaxWidth(1f), typingVM)
                }
            }
        }
    }

    DisposableEffect(key1 = Unit, effect = {
        onDispose { typingVM.reset() }
    })
}

@Composable
private fun ReferenceParagraph(modifier: Modifier, currentReference: AnnotatedString) {
    Text(
        currentReference,
        style = Typography.bodyLarge,
        fontSize = 15.sp,
        modifier = Modifier
            .padding(top = 8.dp)
            .background(Pink40.copy(0.1f), shape = RoundedCornerShape(8.dp))
            .padding(18.dp)
            .then(modifier)
    )
}

@Composable
private fun UserEditText(modifier: Modifier, vm: TypingViewModel) {
    OutlinedTextField(
        value = vm.text,
        shape = RoundedCornerShape(8.dp),
        enabled = vm.inputEnabled,
        onValueChange = {
            vm.registerNewKeystroke(it)
        },
        keyboardOptions = KeyboardOptions(autoCorrect = false),
        modifier = Modifier
            .padding(top = 8.dp)
            .then(modifier),
        label = { Text("Start typing") }
    )
}

@Composable
private fun Stats(analytics: Analytics) {
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
            runCatching {
                val diff = System.currentTimeMillis() - analytics.lastTypeTime.value
                statsAreLive = when {
                    diff < Analytics.MAX_WAIT_TIME -> true
                    else -> false
                }
            }
        }
    })

    Box(
        modifier = Modifier
            .padding(top = 8.dp)
            .background(Pink40.copy(0.1f), shape = RoundedCornerShape(8.dp))
            .padding(18.dp)
    ) {
        val wpmCount = """
                  WPM: ${analytics.currentStat.value.wpm}
                  Character accuracy: ${analytics.currentStat.value.wordCharacterAccuracy}%
                  Net WPM: ${analytics.currentStat.value.wpmWithAccuracy}
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
        TypingScreen(User(-1, "default"))
    }
}