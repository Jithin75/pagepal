package org.example

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

fun getResponse(question: String, callback: (String) -> Unit){
    val client = OkHttpClient()

    val apiKey="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"
    val url="https://api.openai.com/v1/chat/completions" // changed this line

    val requestBody="""
            {
            "model": "gpt-3.5-turbo",
            "messages": [
                {
                    "role": "system",
                    "content": "You are a book recommender. I need you to recommend 5 books based on what the user has previously read, and the books can either be novels, comics or mangas. Your response to every questions should be in a json format where each entry is the name of the book followed by the author. So it is 1) Author:Book and so on. Also all your recommendations has to be new and should contain any of the books the user has previously read. Find recommendation based on similarity of genres and possible preferences or trends obtained from the books the user has already read"
                },
                {
                    "role": "user",
                    "content": "$question"
                }
            ],
            "max_tokens": 700,
            "temperature": 0
            }
        """.trimIndent()

    val request = Request.Builder()
        .url(url)
        .addHeader("Content-Type", "application/json")
        .addHeader("Authorization", "Bearer $apiKey")
        .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            print("error: API FAILED")
        }

        override fun onResponse(call: Call, response: Response) {
            val body=response.body?.string()
            if (body != null) {
                print(body)
            }
            else{
                print("body: empty")
            }
            val jsonObject= JSONObject(body)
            val jsonArray: JSONArray =jsonObject.getJSONArray("choices")
            val messageObject = jsonArray.getJSONObject(0).getJSONObject("message")
            val textResult = messageObject.getString("content")
            callback(textResult)
        }
    })

}

fun main() {
    getResponse("The user has read the following books: \\n" +
            "God of Small things: Arundati Roy\\n" +
            "Batman Hush: DC Comics\\n" +
            "The Book Thief: Marcus Zusak\\n" +
            "Gintama: Hideaki Sorachi\\n" +
            "Flashpoint Paradox: DC Comics\\n") { response ->
        println("Recommendations: $response")
    }
}
