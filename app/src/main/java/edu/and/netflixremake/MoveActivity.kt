package edu.and.netflixremake

import android.graphics.drawable.LayerDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat

class MoveActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_move)

        val moveToolbar: Toolbar = findViewById(R.id.move_toolbar)
        setSupportActionBar(moveToolbar)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Busca do desenhavel (layer-list)
        val layerDrawable: LayerDrawable = ContextCompat.getDrawable(this, R.drawable.shadows) as LayerDrawable

       // Busca do filme selecionado
        val movieCover = ContextCompat.getDrawable(this, R.drawable.movie_4)

        //Atribui o novo filme ao layer-list
        layerDrawable.setDrawableByLayerId(R.id.cover_drawable, movieCover)

        // Set no image view
        val coverImg: ImageView = findViewById(R.id.img_cover)
        coverImg.setImageDrawable(layerDrawable)
    }
}