package dev.android.restaurants.activity.RetrofitAPI

import dev.android.restaurants.activity.response.CityResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ZomatoAPI {

    @GET("/api/v2.1/cities")
    fun getCity(
            @Query("q") searchText: String,
            @Query("count") count: Int): Observable<CityResponse>

}