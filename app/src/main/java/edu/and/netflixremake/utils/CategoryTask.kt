package edu.and.netflixremake.utils

import android.os.Handler
import android.os.Looper
import android.util.Log
import edu.and.netflixremake.model.Category
import edu.and.netflixremake.model.Movie
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection

class CategoryTask(private val callback: CallBack){

    private val handler = Handler(Looper.getMainLooper())
    private val execultor = Executors.newSingleThreadExecutor()

    interface CallBack {
        fun onPreExecute()
        fun onResult(categories: List<Category>)
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

                    if(statusCode > 400) {
                        throw IOException("Erro na comunicação com o servidor!")
                    }

                    stream = urlConnection.inputStream

                    //val jsonAsString = stream.bufferedReader().use { it.readText() }
                    //Log.i("TESTE", jsonAsString)

                    buffer = BufferedInputStream(stream)
                    val jsonAsString = toString(buffer)

                    val categories = toCategories(jsonAsString)

                    handler.post {
                        callback.onResult(categories)
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

    private fun toCategories(jsonAsString: String) : List<Category> {
        val categories = mutableListOf<Category>()

        val jasonRoot = JSONObject(jsonAsString)
        val jsonCategories = jasonRoot.getJSONArray("category")

        for(c in 0 until jsonCategories.length()) {
            val jsonCategory = jsonCategories.getJSONObject(c)
            val title = jsonCategory.getString("title")
            val jsonMovies = jsonCategory.getJSONArray("movie")

            val movies = mutableListOf<Movie>()
            for(m in 0 until jsonMovies.length()) {
                val jsonMovie = jsonMovies.getJSONObject(m)
                val id = jsonMovie.getInt("id")
                val url = jsonMovie.getString("cover_url")

                movies.add(Movie(id,url))
            }
            categories.add(Category(title, movies))
        }
        return categories
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