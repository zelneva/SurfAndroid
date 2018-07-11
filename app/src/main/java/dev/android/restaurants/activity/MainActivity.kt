package dev.android.restaurants.activity


import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.*
import dev.android.restaurants.R
import dev.android.restaurants.activity.retrofitAPI.RetrofitClient
import dev.android.restaurants.activity.retrofitAPI.ZomatoAPI
import dev.android.restaurants.activity.model.City
import dev.android.restaurants.activity.response.CityResponse
import org.jetbrains.anko.AnkoLogger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(), AnkoLogger {

    lateinit var zomatoServiceApi: ZomatoAPI

    var citiesArray: ArrayList<City> = ArrayList()
    lateinit var cityAdapter: CityAdapter
    var searchText: EditText? = null
    lateinit var listCities: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchText = findViewById(R.id.searchView)
        cityAdapter = CityAdapter(citiesArray)
        listCities = findViewById(R.id.list_cities)
        listCities.adapter = cityAdapter

        zomatoServiceApi = RetrofitClient().getClient()

        searchTextListner()

        listCities.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            val intent = Intent(this, RestaurantListActivity::class.java)
            intent.putExtra("cityId", citiesArray[position].id)
            startActivity(intent)
        }
    }

    private fun searchTextListner() {
        searchText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                citiesArray.clear()
                cityAdapter.invalidate()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                citiesArray.clear()
                cityAdapter.invalidate()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (searchText?.length()!! < 2) {
                    citiesArray.clear()
                    cityAdapter.invalidate()
                    return
                }
                loadCity(p0.toString())
            }
        })
    }

    fun loadCity(searchTxt: String) {

        val callCity:Call<CityResponse> = zomatoServiceApi.getCity(searchTxt)
        callCity.enqueue(object: Callback<CityResponse>{
            override fun onFailure(call: Call<CityResponse>?, t: Throwable?) {
                call!!.cancel()
            }

            override fun onResponse(call: Call<CityResponse>?, response: Response<CityResponse>?) {
                if(response != null){
                    addCity(response.body()!!)
                    cityAdapter.invalidate()
                }
            }
        })
    }


    fun addCity(cityResponse: CityResponse) {
        citiesArray.clear()
        for (item in cityResponse.cities) {
            citiesArray.add(City(item.id, item.name, item.country_name))
        }
    }

    inner class CityAdapter(private var citiesList: ArrayList<City>) : BaseAdapter() {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val view: View?
            val vh: ViewHolder

            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.city_list_item, parent, false)
                vh = ViewHolder(view)
                view!!.tag = vh
            } else {
                view = convertView
                vh = view.tag as ViewHolder
            }

            vh.city.text = citiesList[position].name
            vh.counrty.text = citiesList[position].country_name
            return view
        }

        override fun getItem(position: Int): Any {
            return citiesList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return citiesList.size
        }

        fun invalidate() {
            this.notifyDataSetChanged()
        }
    }

    private class ViewHolder(view: View?) {
        var city: TextView = view?.findViewById(R.id.city_name) as TextView
        var counrty: TextView = view?.findViewById(R.id.county_name) as TextView
    }
}
