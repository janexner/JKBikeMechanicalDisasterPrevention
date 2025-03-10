package com.exner.tools.jkbikemechanicaldisasterprevention.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface StravaApiService {
    @POST("/oauth/token")
    fun postOAuthTokenRequest(@Body body: Map<String, String>): Call<ResponseBody>

    @POST
    fun postArtistInformationRequest(): Call<ResponseBody>
}

// java -jar modules\swagger-codegen-cli\target\swagger-codegen-cli.jar generate -i https://developers.strava.com/swagger/swagger.json -l kotlin -o generated\kotlin

