package com.example.myapplication.data.realm

import androidx.room.PrimaryKey
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.FullText
import org.mongodb.kbson.ObjectId

class MarvelObject() : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    @FullText
    var name: String = ""
    var thumbnail: String = ""

    constructor(name: String = "", thumbnail: String = ""): this() {
        this.name = name
        this.thumbnail = thumbnail
    }
}

