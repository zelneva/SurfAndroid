package dev.android.restaurants.activity.model

import com.google.gson.annotations.SerializedName

class Location {
    @SerializedName("address")
    var address: String = ""

    @SerializedName("city")
    var city: String = ""

}