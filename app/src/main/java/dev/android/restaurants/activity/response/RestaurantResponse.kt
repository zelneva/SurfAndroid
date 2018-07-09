package dev.android.restaurants.activity.response

import com.google.gson.annotations.SerializedName
import dev.android.restaurants.activity.model.Restaurant
import dev.android.restaurants.activity.model.RestaurantItem

class RestaurantResponse {
    @SerializedName("restaurants")
    var restaurants: List<RestaurantItem> = emptyList()
}