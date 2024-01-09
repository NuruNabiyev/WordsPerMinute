@file:OptIn(ExperimentalMaterial3Api::class)

package com.nurunabiyev.wpmapp.features.wpmcounter.presentation.view

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.nurunabiyev.wpmapp.core.user.domain.User
import com.nurunabiyev.wpmapp.features.wpmcounter.domain.Analytics
import com.nurunabiyev.wpmapp.features.wpmcounter.presentation.viewmodel.TypingViewModel
import com.nurunabiyev.wpmapp.ui.theme.Pink40
import com.nurunabiyev.wpmapp.ui.theme.Typography
import com.nurunabiyev.wpmapp.ui.theme.WpmAppTheme
import kotlinx.coroutines.delay

@Composable
fun TypingScreen(
    navHostController: NavHostController,
    user: User,
    typingVM: TypingViewModel = hiltViewModel()
) {
    typingVM.user = user
    val scrollState = rememberScrollState()

    Box(
        Modifier
            .fillMaxSize()
            .padding(12.dp)
            .verticalScroll(scrollState)) {
        val configuration = LocalConfiguration.current
        typingVM.currentOrientation = configuration.orientation
        when (configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                Row {
                    ReferenceParagraph(Modifier.weight(1f), typingVM.currentReference.value)
                    UserEditText(Modifier.weight(1f), typingVM)
                    Stats(typingVM.analytics)
                }
            }

            else -> {
                Scaffold(topBar = {
                    TopAppBar(title = {
                        Text(user.username)
                    }, navigationIcon = {
                        Icon(imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { navHostController.navigateUp() })
                    })
                }, content = {
                    Column(Modifier.padding(it)) {
                        Stats(typingVM.analytics)
                        ReferenceParagraph(Modifier.fillMaxWidth(1f), typingVM.currentReference.value)
                        UserEditText(Modifier.fillMaxWidth(1f), typingVM)
                    }
                })
            }
        }
    }

    BackHandler {
        navHostController.navigateUp()
    }
}

@Composable
private fun ReferenceParagraph(modifier: Modifier, currentReference: AnnotatedString) {
    val textSize = when (LocalConfiguration.current.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> 12.sp
        Configuration.ORIENTATION_PORTRAIT -> 15.sp
        else -> 14.sp
    }
    Text(
        currentReference,
        style = Typography.bodyLarge,
        fontSize = textSize,
        modifier = Modifier
            .padding(top = 8.dp)
            .background(Pink40.copy(0.1f), shape = RoundedCornerShape(8.dp))
            .padding(12.dp)
            .then(modifier)
    )
}

@Composable
private fun UserEditText(modifier: Modifier, vm: TypingViewModel) {
    val textSize = when (LocalConfiguration.current.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> 12.sp
        Configuration.ORIENTATION_PORTRAIT -> 15.sp
        else -> 14.sp
    }

    OutlinedTextField(
        value = vm.text,
        shape = RoundedCornerShape(8.dp),
        textStyle = TextStyle.Default.copy(fontSize = textSize),
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
            .padding(12.dp)
    ) {
        val stats = analytics.currentStat.value

        val statsCount = when (LocalConfiguration.current.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> "WPM: ${stats.wpm} \t " +
                    "Net WPM: ${stats.wpmWithAccuracy} \t " +
                    "Accuracy: ${stats.wordCharacterAccuracy}%"

            else -> {
                "WPM: ${stats.wpm} \n" +
                        "Net WPM: ${stats.wpmWithAccuracy} \n" +
                        "Accuracy: ${stats.wordCharacterAccuracy}%"
            }
        }

        Text(
            statsCount,
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
        //TypingScreen(User(-1, "default"))
    }
}