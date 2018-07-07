package dev.android.restaurants.activity


import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.*
import dev.android.restaurants.R
import dev.android.restaurants.activity.RetrofitAPI.DaggerRetrofitComponent
import dev.android.restaurants.activity.RetrofitAPI.RetrofitModule
import dev.android.restaurants.activity.RetrofitAPI.ZomatoAPI
import dev.android.restaurants.activity.model.City
import dev.android.restaurants.activity.response.CityResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast
import javax.inject.Inject


class MainActivity : AppCompatActivity(), AnkoLogger {

    val CITIES_COUNT = 5

    @Inject
    lateinit var zomatoServiceApi: ZomatoAPI

    var citiesArray: ArrayList<City> = ArrayList()
    lateinit var cityAdapter: CityAdapter
    var searchText: EditText? = null
    lateinit var listCities: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchText = findViewById(R.id.searchView)
        cityAdapter = CityAdapter(this.applicationContext, citiesArray)
        listCities = findViewById(R.id.list_cities)
        listCities.adapter = cityAdapter

        searchText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
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

        listCities.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            toast("Click on " + citiesArray[position].id)
        }
    }

    fun loadCity(searchTxt: String) {

        var cityResponse: CityResponse? = null

        DaggerRetrofitComponent.builder()
                .retrofitModule(RetrofitModule())
                .build()
                .inject(this)

        zomatoServiceApi.getCity(searchTxt, CITIES_COUNT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<CityResponse>() {
                    override fun onNext(cityRes: CityResponse) {
                        cityResponse = cityRes
                    }

                    override fun onError(e: Throwable) {
                        finish()
                    }

                    override fun onComplete() {
                        if (cityResponse != null) {
                            addCity(cityResponse!!)
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

    inner class CityAdapter(context: Context, private var citiesList: ArrayList<City>) : BaseAdapter() {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val view: View?
            val vh: ViewHolder

            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.city_list_item, parent, false)
                vh = ViewHolder(view)
                view.tag = vh
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
            this.notifyDataSetChanged();
        }

    }

    private class ViewHolder(view: View?) {
        var city: TextView = view?.findViewById(R.id.city_name) as TextView
        var counrty: TextView = view?.findViewById(R.id.county_name) as TextView
    }
}
