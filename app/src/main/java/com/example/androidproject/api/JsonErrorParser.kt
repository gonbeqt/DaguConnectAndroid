package com.example.androidproject.api

import org.json.JSONObject

object JsonErrorParser {
    /**
     * Parses a JSON string to extract a specific field.
     *
     * @param json The JSON string to parse.
     * @param field The field to extract from the JSON.
     * @return The value of the specified field, or a default error message if parsing fails.
     */
    fun extractField(json: String?, field: String): String {
        return try {
            val jsonObject = JSONObject(json ?: "")
            jsonObject.getString(field)
        } catch (e: Exception) {
            "An error occurred"
        }
    }
}