package dev.android.restaurants.activity

import android.app.ProgressDialog
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import dev.android.restaurants.R
import dev.android.restaurants.activity.App.Companion.zomatoServiceApi
import dev.android.restaurants.activity.adapter.RestaurantListAdapter
import dev.android.restaurants.activity.model.Restaurant
import dev.android.restaurants.activity.response.RestaurantResponse
import dev.android.restaurants.activity.retrofitAPI.RetrofitClient
import dev.android.restaurants.activity.retrofitAPI.ZomatoAPI
import kotlinx.android.synthetic.main.activity_restaurant_list.*
import org.jetbrains.anko.AnkoLogger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RestaurantListActivity : AppCompatActivity(), AnkoLogger {

    var restaurantArray: ArrayList<Restaurant> = ArrayList()
    lateinit var adapter: RestaurantListAdapter

    companion object {
        val cityId = "cityId"
        fun start(context: Context, id: Int): Intent {
            val intent = Intent(context, RestaurantListActivity::class.java)
            intent.putExtra(cityId, id)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_list)

        val cityId = intent.getIntExtra(cityId, 0)

        restaurant_rv.layoutManager = LinearLayoutManager(this)

        loadListRestaurant(cityId)

        adapter = RestaurantListAdapter(this, restaurantArray) {
            startActivity(RestaurantDetailsActivity.start(this, restaurantArray[it].id, restaurantArray[it].name))
        }

        restaurant_rv.adapter = adapter
    }

    private fun loadListRestaurant(cityId: Int) {

        val callRestaurantList: Call<RestaurantResponse> = zomatoServiceApi.getListRestaurant(cityId)

        val progressDialog = ProgressDialog(this, R.style.DialogStyle)
        progressDialog.max = 100
        with(progressDialog) {
            setMessage("Loading....")
            show()
        }

        callRestaurantList.enqueue(object : Callback<RestaurantResponse> {
            override fun onFailure(call: Call<RestaurantResponse>?, t: Throwable?) {
                call!!.cancel()
                progressDialog.dismiss()
            }

            override fun onResponse(call: Call<RestaurantResponse>?, response: Response<RestaurantResponse>?) {
                if (response != null) {
                    addListRestaurant(response.body()!!)
                    adapter.invalidate()
                }
                progressDialog.dismiss()
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