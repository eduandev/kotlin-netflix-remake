package edu.and.netflixremake

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.and.netflixremake.model.Category
import edu.and.netflixremake.model.Movie

class MainActivity : AppCompatActivity() {

    //private lateinit var rvMain : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i("Teste", "onCreate")

        val categories = mutableListOf<Category>()

        for(c in 0 until 10) {
            val movies = mutableListOf<Movie>()
            for(i in 0 until 5) {
                val movie = Movie(R.drawable.movie_4)
                movies.add(movie)
            }
            val category = Category("cat $c", movies)
            categories.add(category)
        }

        val adapter = CategoryAdapter(categories)
        val rvMain: RecyclerView = findViewById(R.id.rv_main)
        rvMain.layoutManager = LinearLayoutManager(this)
        rvMain.adapter = adapter
    }

}