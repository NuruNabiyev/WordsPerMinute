package com.nurunabiyev.wpmapp.features.greetings.presentation.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nurunabiyev.wpmapp.core.user.domain.User
import com.nurunabiyev.wpmapp.features.greetings.presentation.viewmodel.GreetingsViewModel
import com.nurunabiyev.wpmapp.ui.theme.Typography
import com.nurunabiyev.wpmapp.ui.theme.WpmAppTheme

@Composable
fun GreetingScreen(
    onUserRegistered: (User) -> Unit,
    greetingViewModel: GreetingsViewModel = viewModel()
) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        greetingViewModel.registrationCompleted?.let(onUserRegistered)

        val intro = "Welcome to Word Per Minute Counter!"
        Text(intro, style = Typography.titleLarge)
        val intro2 = "Enter your username to keep records."
        Text(intro2, style = Typography.bodyLarge)

        val isError = remember { derivedStateOf { greetingViewModel.error.isNotEmpty() } }
        var text by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue("", TextRange(0, 7)))
        }
        val focusRequester = remember { FocusRequester() }

        TextField(
            value = text,
            singleLine = true,
            isError = isError.value,
            supportingText = {
                if (isError.value) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Error: ${greetingViewModel.error}",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .padding(top = 16.dp),
            onValueChange = {
                greetingViewModel.resetError()
                text = it
            },
            label = { Text("Username") }
        )

        Button(
            modifier = Modifier
                .padding(top = 16.dp)
                .align(Alignment.End),
            onClick = {
                greetingViewModel.registerUser(text.text)
            }
        ) {
            Text("Start the test")
            Icon(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(18.dp),
                imageVector = Icons.Outlined.ArrowForward,
                contentDescription = "Next"
            )
        }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WpmAppTheme {
        //GreetingScreen {}
    }
}