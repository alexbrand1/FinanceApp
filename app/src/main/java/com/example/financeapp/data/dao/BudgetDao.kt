package com.example.financeapp.data.dao


import androidx.room.*
import com.example.financeapp.data.model.Budget
import com.example.financeapp.data.model.ExpenseCategory
import kotlinx.coroutines.flow.Flow



@Dao
interface BudgetDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budget: Budget): Long


    @Query("SELECT * FROM budgets WHERE userId = :userId AND month = :month AND year = :year")
    fun getBudgetsByMonthYear(userId: Long, month: Int, year: Int): Flow<List<Budget>>


    @Query("SELECT * FROM budgets WHERE userId = :userId AND category = :category AND month = :month AND year = :year LIMIT 1")
    fun getBudgetByCategoryMonthYear(
        userId: Long,
        category: ExpenseCategory,
        month: Int,
        year: Int
    ): Flow<Budget?>


    @Update
    suspend fun update(budget: Budget)


    @Delete
    suspend fun delete(budget: Budget)
}