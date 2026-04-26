package com.example.financeapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * User - Entidad que representa un Usuario en la base de datos
 *
 * * @Entity = Anotación de Room que marca esta clase como una TBALA de SQLite
 * */
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val email: String,
    val passwordHash: String,
    val createAt: Long = System.currentTimeMillis()

)