package dev.android.restaurants.activity.model

import com.google.gson.annotations.SerializedName


data class Restaurant (
    @SerializedName("id")
    var id: Int,

    @SerializedName("name")
    var name: String,

    @SerializedName("url")
    var url: String,

    @SerializedName("location")
    var location: Location,

    @SerializedName("cuisines")
    var cuisines: String,

    @SerializedName("user_rating")
    var rating: Rating,

    @SerializedName("thumb")
    var photoURL: String)


