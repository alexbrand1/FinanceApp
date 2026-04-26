package com.example.financeapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.financeapp.data.model.User
import kotlinx.coroutines.flow.Flow

/**
 * UserDao - Data Acces Object para la tabla Users
 *
 * DAO = Data Acces Object (Objeto de Acceso a Datos)
 * Es una INTERFAZ que define todas las operaciones que podemos hacer sobre la tabla.
 *
 * @Dao = Anotación que marca esta interfaz como DAO
 * Room generará automáticamente la implementación de todos los métodos
 */


@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User): Long

    @Query(value = "SELECT * FROM users WHERE email= :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query(value = "SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: Long): Flow<User?>

    @Update
    suspend fun update(
        user: User
    )

    @Delete
    suspend fun delete(user: User)
}