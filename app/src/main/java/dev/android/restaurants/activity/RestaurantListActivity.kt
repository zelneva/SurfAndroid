package dev.android.restaurants.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import dev.android.restaurants.R
import dev.android.restaurants.activity.RetrofitAPI.DaggerRetrofitComponent
import dev.android.restaurants.activity.RetrofitAPI.RetrofitModule
import dev.android.restaurants.activity.RetrofitAPI.ZomatoAPI
import dev.android.restaurants.activity.adapter.RestaurantListAdapter
import dev.android.restaurants.activity.model.Restaurant
import dev.android.restaurants.activity.response.RestaurantResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.restaurant_list_activity.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import javax.inject.Inject
import android.content.Context
import android.app.SearchManager
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener


class RestaurantListActivity : AppCompatActivity(), AnkoLogger {

    var restaurantArray: ArrayList<Restaurant > = ArrayList()
    lateinit var adapter: RestaurantListAdapter

    @Inject
    lateinit var zomatoServiceApi: ZomatoAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.restaurant_list_activity)

        val intent = intent
        val cityId = intent.getIntExtra("cityId", 0)
        restaurant_rv.layoutManager = LinearLayoutManager(this)

        loadListRestaurant(cityId)

        adapter = RestaurantListAdapter(this, restaurantArray)
        restaurant_rv.adapter = adapter
    }

    fun loadListRestaurant(cityId: Int) {
        var restaurantResponse: RestaurantResponse? = null

        DaggerRetrofitComponent.builder()
                .retrofitModule(RetrofitModule())
                .build()
                .inject(this)

        zomatoServiceApi.getListRestaurant(cityId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<RestaurantResponse>() {
                    override fun onNext(restaurantRes: RestaurantResponse) {
                        restaurantResponse = restaurantRes
                        info("RESTAURANT SIZE!!!!" + restaurantRes.restaurants.size)
                    }

                    override fun onError(e: Throwable) {
                        finish()
                    }

                    override fun onComplete() {
                        if (restaurantResponse != null) {
                            addListRestaurant(restaurantResponse!!)
                            adapter.invalidate()
                        }
                    }
                })
    }

    fun addListRestaurant(restaurantResponse: RestaurantResponse) {
        for (item in restaurantResponse.restaurants) {
            restaurantArray.add(Restaurant(item.restaurant.id, item.restaurant.name, item.restaurant.url,
                    item.restaurant.location, item.restaurant.cuisines, item.restaurant.rating, item.restaurant.photoURL))
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_search, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setIconifiedByDefault(false)

        searchView.setOnQueryTextListener(object : OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.getFilter().filter(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                adapter.getFilter().filter(query)
                return false
            }

        })
        return true
    }
}