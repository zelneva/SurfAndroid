package dev.android.restaurants.activity.model

import com.google.gson.annotations.SerializedName

class Rating {
    @SerializedName("aggregate_rating")
    var rating: String = ""

    @SerializedName("rating_text")
    var text: String = ""
}