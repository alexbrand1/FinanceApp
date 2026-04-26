package com.example.financeapp.data.dao

import androidx.room.*
import com.example.financeapp.data.model.SharedExpense
import kotlinx.coroutines.flow.Flow

@Dao
interface SharedExpenseDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sharedExpense: SharedExpense): Long


    @Query("SELECT * FROM shared_expenses WHERE creatorUserId = :userId ORDER BY date DESC")
    fun getSharedExpensesByUser(userId: Long): Flow<List<SharedExpense>>


    @Query("SELECT * FROM shared_expenses WHERE creatorUserId = :userId AND settled = 0 ORDER BY date DESC")
    fun getUnsettledSharedExpenses(userId: Long): Flow<List<SharedExpense>>


    @Update
    suspend fun update(sharedExpense: SharedExpense)


    @Delete
    suspend fun delete(sharedExpense: SharedExpense)
}
