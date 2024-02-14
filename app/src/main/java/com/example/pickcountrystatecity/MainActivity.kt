package com.example.pickcountrystatecity

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.http.GET

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Callback
import retrofit2.Response
object ApiClient {
    private const val BASE_URL = "https://countriesnow.space/api/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val countrySpinner: Spinner = findViewById(R.id.countrySpinner)
        val call = ApiClient.apiService.getCountries()
        call.enqueue(object : Callback<CountryResponse> {
            override fun onResponse(call: Call<CountryResponse>, response: Response<CountryResponse>) {
                if (response.isSuccessful) {
                    val countryResponse = response.body()
                    val countries = countryResponse?.data?.map { it.country }
                    countries?.let {
                        val adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, it)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        countrySpinner.adapter = adapter
                    }
                } else {
                    // Handle error
                }
            }

            override fun onFailure(call: Call<CountryResponse>, t: Throwable) {
                // Handle failure
            }
        })

    }

}

interface ApiService {
    @GET("v0.1/countries")
    fun getCountries(): Call<CountryResponse>
}

