package edu.and.netflixremake

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import edu.and.netflixremake.model.Category
import edu.and.netflixremake.model.Movie
import edu.and.netflixremake.utils.DowloadImageTask

class MovieAdapter(private val movies: List<Movie>,
                   @LayoutRes private val layoutId: Int,
                   private val onItemClickListener: ((Int) -> Unit)? = null)
    : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            val imgCover: ImageView = itemView.findViewById(R.id.img_cover)

            imgCover.setOnClickListener {
                onItemClickListener?.invoke(movie.id)
            }

            DowloadImageTask(object : DowloadImageTask.CallBackDowload {
                override fun onResult(bitmap: Bitmap) {
                    imgCover.setImageBitmap(bitmap)
                }
            }).execute(movie.coverUrl)

        //Picasso.get().load(movie.coverUrl).into(imgCover)
        }
    }
}