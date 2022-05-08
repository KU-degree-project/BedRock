package com.bed.android.bedrock.network

import com.bed.android.bedrock.model.vision.ProductSetDTO
import com.bed.android.bedrock.model.vision.ProductSetResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @Headers("Content-Type: application/json; charset=utf-8",
        "Authorization: Bearer $ACCESS_TOKEN")
    @POST("v1/projects/${PROJECT_ID}/locations/asia-east1/productSets:import")
    suspend fun makeProductSet(@Body productSetDTO: ProductSetDTO) : Response<ProductSetResponse>


    companion object {

        private const val PROJECT_ID = "bedrock-349504"
        private const val ACCESS_TOKEN = "ya29.A0ARrdaM_u7ngLmejjfQ3KksnJe3lygwXvoFvULm7T1MkFTZoVHCj0q8szQBiySE4rgNW4J681qz26N1yho3Napc3o04kzACcmsyI_xISRrQ9XZpqN3DyolFNusntZ_4x67DCkPfzSWQ-9oPzfij5WUhk05vX3kdgKQQizvB6SZNzlyTHPNpaw1VvKGNAqBRf4rHiDG97Bb9Oawy1dsTglJwUHAMFxeKVx7umYEREs95NdQJkPe7Hh0x3e393YYDfmfP4o-48"

    }

}