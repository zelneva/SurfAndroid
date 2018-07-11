package dev.android.restaurants.activity.retrofitAPI

import dev.android.restaurants.activity.model.RestaurantDetails
import dev.android.restaurants.activity.response.CityResponse
import dev.android.restaurants.activity.response.RestaurantResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ZomatoAPI {

    @GET("/api/v2.1/cities")
    fun getCity(
            @Query("q") searchText: String): Call<CityResponse>


    @GET("api/v2.1/search")
    fun getListRestaurant(
            @Query("entity_id") city_id: Int,
            @Query("entity_type") type: String = "city"): Call<RestaurantResponse>


    @GET("api/v2.1/restaurant")
    fun getDetailsRestaurant(
            @Query("res_id") rs_id: Int): Call<RestaurantDetails>
}