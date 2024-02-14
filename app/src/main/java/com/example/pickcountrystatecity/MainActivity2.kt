
package com.example.pickcountrystatecity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pickcountrystatecity.ApiService
import com.example.pickcountrystatecity.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

import android.widget.Spinner

data class State(
    val name: String,
    val state_code: String
)


class MainActivity2 : AppCompatActivity() {

    private lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        spinner = findViewById(R.id.spinner6)
        fetchD()

    }

    private fun fetchD() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://countriesnow.space/api/v0.1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService3::class.java)
        val requestBody = CountryRequestBody("India")
        val call = apiService.getStates(requestBody)

        call.enqueue(object : Callback<ShowStatesResponse> {
            override fun onResponse(call: Call<ShowStatesResponse>, response: Response<ShowStatesResponse>) {
                if (response.isSuccessful) {
                    val showStatesResponse = response.body()
                    showStatesResponse?.let { response ->
                        val data = response.data
                        val states = data.states.map { it.name }

                        // Create an adapter to bind the data to the Spinner
                        val adapter = ArrayAdapter(
                            this@MainActivity2,
                            android.R.layout.simple_spinner_item,
                            states
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinner.adapter = adapter
                    }
                } else {
                    Toast.makeText(this@MainActivity2, "Failed to fetch states data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ShowStatesResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity2, "Error occurred while fetching states data", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }
        })
    }
}
data class CountryRequestBody(
    val country: String
)

data class Data(
    val iso2: String,
    val iso3: String,
    val name: String,
    val states: List<State>
)

data class ShowStatesResponse(
    val data: Data,
    val error: Boolean,
    val msg: String
)
