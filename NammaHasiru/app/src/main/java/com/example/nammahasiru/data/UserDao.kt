package com.example.nammahasiru.data

import androidx.room.*

@Dao
interface UserDao {
    @Insert suspend fun insert(user: User)
    @Query("SELECT * FROM users WHERE username = :u AND password = :p LIMIT 1")
    suspend fun login(u: String, p: String): User?
    @Query("SELECT * FROM users WHERE username = :u LIMIT 1")
    suspend fun findByUsername(u: String): User?
    @Query("UPDATE users SET plantCount = plantCount + 1 WHERE username = :u")
    suspend fun incrementPlantCount(u: String)
    @Query("SELECT * FROM users WHERE username = :u LIMIT 1")
    suspend fun getUser(u: String): User?
}