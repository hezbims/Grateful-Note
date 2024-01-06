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
import com.example.gratefulnote.common.data.ResponseWrapper

@Composable
inline fun <reified T>ResponseWrapperLoader(
    response : ResponseWrapper,
    noinline onRetry : () -> Unit,
    content : @Composable (T?) -> Unit,
    modifier: Modifier = Modifier,
){
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier,
    ) {
        when (response){
            is ResponseWrapper.ResponseLoading ->
                Column {
                    CircularProgressIndicator()
                    if (response.message != null)
                        Text(
                            textAlign = TextAlign.Center,
                            text = response.message
                        )
                }
            is ResponseWrapper.ResponseError ->
                DefaultErrorDisplay(
                    exception = response.exception,
                    onRefresh = onRetry
                )

            is ResponseWrapper.ResponseSucceed<*> -> {
                if (response.data is T?){
                    content(response.data)
                }
                else
                    throw IllegalArgumentException("Tipe data tidak diketahui pada response wrapper")
            }
        }
    }
}

@Preview
@Composable
private fun PreviewLoading(){
    Surface {
        ResponseWrapperLoader<String>(
            response = ResponseWrapper.ResponseLoading(),
            onRetry = {  },
            content = { _  -> }
        )
    }
}