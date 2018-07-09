package dev.android.restaurants.activity.RetrofitAPI

import dev.android.restaurants.activity.response.CityResponse
import dev.android.restaurants.activity.response.RestaurantResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ZomatoAPI {

    @GET("/api/v2.1/cities")
    fun getCity(
            @Query("q") searchText: String,
            @Query("count") count: Int): Observable<CityResponse>


    @GET("api/v2.1/search")
    fun getListRestaurant(
            @Query("entity_id") city_id: Int,
            @Query("entity_type") type: String = "city",
            @Query("count") count:Int = 20): Observable<RestaurantResponse>


}