package com.example.gratefulnote.common.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.gratefulnote.common.domain.ResponseWrapper

@Composable
fun <T>ResponseWrapperLoader(
    response : ResponseWrapper<T>,
    onRetry : () -> Unit,
    content : @Composable (T?) -> Unit,
    modifier: Modifier = Modifier,
){
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier,
    ) {
        when (response){
            is ResponseWrapper.Loading ->
                Column {
                    CircularProgressIndicator()
                    if (response.message != null)
                        Text(
                            textAlign = TextAlign.Center,
                            text = response.message
                        )
                }
            is ResponseWrapper.Error ->
                DefaultErrorDisplay(
                    exception = response.exception,
                    onRefresh = onRetry
                )

            is ResponseWrapper.Succeed -> {
                content(response.data)
            }
        }
    }
}

@Preview
@Composable
private fun PreviewLoading(){
    Surface {
        ResponseWrapperLoader<String>(
            response = ResponseWrapper.Loading(),
            onRetry = {  },
            content = { _  -> }
        )
    }
}