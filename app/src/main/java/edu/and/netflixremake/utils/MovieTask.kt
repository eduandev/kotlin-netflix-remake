package edu.and.netflixremake.utils

import android.os.Handler
import android.os.Looper
import android.util.Log
import edu.and.netflixremake.model.Category
import edu.and.netflixremake.model.Movie
import edu.and.netflixremake.model.MovieDetails
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection

class MovieTask(private val callback: CallBack){

    private val handler = Handler(Looper.getMainLooper())
    private val execultor = Executors.newSingleThreadExecutor()

    interface CallBack {
        fun onPreExecute()
        fun onResult(movieDetail: MovieDetails)
        fun onFailure(message: String)
    }

    fun execute(url: String) {

        callback.onPreExecute()

        execultor.execute {

            var urlConnection: HttpsURLConnection? = null
            var buffer: BufferedInputStream? = null
            var stream: InputStream? = null

                try {
                    val requestURL = URL(url)
                    urlConnection = requestURL.openConnection() as HttpsURLConnection

                    urlConnection.readTimeout = 2000 // (2s)
                    urlConnection.connectTimeout = 2000 // (2s)

                    val statusCode: Int = urlConnection.responseCode

                    if(statusCode == 400) {
                        stream = urlConnection.errorStream
                        buffer = BufferedInputStream(stream)
                        val jsonAsString = toString(buffer)

                        val json = JSONObject(jsonAsString)
                        val message = json.getString("message")
                        throw IOException(message)

                    } else if(statusCode > 400) {
                        throw IOException("Erro na comunicação com o servidor!")
                    }

                    stream = urlConnection.inputStream

                    //val jsonAsString = stream.bufferedReader().use { it.readText() }
                    //Log.i("TESTE", jsonAsString)

                    buffer = BufferedInputStream(stream)
                    val jsonAsString = toString(buffer)

                    val movieDetails = toMovieDetails(jsonAsString)

                    handler.post {
                        callback.onResult(movieDetails)
                    }

                } catch(e: IOException) {
                    val message = e.message?: "Erro desconhecido!"
                    Log.e("TESTE", message, e)
                    handler.post {
                        callback.onFailure(message)
                    }
                } finally {
                    urlConnection?.disconnect()
                    buffer?.close()
                    stream?.close()
                }
        }
    }

    private fun toMovieDetails(jsonAsString: String) : MovieDetails {
        val json = JSONObject(jsonAsString)

        val id = json.getInt("id")
        val title = json.getString("title")
        val desc = json.getString("desc")
        val cast = json.getString("cast")
        val coverUrl = json.getString("cover_url")
        val jsonMovies = json.getJSONArray("movie")

        val similars = mutableListOf<Movie>()
        for(s in 0 until jsonMovies.length()) {
            val jsonMovie = jsonMovies.getJSONObject(s)
            val similarId = jsonMovie.getInt("id")
            val similarCoverUrl = jsonMovie.getString("cover_url")
            val mS = Movie(similarId, similarCoverUrl)
            similars.add(mS)
        }
        val movie = Movie(id, coverUrl, title, desc, cast)
        return MovieDetails(movie, similars)
    }

    private fun toString(stream: InputStream) : String {
        val bytes = ByteArray(1024)
        val baos = ByteArrayOutputStream()
        var read: Int

        while(true) {
            read = stream.read(bytes)
            if(read <= 0) {
                break
            }
            baos.write(bytes, 0, read)
        }
        return String(baos.toByteArray())
    }
}