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
                        "content": "You are a book recommender. I need you to recommend $num books based on what the user has previously read. The user's reading preferences include novels, comics, and mangas. Your response should be in JSON format (the output message must start with \"{\" and end with \"}\" ), with each entry containing the book's title followed by the author. For example: {\"recommendations\":[{\"title\": \"Book Title\", \"author\": \"Author Name\"}, {\"title\": \"Book Title\", \"author\": \"Author Name\"}]}. Ensure that all recommended books are new and relevant to the user's reading preferences, do not suggest books the user has previously read, considering the similarity of genres and types of books the user has previously read."
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

            val response = client.newCall(request).execute()
            val body = response.body?.string()

            try {
                val jsonObject = JSONObject(body)
                val jsonArray: JSONArray = jsonObject.getJSONArray("choices")
                val messageObject = jsonArray.getJSONObject(0).getJSONObject("message")
                return messageObject.getString("content")
            } catch (e: Exception) {
                println("Exception: $e")
                println("Body of HTTP response :\n$body")
                println("Question passed: \n$question")
                return "PARSE ERROR"
            }
        }
    }
}