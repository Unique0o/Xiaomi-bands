package com.example.logifitappp.viewmodel.views.termsConditions

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TermsAndConditionsViewModel : ViewModel() {
    private val _url = MutableLiveData("https://logifit.app/api/terminos_condiciones")
    val url: LiveData<String> get() = _url

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> get() = _isLoading

    @SuppressLint("SetJavaScriptEnabled")
    fun getWebViewSettings(webView: WebView, backgroundColor: Int)  {
        webView.apply {
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    _isLoading.value = true
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    _isLoading.value = false
                }
            }
            settings.javaScriptEnabled = true
            setBackgroundColor(backgroundColor)
        }
    }
}
