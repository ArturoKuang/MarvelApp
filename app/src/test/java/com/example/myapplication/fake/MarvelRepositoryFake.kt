package com.example.myapplication.fake

import com.example.myapplication.data.model.CharacterResponse
import com.example.myapplication.data.remote.IMarvelRepository
import com.example.myapplication.utils.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

class MarvelRepositoryFake : IMarvelRepository {

    private val characterJsonFile = "./src/test/resources/marvel_response.json"
    private val characterResponse: CharacterResponse

    enum class Behavior {
        SUCCESS,
        ERROR,
    }

    var behavior = Behavior.SUCCESS

    init {
        println(File(characterJsonFile).absolutePath)
        val file: String = File(characterJsonFile).readText()
        characterResponse = Json.decodeFromString(file)
    }

    override fun getCharacters(): Flow<Resource<CharacterResponse>> {
        return when (behavior) {
            Behavior.SUCCESS -> getCharactersSuccess()
            Behavior.ERROR -> getCharactersFail()
        }
    }

    private fun getCharactersSuccess() = flow {
        emit(Resource.loading())
        delay(1000L)
        emit(Resource.success(characterResponse))
    }


    private fun getCharactersFail() = flow<Resource<CharacterResponse>> {
        emit(Resource.loading())
        delay(1000L)
        emit(Resource.error("simulating http error"))
    }
}