package edu.and.netflixremake

import android.graphics.drawable.LayerDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.and.netflixremake.model.Movie

class MoveActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_move)

        val txtTitle: TextView = findViewById(R.id.movie_txt_title)
        val txtDesc: TextView = findViewById(R.id.movie_txt_desc)
        val txtCast: TextView = findViewById(R.id.movie_txt_cast)
        val rvSimilar: RecyclerView = findViewById(R.id.movie_rv_similar)

        txtTitle.text = "Batman Begins"
        txtDesc.text = "Descrição do filme do Batman"
        txtCast.text = getString(R.string.cast, "Christian Bale, Kate Noelle, Sir Michael Caine")

        val movies = mutableListOf<Movie>()
        for(i in 0 until 15) {
            val movie = Movie(R.drawable.movie_4)
            movies.add(movie)
        }

        rvSimilar.layoutManager = GridLayoutManager(this, 3)
        rvSimilar.adapter = MovieAdapter(movies, R.layout.movie_item_similar)

        val moveToolbar: Toolbar = findViewById(R.id.move_toolbar)
        setSupportActionBar(moveToolbar)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null

        // Busca do desenhavel (layer-list)
        val layerDrawable: LayerDrawable = ContextCompat.getDrawable(this, R.drawable.shadows) as LayerDrawable

       // Busca do filme selecionado
        val movieCover = ContextCompat.getDrawable(this, R.drawable.movie_4)

        //Atribui o novo filme ao layer-list
        layerDrawable.setDrawableByLayerId(R.id.cover_drawable, movieCover)

        // Set no image view
        val coverImg: ImageView = findViewById(R.id.movie_img)
        coverImg.setImageDrawable(layerDrawable)
    }
}