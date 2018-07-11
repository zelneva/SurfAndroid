package dev.android.restaurants.activity

import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import com.squareup.picasso.Picasso
import dev.android.restaurants.R
import dev.android.restaurants.activity.model.RestaurantDetails
import dev.android.restaurants.activity.retrofitAPI.RetrofitClient
import dev.android.restaurants.activity.retrofitAPI.ZomatoAPI
import kotlinx.android.synthetic.main.activity_restaurant_details.*
import org.jetbrains.anko.browse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RestaurantDetailsActivity : AppCompatActivity() {

    lateinit var zomatoServiceApi: ZomatoAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_details)

        zomatoServiceApi = RetrofitClient().getClient()

        val resId = intent.getIntExtra("restaurantId", 0)
        loadRestaurantDetails(resId)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        val title = SpannableString(intent.getStringExtra("restaurantName"))
        supportActionBar!!.title = title
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


    }


    private fun loadRestaurantDetails(resId: Int) {
        val callResayurantDetails: Call<RestaurantDetails> = zomatoServiceApi.getDetailsRestaurant(resId)

        val progressDialog: ProgressDialog = ProgressDialog(this, R.style.DialogStyle)
        progressDialog.max = 100
        with(progressDialog) {
            setMessage("Loading....")
            show()
        }

        callResayurantDetails.enqueue(object : Callback<RestaurantDetails> {
            override fun onFailure(call: Call<RestaurantDetails>?, t: Throwable?) {
                progressDialog.dismiss()
                call!!.cancel()
            }

            override fun onResponse(call: Call<RestaurantDetails>?, response: Response<RestaurantDetails>?) {
                if (response != null) {
                    addDetailsRestaurant(response.body()!!)
                }
                progressDialog.dismiss()
            }
        })
    }

    fun addDetailsRestaurant(restaurantDetailResponse: RestaurantDetails) {
        val restaurant = restaurantDetailResponse
        rd_address.text = restaurant.location.address
        rd_cost.text = restaurant.average_cost_for_two
        rd_cuisines.text = restaurant.cuisines
        rd_rating.text = restaurant.user_rating.rating
        rd_review.text = restaurant.user_rating.text
        rd_site.text = restaurant.url

        if (!restaurant.featured_image.isEmpty()) {
            Picasso.with(this)
                    .load(restaurant.featured_image)
                    .into(rd_image)
        }

        rd_site.setOnClickListener { browse(restaurant.url) }
    }
}