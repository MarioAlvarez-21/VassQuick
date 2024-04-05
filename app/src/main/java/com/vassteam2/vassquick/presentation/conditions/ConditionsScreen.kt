package com.vassteam2.vassquick.presentation.conditions

import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.vassteam2.vassquick.domain.constants.URL_CONDITIONS


@Composable
fun ConditionsScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient()
                loadUrl(URL_CONDITIONS)

                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        return true
                    }
                }
            }
        }, modifier = Modifier.fillMaxSize())
    }

}