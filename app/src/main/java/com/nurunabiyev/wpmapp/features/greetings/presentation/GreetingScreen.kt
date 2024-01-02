package com.nurunabiyev.wpmapp.features.greetings.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nurunabiyev.wpmapp.ui.theme.Typography
import com.nurunabiyev.wpmapp.ui.theme.WpmAppTheme

@Composable
fun GreetingScreen() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        val intro = "Welcome to Word Per Minute Counter!"
        Text(intro, style = Typography.titleLarge)
        val intro2 = "Enter your username to keep records."
        Text(intro2, style = Typography.bodyLarge)

        var text by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue("", TextRange(0, 7)))
        }

        TextField(
            value = text,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            onValueChange = { text = it },
            label = { Text("Username") }
        )

        Button(
            modifier = Modifier.padding(top = 16.dp).align(Alignment.End),
            onClick = {
                /* Do something! */
            }
        ) {
            Text("Start the test")
            Icon(
                modifier = Modifier.padding(start = 12.dp).size(18.dp),
                imageVector = Icons.Outlined.ArrowForward,
                contentDescription = "Next"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WpmAppTheme {
        GreetingScreen()
    }
}