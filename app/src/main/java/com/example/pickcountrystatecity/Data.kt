package com.example.pickcountrystatecity

data class Data(
    val cities: List<String>,
    val country: String,
    val iso2: String,
    val iso3: String
)

data class CountryResponse(
    val data: List<Data>,
    val error: Boolean,
    val msg: String
)
