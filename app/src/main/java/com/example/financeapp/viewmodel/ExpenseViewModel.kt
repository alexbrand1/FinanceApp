package com.example.financeapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.financeapp.data.database.AppDatabase
import com.example.financeapp.data.model.Expense
import com.example.financeapp.data.model.ExpenseCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {

    private val expenseDao = AppDatabase.getDatabase(context = application).expenseDao()

    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses

    private val _totalExpenses = MutableStateFlow<Double>(0.0)
    val totalExpenses: StateFlow<Double> = _totalExpenses

    fun loadExpenses(userId: Long) {
        viewModelScope.launch {
            expenseDao.getExpensesByUser(userId).collect {
                _expenses.value = it
            }
        }
    }

    fun loadExpensesByMonth(userId: Long, month: Int, year: Int) {
        viewModelScope.launch {
            val calendar = Calendar.getInstance()
            calendar.set(year, month - 1, 1, 0, 0, 0)
            val startDate = calendar.timeInMillis

            calendar.add(Calendar.MONTH, 1)
            val endDate = calendar.timeInMillis

            expenseDao.getExpensesByDateRange(userId, startDate, endDate).collect {
                _expenses.value = it
            }

            expenseDao.getTotalExpensesByDateRange(userId, startDate, endDate).collect {
                _totalExpenses.value = it
            }
        }
    }

    fun addExpense(userId: Long, amount: Double, category: ExpenseCategory, description: String) {
        viewModelScope.launch {
            val expense = Expense(
                userId = userId,
                amount = amount,
                category = category,
                description = description
            )
            expenseDao.insert(expense)
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            expenseDao.delete(expense)
        }
    }


    fun getExpensesByCategory(userId: Long): Map<ExpenseCategory, Double> {
        val categoryTotals = mutableMapOf<ExpenseCategory, Double>()
        _expenses.value.forEach { expense ->
            categoryTotals[expense.category] = (categoryTotals[expense.category] ?: 0.0) + expense.amount
        }
        return categoryTotals
    }

}