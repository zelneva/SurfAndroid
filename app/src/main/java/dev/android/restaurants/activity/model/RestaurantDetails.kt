package dev.android.restaurants.activity.model

data class RestaurantDetails(
        var id: Int,
        var name: String,
        var location: Location,
        var featured_image: String,
        var cuisines: String,
        var average_cost_for_two: String,
        var user_rating: Rating,
        var url: String
)