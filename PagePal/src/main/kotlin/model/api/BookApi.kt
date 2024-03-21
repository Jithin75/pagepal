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

    fun searchBooks(query: String): List<Book> {
        val formatQuery = query.replace(" ", "+")
        val url = "$baseUrl?q=${formatQuery}&key=${apiKey}"
        val request = Request.Builder()
            .url(url)
            .build()

        val jsonString = executeRequest(request)
        if (jsonString == null) {
            return emptyList()
        }
        return parseBooks(jsonString)
    }

    fun searchBook(query: String): Book {
        val formatQuery = query.replace(" ", "+")
        val url = "$baseUrl?q=${formatQuery}&key=${apiKey}"
        val request = Request.Builder()
            .url(url)
            .build()

        val jsonString = executeRequest(request)
        if (jsonString == null) {
            return Book()
        }
        val bookList = parseBooks(jsonString)
        if (bookList == mutableListOf<Book>()) {
            return Book()
        }
        return bookList[0]
    }

    private fun parseBooks(jsonString: String): List<Book> {
        val jsonObject = gson.fromJson(jsonString, JsonObject::class.java)
        val itemsArray = jsonObject.getAsJsonArray("items")
        if (itemsArray == null) {
            return emptyList()
        }
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
                ?.joinToString(", ") { it.asString } ?: "unspecified Authors"
            val publisher = itemInfo.asJsonObject.get("publisher")?.asString ?: "Publisher not available"
            val publishDate = itemInfo.asJsonObject.get("publishedDate")?.asString ?: "Publish Year not available"
            val publishYear = publishDate.take(4)
            val description = itemInfo.asJsonObject.get("description")?.asString ?: "Description not available"
            val categories = itemInfo.asJsonObject.get("categories")?.asJsonArray
                ?.joinToString(", ") { it.asString } ?: "Categories not available"
            val imgLinks =  itemInfo.asJsonObject.get("imageLinks")?.asJsonObject
            val img = if (imgLinks != null) {
                imgLinks.get("thumbnail")?.asString ?: "coverNotAvailable.png"
            } else {
                "coverNotAvailable.png"
            }
            books.add(Book(title, authors, img, publisher, publishYear, description, categories))
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

data class Book(val title: String = "N/A", val authors: String = "N/A", val img: String = "N/A",
                val publisher: String = "N/A", val publishYear: String = "N/A",
                val description: String = "N/A", val categories: String = "N/A")

fun main() {
    val bookApiClient = BookApiClient()
    val query = "dune"
    val book = bookApiClient.searchBook(query)
    println("Search Result for '$query':")
    println("1. ${book.title} by ${book.authors}, published by ${book.publisher}, on ${book.publishYear}.")
    println("   Categories: ${book.categories}")
    println("   Description: ${book.description}")
    println("   Image: ${book.img}")
    println()
}