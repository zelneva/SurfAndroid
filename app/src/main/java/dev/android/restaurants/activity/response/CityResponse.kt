package dev.android.restaurants.activity.response

import com.google.gson.annotations.SerializedName
import dev.android.restaurants.activity.model.City

class CityResponse {
    @SerializedName("location_suggestions")
    var cities: List<City> = emptyList()
}