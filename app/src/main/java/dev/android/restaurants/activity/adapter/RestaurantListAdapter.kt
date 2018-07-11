package dev.android.restaurants.activity.adapter

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import dev.android.restaurants.R
import dev.android.restaurants.activity.model.Restaurant


class RestaurantListAdapter(context: Context, private val restaurants: ArrayList<Restaurant>, val listener: (Int) -> Unit) : RecyclerView.Adapter<RestaurantListAdapter.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var filterList: ArrayList<Restaurant> = restaurants

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantListAdapter.ViewHolder {

        val view = inflater.inflate(R.layout.restaurant_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestaurantListAdapter.ViewHolder, position: Int) {
        val restaurant = filterList[position]
        holder.name.text = restaurant.name
        holder.address.text = restaurant.location.address
        holder.rating.text = restaurant.rating.rating

        if (!restaurant.photoURL.isEmpty()) {
            Picasso.with(this.inflater.context)
                    .load(restaurant.photoURL)
                    .into(holder.photoRestaurant)
        }
        return holder.bind(restaurants[position], position, listener)
    }

    override fun getItemCount(): Int {
        return filterList.size
    }

    fun invalidate() {
        this.notifyDataSetChanged()
    }

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    filterList = restaurants
                } else {
                    val filteredList: ArrayList<Restaurant> = ArrayList()
                    for (row in restaurants) {
                        if (row.name.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }

                    filterList = filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = filterList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filterList = filterResults.values as ArrayList<Restaurant>
                notifyDataSetChanged()
            }
        }
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.name_restaurant)
        var address: TextView = view.findViewById(R.id.address_restaurant)
        var rating: TextView = view.findViewById(R.id.rating_restaurant)
        var photoRestaurant: ImageView = view.findViewById(R.id.photo_restaurant)

        fun bind(item: Restaurant, pos: Int, listener: (Int) -> Unit) = with(itemView) {
            val cvItem = findViewById(R.id.card_view) as CardView
            cvItem.setOnClickListener {
                listener(pos)
            }
        }
    }
}