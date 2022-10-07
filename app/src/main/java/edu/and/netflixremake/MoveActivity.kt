package edu.and.netflixremake

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.LayerDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.and.netflixremake.model.Movie
import edu.and.netflixremake.model.MovieDetails
import edu.and.netflixremake.utils.DowloadImageTask
import edu.and.netflixremake.utils.MovieTask

class MoveActivity : AppCompatActivity(), MovieTask.CallBack {

    private lateinit var txtTitle: TextView
    private lateinit var txtDesc: TextView
    private lateinit var txtCast: TextView
    private lateinit var rvSimilar: RecyclerView
    private lateinit var adapter: MovieAdapter
    private lateinit var progress: ProgressBar
    private val movies = mutableListOf<Movie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_move)

        txtTitle = findViewById(R.id.movie_txt_title)
        txtDesc = findViewById(R.id.movie_txt_desc)
        txtCast = findViewById(R.id.movie_txt_cast)
        rvSimilar = findViewById(R.id.movie_rv_similar)
        progress = findViewById(R.id.movie_progress)

        val id = intent?.getIntExtra("id", 0) ?: throw IllegalStateException("ID não foi encontrado!")
        val url = "https://api.tiagoaguiar.co/netflixapp/movie/$id?apiKey=e7a785df-c171-47de-825e-2ce8a15a8c02"
        MovieTask(this).execute(url)

        adapter = MovieAdapter(movies, R.layout.movie_item_similar)
        rvSimilar.layoutManager = GridLayoutManager(this, 3)
        rvSimilar.adapter = adapter

        val moveToolbar: Toolbar = findViewById(R.id.movie_toolbar)
        setSupportActionBar(moveToolbar)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null


    }

    override fun onPreExecute() {
        progress.visibility = View.VISIBLE
    }

    override fun onFailure(message: String) {
        progress.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onResult(movieDetail: MovieDetails) {
        progress.visibility = View.GONE
        txtTitle.text = movieDetail.movie.title
        txtDesc.text = movieDetail.movie.desc
        txtCast.text = getString(R.string.cast, movieDetail.movie.cast)

        movies.clear()
        movies.addAll(movieDetail.similars)
        adapter.notifyDataSetChanged()

        DowloadImageTask(object : DowloadImageTask.CallBackDowload {
            override fun onResult(bitmap: Bitmap) {
                // Busca do desenhavel (layer-list)
                val layerDrawable: LayerDrawable = ContextCompat.getDrawable(this@MoveActivity, R.drawable.shadows) as LayerDrawable

                // Busca do filme selecionado
                val movieCover = BitmapDrawable(resources, bitmap)

                //Atribui o novo filme ao layer-list
                layerDrawable.setDrawableByLayerId(R.id.cover_drawable, movieCover)

                // Set no image view
                val coverImg: ImageView = findViewById(R.id.movie_img)
                coverImg.setImageDrawable(layerDrawable)
            }
        }).execute(movieDetail.movie.coverUrl)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}