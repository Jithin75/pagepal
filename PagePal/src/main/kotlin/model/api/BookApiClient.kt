package model.api

import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import model.BookModel


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
    fun searchBook(query: String): BookModel {
        val formatQuery = query.replace(" ", "+")
        val url = "$baseUrl?q=${formatQuery}&key=${apiKey}"
        val request = Request.Builder()
            .url(url)
            .build()

        val jsonString = executeRequest(request)
        if (jsonString == null) {
            val b = BookModel("")
            return b
        }
        val bookList = parseBooks(jsonString)
        if (bookList == mutableListOf<Book>()) {
            val b = BookModel("")
            return b
        }
//        val rawBook = if (bookList.isNotEmpty()) bookList[0] else Book()
        val rawBook = bookList[0]
        val b = BookModel(rawBook.title, rawBook.authors, rawBook.img, rawBook.publisher, rawBook.publishYear, rawBook.description, rawBook.categories)
        return b
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
                imgLinks.get("thumbnail")?.asString ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/2048px-No_image_available.svg.png"
            } else {
                "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/2048px-No_image_available.svg.png"
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

