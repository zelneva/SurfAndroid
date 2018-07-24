package dev.android.restaurants.activity

import android.app.Application
import dev.android.restaurants.activity.retrofitAPI.RetrofitClient
import dev.android.restaurants.activity.retrofitAPI.ZomatoAPI

class App : Application() {

    companion object {
        var zomatoServiceApi: ZomatoAPI = RetrofitClient().getClient()
    }


    override fun onCreate() {
        super.onCreate()
    }
}