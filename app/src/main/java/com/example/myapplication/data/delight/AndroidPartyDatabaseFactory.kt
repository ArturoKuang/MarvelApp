package com.example.myapplication.data.delight

import com.example.Database

class AndroidPartyDatabaseFactory(private val driverFactory: DriverFactory) {

    fun createDriver(): Database {
        return Database(
            driver = driverFactory.createDriver()
        )
    }
}