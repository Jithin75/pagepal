package model

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class AIRecommender {
    companion object {
        fun getResponse(question: String, num: Int): String {
            val client = OkHttpClient()

            val apiKey = "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"
            val url = "https://api.openai.com/v1/chat/completions"

            val requestBody = """
                {
                "model": "gpt-3.5-turbo",
                "messages": [
                    {
                        "role": "system",
                        "content": "You are a book recommender. I need you to recommend $num books based on what the user has previously read. The user's reading preferences include novels, comics, and mangas. Your response should be in JSON format (the output message must start with { and end with } ), with each entry containing the book's title followed by the author. For example: {\"recommendations\":[{\"title\": \"Book Title\", \"author\": \"Author Name\"}, {\"title\": \"Book Title\", \"author\": \"Author Name\"}]}. Ensure that all recommended books are new and relevant to the user's reading preferences, considering the similarity of genres and types of books the user has previously read."
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

//            client.newCall(request).enqueue(object : Callback {
//                override fun onFailure(call: Call, e: IOException) {
//                    print("error: API FAILED")
//                }
//
//                override fun onResponse(call: Call, response: Response) {
//                    val body = response.body?.string()
//                    if (body != null) {
//                        print(body)
//                    } else {
//                        print("body: empty")
//                    }
//                    val jsonObject = JSONObject(body)
//                    val jsonArray: JSONArray = jsonObject.getJSONArray("choices")
//                    val messageObject = jsonArray.getJSONObject(0).getJSONObject("message")
//                    return messageObject.getString("content")
//                }
//            })
            val response = client.newCall(request).execute()
            val body = response.body?.string()

            val jsonObject = JSONObject(body)
            val jsonArray: JSONArray = jsonObject.getJSONArray("choices")
            val messageObject = jsonArray.getJSONObject(0).getJSONObject("message")
            return messageObject.getString("content")
        }
    }
}

fun main() {
    val recommendations = AIRecommender.getResponse(
        "The user has read the following books: \\n" +
                "God of Small things: Arundati Roy\\n" +
                "Batman Hush: DC Comics\\n" +
                "The Book Thief: Marcus Zusak\\n" +
                "Gintama: Hideaki Sorachi\\n" +
                "Flashpoint Paradox: DC Comics\\n"
        , 10)
    println("Recommendations: $recommendations")
}