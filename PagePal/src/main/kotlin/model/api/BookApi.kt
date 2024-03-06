package model.api

import okhttp3.OkHttpClient
import okhttp3.Request
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.Response

class BookApiClient(private val baseUrl: String = "https://www.googleapis.com/books/v1/volumes") {
    private val client = OkHttpClient()
    private val gson = Gson()
    private val apiKey = "AIzaSyBBXlEGsv7EiNaJkfrco-wfFj6KFaIVDlY"

    fun searchBooks(query: String): List<Book>? {
        val formatQuery = query.replace(" ", "+")
        val url = "$baseUrl?q=${formatQuery}&key=${apiKey}"
        val request = Request.Builder()
            .url(url)
            .build()

        val jsonString = executeRequest(request)
        return jsonString?.let { parseBooks(it) }
    }

    private fun parseBooks(jsonString: String): List<Book> {
        val jsonObject = gson.fromJson(jsonString, JsonObject::class.java)
        val itemsArray = jsonObject.getAsJsonArray("items")
        val items = if (itemsArray.size() >= 10) {
            itemsArray.take(10)
        } else {
            itemsArray
        }
        val books = mutableListOf<Book>()

        items?.forEach { item ->
            val itemInfo = item.asJsonObject.get("volumeInfo")
            val title = itemInfo.asJsonObject.get("title")?.asString ?: ""
            val authors = itemInfo.asJsonObject.get("authors")?.asJsonArray
                ?.joinToString(", ") { it.asString } ?: ""
            val publisher = itemInfo.asJsonObject.get("publisher")?.asString ?: ""
            val publishDate = itemInfo.asJsonObject.get("publishedDate")?.asString ?: ""
            val publishYear = publishDate.take(4)
            val description = itemInfo.asJsonObject.get("description")?.asString ?: ""
            val categories = itemInfo.asJsonObject.get("categories")?.asJsonArray
                ?.joinToString(", ") { it.asString } ?: ""
            val imgLinks =  itemInfo.asJsonObject.get("imageLinks")?.asJsonObject
            val img = if (imgLinks != null) {
                imgLinks.get("thumbnail")?.asString
            } else {
                ""
            }
            books.add(Book(title, authors, publisher, publishYear, description, categories, img))
        }
        return books
    }

    private fun executeRequest(request: Request): String? {
        return try {
            val response: Response = client.newCall(request).execute()
            response.body!!.string()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

data class Book(val title: String?, val authors: String?, val publisher: String?, val publishYear: String?,
                val description: String?, val categories: String?, val img: String?)

fun main() {
    val bookApiClient = BookApiClient()
    val query = "blue lock"
    val bookResults = bookApiClient.searchBooks(query)
    println("Search Result for '$query':")
    bookResults?.forEachIndexed { index, book ->
        println("${index + 1}. ${book.title} by ${book.authors}, published by ${book.publisher}, on ${book.publishYear}.")
        println("   Categories: ${book.categories}")
        println("   Description: ${book.description}")
        println("   Image: ${book.img}")
        println()
    }
}