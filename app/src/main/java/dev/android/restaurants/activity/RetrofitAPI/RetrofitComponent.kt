package dev.android.restaurants.activity.RetrofitAPI

import javax.inject.Singleton

import dagger.Component
import dev.android.restaurants.activity.MainActivity
import dev.android.restaurants.activity.RestaurantListActivity

@Component(modules = arrayOf(RetrofitModule::class))
@Singleton
interface RetrofitComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(restaurantListActivity: RestaurantListActivity)
}
