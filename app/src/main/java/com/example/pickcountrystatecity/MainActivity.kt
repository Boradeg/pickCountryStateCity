package com.example.pickcountrystatecity
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pickcountrystatecity.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
interface ApiService3 {
    @Headers("Content-Type: application/json")
    @POST("countries/states")
    fun getStates(@Body body: CountryRequestBody): Call<ShowStatesResponse>
}

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

    private lateinit var countrySpinner: Spinner
    private lateinit var spinner4: Spinner
    private lateinit var spinner: Spinner

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        countrySpinner = findViewById(R.id.countrySpinner)
        spinner4 = findViewById(R.id.stateSpinner)

        spinner = findViewById(R.id.spinner7)
        fetchD()
        populateStateSpinner()
        populateCountrySpinner()

    }

    private fun populateStateSpinner() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://countriesnow.space/api/v0.1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.getCities("India", "Maharashtra")
        call.enqueue(object : Callback<CityData> {
            override fun onResponse(call: Call<CityData>, response: Response<CityData>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "j", Toast.LENGTH_SHORT).show()
                    val cityData = response.body()
                    Toast.makeText(this@MainActivity, cityData?.data.toString(), Toast.LENGTH_SHORT).show()

                    cityData?.let {
                        val adapter = ArrayAdapter(
                            this@MainActivity,
                            android.R.layout.simple_spinner_item,
                            it.data
                        )

                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinner4.adapter = adapter
                    }
                } else {
                    // Handle error
                }
            }

            override fun onFailure(call: Call<CityData>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Failed to fetch data!", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }

        })
    }
    private fun fetchD() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://countriesnow.space/api/v0.1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService3 = retrofit.create(ApiService3::class.java)
        val requestBody = CountryRequestBody("India")
        val call = apiService3.getStates(requestBody)

        call.enqueue(object : Callback<ShowStatesResponse> {
            override fun onResponse(call: Call<ShowStatesResponse>, response: Response<ShowStatesResponse>) {
                if (response.isSuccessful) {
                    val showStatesResponse = response.body()
                    showStatesResponse?.let { response ->
                        val data = response.data
                        val states = data.states.map { it.name }

                        // Create an adapter to bind the data to the Spinner
                        val adapter = ArrayAdapter(
                            this@MainActivity,
                            android.R.layout.simple_spinner_item,
                            states
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinner.adapter = adapter
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Failed to fetch states data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ShowStatesResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error occurred while fetching states data", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }
        })
    }
    private fun populateCountrySpinner() {
        val countryCall = ApiClient.apiService.getCountries()
        countryCall.enqueue(object : Callback<CountryResponse> {
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
    @GET("countries/state/cities/q")
    fun getCities(
        @Query("country") country: String,
        @Query("state") state: String
    ): Call<CityData>

}

data class CountryResponse(
    val data: List<CountryData>
)

data class CountryData(
    val country: String,
    val cities: List<String>,
    val iso2: String,
    val iso3: String
)

data class CityData(
    val data: List<String>,
    val error: Boolean,
    val msg: String
)
