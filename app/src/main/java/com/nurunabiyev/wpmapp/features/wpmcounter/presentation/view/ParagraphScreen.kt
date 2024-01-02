package com.nurunabiyev.wpmapp.features.wpmcounter.presentation.view

import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nurunabiyev.wpmapp.ui.theme.Pink40
import com.nurunabiyev.wpmapp.ui.theme.PurpleGrey40
import com.nurunabiyev.wpmapp.ui.theme.Typography
import com.nurunabiyev.wpmapp.ui.theme.WpmAppTheme

@Composable
fun ParagraphScreen() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Text to copy:", style = Typography.headlineSmall)
        val intro = """
            He thought he would light the fire when
            he got inside, and make himself some
            breakfast, just to pass away the time;
            but he did not seem able to handle anything
            from a scuttleful of coals to a
        """.trimIndent()
        Text(
            intro,
            style = Typography.titleMedium,
            modifier = Modifier
                .padding(top = 8.dp)
                .background(Pink40.copy(0.1f), shape = RoundedCornerShape(8.dp))
                .padding(18.dp)
                .fillMaxWidth()
        )

        var text by rememberSaveable { mutableStateOf("") }

        OutlinedTextField(
            value = text,
            shape = RoundedCornerShape(8.dp),
            onValueChange = { text = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            label = { Text("Type the text above to start counting") }
        )

        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .background(Pink40.copy(0.1f), shape = RoundedCornerShape(8.dp))
                .padding(18.dp)
                .fillMaxWidth()
        ) {
            val wpmCount = """
                  Your stats: 123
                  """.trimIndent()
            Text(
                wpmCount,
                style = Typography.bodyMedium,

                )
        }

    }
}


@Preview(showBackground = true)
@Composable
fun ParagraphPreview() {
    WpmAppTheme {
        ParagraphScreen()
    }
}