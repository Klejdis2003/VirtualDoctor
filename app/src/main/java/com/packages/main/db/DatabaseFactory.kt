//package com.packages.main.db
//
//import com.packages.virtual_doctor.BuildConfig.DATABASE_URL
//import com.packages.virtual_doctor.BuildConfig.SQL_PASSWORD
//import com.packages.virtual_doctor.BuildConfig.SQL_USER
//import org.jetbrains.exposed.sql.Database
//
//abstract class DatabaseFactory {
//    private val DRIVER = "org.postgresql.Driver"
//    fun init(){
//        Database.connect(DATABASE_URL, DRIVER, SQL_USER, SQL_PASSWORD)
//    }
//    fun rebuildTables(){
//        //SchemaUtils.create(Account)
//    }
//}


