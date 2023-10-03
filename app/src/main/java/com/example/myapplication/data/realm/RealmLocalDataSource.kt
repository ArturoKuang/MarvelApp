package com.example.myapplication.data.realm

import com.example.myapplication.data.remote.BaseDataSource
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.ResultsChange
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
class RealmLocalDataSource @Inject constructor(
    private val realm: Realm
): BaseDataSource() {

    fun addCharacter(marvelObject: MarvelObject)  {
        realm.writeBlocking {
            copyToRealm(marvelObject)
        }
    }

    fun addCharacters(marvelObjectList: List<MarvelObject>) {
        realm.writeBlocking {
            for (marvelObject in marvelObjectList) {
                copyToRealm(marvelObject)
            }
        }
    }

    fun searchCharacter(name: String): MarvelObject? {
        return realm.query<MarvelObject>("name TEXT $0", "$name*").first().find()
    }

    fun searchCharacters(name: String): Flow<ResultsChange<MarvelObject>> {
        return realm.query<MarvelObject>("name TEXT $0","$name*").find().asFlow()
    }

    fun getAllCharacters(): Flow<ResultsChange<MarvelObject>> {
        return realm.query<MarvelObject>().asFlow()
    }
}