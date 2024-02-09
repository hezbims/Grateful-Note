package com.example.gratefulnote.common.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DefaultErrorDisplay(
    exception : Throwable?,
    onRefresh : () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Icon(
            Icons.Filled.ErrorOutline,
            contentDescription = "Error",
            modifier = Modifier
                .size(64.dp)
        )

        Text(
            textAlign = TextAlign.Center,
            text = exception?.message ?: "Unknown Error",
        )

        ElevatedButton(onClick = onRefresh) {
            Text(text = "Retry")
        }
    }
}

@Preview
@Composable
private fun PreviewDefaultErrorDisplay(){
    Surface {
        DefaultErrorDisplay(
            exception = IllegalArgumentException("Error, parameter tidak valid"),
            onRefresh = {},
            modifier = Modifier.padding(24.dp)
        )
    }
}