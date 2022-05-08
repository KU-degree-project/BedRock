package com.bed.android.bedrock.vision

import android.os.Bundle
import android.util.Log
import android.view.View
import com.bed.android.bedrock.R
import com.bed.android.bedrock.databinding.FragmentCloudVisionBinding
import com.bed.android.bedrock.model.vision.GcsSource
import com.bed.android.bedrock.model.vision.InputConfig
import com.bed.android.bedrock.model.vision.ProductSetDTO
import com.bed.android.bedrock.network.ApiService
import com.bed.android.bedrock.ui.BaseFragment
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.storage.StorageOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class CloudVisionFragment : BaseFragment<FragmentCloudVisionBinding>(R.layout.fragment_cloud_vision) {

    private val client = OkHttpClient
        .Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build()
    private val retrofit = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(ApiService::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authExplicit()
    }

    private fun authExplicit() {
        val assetManager = resources.assets

        val source = assetManager.open(JSON_NAME)

        val credentials = GoogleCredentials
            .fromStream(source)
            .createScoped(listOf("https://www.googleapis.com/auth/cloud-platform"))

        val storage = StorageOptions.newBuilder().setCredentials(credentials).build().service

        CoroutineScope(Dispatchers.IO).launch {
            val buckets = storage.list()

            buckets.values.forEach {
                println(it)
            }

            val dto = ProductSetDTO(InputConfig(GcsSource(CSV_URL)))
            val response = service.makeProductSet(dto)

            Log.d(TAG, "authExplicit: $response")
        }
    }


    companion object {

        private const val TAG = "CloudVisionFragment"
        private const val JSON_NAME = "bedrock-349504-e4b5f96ae15f.json"
        private const val BASE_URL = "https://vision.googleapis.com/"
        private const val CSV_URL = "https://raw.githubusercontent.com/williamtsoi1/vision-api-product-search-demo/main/data/products_0.csv"

        fun newInstance() = CloudVisionFragment()
    }
}