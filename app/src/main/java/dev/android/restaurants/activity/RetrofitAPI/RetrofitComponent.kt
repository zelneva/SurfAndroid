package dev.android.restaurants.activity.RetrofitAPI

import javax.inject.Singleton

import dagger.Component
import dev.android.restaurants.activity.MainActivity

@Component(modules = arrayOf(RetrofitModule::class))
@Singleton
interface RetrofitComponent {
    fun inject(mainActivity: MainActivity)
}
