package com.example.myapplication.data.remote

import com.example.myapplication.data.model.CharacterResponse
import com.google.gson.Gson
import java.io.File


object TestUtils {

    fun getMockResponse(fileName: String): CharacterResponse {
        val jsonString = getJson(fileName)
        return Gson().fromJson(jsonString, CharacterResponse::class.java)
    }

    fun getJson(path: String): String {
        val uri = this.javaClass.classLoader?.getResource(path)
        val file = File(uri?.path)
        return String(file.readBytes())
    }
}
