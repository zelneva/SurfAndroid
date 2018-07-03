package dev.android.restaurants

import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.widget.SearchView


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)

        val searchItem = menu.findItem(R.id.search)
        //val searchView = MenuItemCompat.getActionView(searchItem) as SearchView
       // searchView.setOnQueryTextListener(this) //вызывается после ввода пользователем каждого символа в текстовом поле.

        return true
    }

    fun onQueryTextSubmit(query: String): Boolean { // запускается, когда будет нажата кнопка поиска.
        // User pressed the search button
        return false
    }

    fun onQueryTextChange(newText: String): Boolean {
        // User changed the text
        return false
    }
}


