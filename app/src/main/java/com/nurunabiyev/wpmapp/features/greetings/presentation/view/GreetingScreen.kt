package com.nurunabiyev.wpmapp.features.greetings.presentation.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nurunabiyev.wpmapp.core.user.domain.User
import com.nurunabiyev.wpmapp.features.greetings.presentation.viewmodel.GreetingsViewModel
import com.nurunabiyev.wpmapp.ui.theme.Typography
import com.nurunabiyev.wpmapp.ui.theme.WpmAppTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun GreetingScreen(
    onUserRegistered: (User) -> Unit,
    greetingVM: GreetingsViewModel = hiltViewModel()
) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        LaunchedEffect(Unit) {
            greetingVM.registrationCompleted.collectLatest {
                onUserRegistered(it)
            }
        }

        Intro()
        UsernameEditText(greetingVM.usernameText, greetingVM.error, greetingVM::resetError)
        RegisterButton(greetingVM::registerUser)
    }
}

@Composable
private fun Intro() {
    val intro = "Welcome to Word Per Minute Counter!"
    Text(intro, style = Typography.titleLarge)
    val intro2 = "Enter your username to keep the records."
    Text(intro2, style = Typography.bodyLarge)
}

@Composable
private fun UsernameEditText(
    username: MutableState<String>,
    error: MutableState<String>,
    resetError: () -> Unit
) {
    val isError = remember { derivedStateOf { error.value.isNotEmpty() } }
    val focusRequester = remember { FocusRequester() }

    TextField(
        value = username.value,
        singleLine = true,
        isError = isError.value,
        supportingText = {
            if (isError.value) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Error: ${error.value}",
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .padding(top = 16.dp),
        onValueChange = {
            resetError()
            username.value = it
        },
        label = { Text("Username") }
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
private fun ColumnScope.RegisterButton(registerUser: () -> Unit) {
    Button(
        modifier = Modifier.align(Alignment.End),
        onClick = { registerUser() }
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
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WpmAppTheme {
        //GreetingScreen {}
    }
}