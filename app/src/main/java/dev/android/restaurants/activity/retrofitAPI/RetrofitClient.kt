package dev.android.restaurants.activity.retrofitAPI

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitClient {

    val ZOMATO_API_KEY = "ce0601dd2bddc0e5062754af9b061700"
    val baseUrl = "https://developers.zomato.com/"

    fun getClient(): ZomatoAPI {
        return provideZomatoServiceApi()
    }

    private fun provideZomatoServiceApi(): ZomatoAPI {
        return provideRetrofit().create(ZomatoAPI::class.java)
    }


    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(provideOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }


    fun provideOkHttpClient(): OkHttpClient {

        return OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val original = chain.request()
                    val request = original!!.newBuilder()
                            .addHeader("user-key", ZOMATO_API_KEY)
                            .build()

                    return@addInterceptor chain.proceed(request)
                }
                .addInterceptor(provideHttpLoggingInterceptor())
                .build()
    }


    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return loggingInterceptor
    }
}
