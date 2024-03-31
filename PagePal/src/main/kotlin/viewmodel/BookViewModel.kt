package viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mongodb.MongoClientSettings
import com.mongodb.kotlin.client.coroutine.MongoClient
import kotlinx.coroutines.runBlocking
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.pojo.PojoCodecProvider
import org.example.model.BookModel
import org.example.model.DatabaseManager

class BookViewModel (val bookModel: BookModel){

    val pojoCodecRegistry: CodecRegistry = CodecRegistries.fromRegistries(
        MongoClientSettings.getDefaultCodecRegistry(),
        CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
    )

    val client = MongoClient.create(connectionString = "mongodb+srv://praviin10:Prav2003@cluster0.fqt7qpj.mongodb.net/?retryWrites=true&w=majority")
    val database = client.getDatabase("PagePalDB").withCodecRegistry(pojoCodecRegistry);
    val dbManager = DatabaseManager(database)

    var isEditOpen by mutableStateOf(false)
        private set

    fun onEditClick() {
        isEditOpen = true
    }

    fun onDismissEdit() {
        isEditOpen = false
    }

    fun getCover(): String {
        return bookModel.cover
    }

    fun getTitle(): String {
        return bookModel.title
    }

    fun getAuthor(): String {
        return bookModel.author
    }

    fun getSummary(): String {
        return bookModel.description
    }

    fun getStatus(): String {
        return bookModel.status
    }

    fun setStatus(status: String) {
        bookModel.status = status
        runBlocking {
            dbManager.setBookStatus(bookModel, status)
        }
    }

    fun getChapter(): String {
        return bookModel.chapter
    }

    fun setChapter(chapter: String) {
        bookModel.chapter = chapter
        runBlocking {
            dbManager.setBookChapter(bookModel, chapter)
        }
    }

    fun getPage(): String {
        return bookModel.page
    }

    fun setPage(page: String) {
        bookModel.page = page
        runBlocking {
            dbManager.setBookPage(bookModel, page)
        }
    }
}