package com.example.myapplication.ui

import RefreshTokenResponse
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.databinding.TwitchLoginBinding
import com.example.myapplication.ui.TwitchRepository.Companion.twitchService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import timber.log.Timber


class TwitchActivity : ComponentActivity() {
    lateinit var binding: TwitchLoginBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TwitchLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.webView.loadUrl(twitchLoginUrl)
        binding.webView.settings.javaScriptEnabled = true;

        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                request?.let {
                    // Check if this url is our OAuth redirectUrl, otherwise ignore it
                    if (request.url.toString().contains(redirectUri)) {
                        // To prevent CSRF attacks, check that we got the same state value we sent, otherwise ignore it
                        val authData = getAuthData(request.url)
                        Timber.d("AuthData: $authData")
                    }
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
    }

    fun getAuthData(uri: Uri): AuthData {
        val params: List<String> = uri.fragment?.split("&") ?: return AuthData("", "")
        var accessToken = ""
        var uniqueState = ""
        for (param in params) {
            if (param.startsWith("access_token=")) {
                accessToken = param.substring("access_token=".length)
            }
            if (param.startsWith("state=")) {
                uniqueState = param.substring("state=".length)
            }
        }
        return AuthData(accessToken, uniqueState)
    }

    data class AuthData(
        val accessToken: String,
        val uniqueState: String
    )

    companion object {
        val clientId = ""
        val clientSecret = ""
        const val redirectUri = "http://localhost:3000"
        private const val uniqueState = "c3ab8aa609ea11e793ae92361f002671"
        private const val twitchLoginUrl =
            "https://id.twitch.tv/oauth2/authorize?response_type=token&client_id=&redirect_uri=http://localhost:3000&scope=channel%3Amanage%3Apolls+channel%3Aread%3Apolls&state=$uniqueState"
    }
}


interface TwitchApi {
    @FormUrlEncoded
    @POST("oauth2/token")
    suspend fun getRefreshToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String,
        @Field("grant_type") grantType: String,
        @Field("redirect_uri") redirectUri: String
    ): Response<RefreshTokenResponse>
}

class TwitchRepository {
    companion object {
        fun buildLogger(): HttpLoggingInterceptor {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BODY
            return logger
        }

        private val client = OkHttpClient.Builder()
            .addNetworkInterceptor(buildLogger())
            .build()

        private val contentType = "application/json".toMediaType()
        private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://id.twitch.tv/")
            .client(client)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()

        val twitchService = retrofit.create(TwitchApi::class.java)
    }
}
