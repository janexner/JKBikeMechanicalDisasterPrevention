package com.exner.tools.jkbikemechanicaldisasterprevention.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exner.tools.jkbikemechanicaldisasterprevention.BuildConfig
import com.exner.tools.jkbikemechanicaldisasterprevention.network.StravaApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

@HiltViewModel
class StravaAuthResultViewModel @Inject constructor(

) : ViewModel() {

    private val _isAuthenticationInitiated: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isAuthenticationInitiated: StateFlow<Boolean> = _isAuthenticationInitiated

    private val _isAuthenticated: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    fun authenticateWithCode(code: String) {
        if (code.isNotBlank()) {
            viewModelScope.launch {
                if (!isAuthenticationInitiated.value) {
                    _isAuthenticationInitiated.value = true
                    val retrofit = Retrofit.Builder()
                        .baseUrl("https://www.strava.com/")
                        .addConverterFactory(MoshiConverterFactory.create())
                        .build()
                    val apiService = retrofit.create(StravaApiService::class.java)
                    val body = mapOf(
                        "client_id" to BuildConfig.STRAVA_CLIENT_ID,
                        "client_secret" to BuildConfig.STRAVA_CLIENT_SECRET,
                        "code" to code,
                        "grant_type" to "authorization_code"
                    )
                    Log.d("SARVM", "Request POST body: $body")
                    apiService.postOAuthTokenRequest(body).enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            // handle the response
                            try {
                                // TODO
                                Log.d("SARVM", "onResponse: $response")
                                val responseCode = response.code()
                                val responseHeaders = response.headers()
                                val responseBody = response.body()
                                Log.d("SARVM", "  code: $responseCode")
                                Log.d("SARVM", "  headers: $responseHeaders")
                                Log.d("SARVM", "  body: $responseBody")
                                val rb1 = responseBody?.contentLength()
                                Log.d("SARVM", "  body length: $rb1")
                                if (responseCode == 200) {
                                    _isAuthenticated.value = true
                                }
                            } finally {
                                response.body()?.close()
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            // handle the failure
                        }
                    })
                }
            }
        }
    }

    fun retrieveBikesFromStrava() {
        viewModelScope.launch {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://www.strava.com/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
            val apiService = retrofit.create(StravaApiService::class.java)

        }
    }
}