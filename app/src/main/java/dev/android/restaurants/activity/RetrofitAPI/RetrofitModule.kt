package dev.android.restaurants.activity.RetrofitAPI

import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class RetrofitModule() {

    val ZOMATO_API_KEY = "ce0601dd2bddc0e5062754af9b061700"
    val baseUrl = "https://developers.zomato.com/"

    @Provides
    @Singleton
    fun provideZomatoServiceApi(): ZomatoAPI {
        return provideRetrofit().create(ZomatoAPI::class.java)
    }


    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(provideOkHttpClient())
                .addConverterFactory(provideGsonConverterFactory())
                .addCallAdapterFactory(provideRxCallAdapterFactory())
                .build()
    }


    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }


    @Provides
    @Singleton
    fun provideRxCallAdapterFactory(): RxJava2CallAdapterFactory {
        return RxJava2CallAdapterFactory.create()
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {

        return OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val original = chain?.request();
                    val request = original!!.newBuilder()
                            .addHeader("user-key", ZOMATO_API_KEY)
                            .build()

                    chain.proceed(request);
                }
                .addInterceptor(provideHttpLoggingInterceptor())
                .build()
    }


    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return loggingInterceptor
    }
}
