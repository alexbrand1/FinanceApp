package com.example.financeapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.financeapp.data.database.AppDatabase
import com.example.financeapp.data.model.Budget
import com.example.financeapp.data.model.ExpenseCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar
class BudgetViewModel(application: Application) : AndroidViewModel(application) {
    /** Inicialización de DAOs */
    private val budgetDao = AppDatabase.getDatabase(context = application).budgetDao()
    private val expenseDao = AppDatabase.getDatabase(context = application).expenseDao()
    private val _budgets = MutableStateFlow<List<Budget>>(value = emptyList())
    val budgets: StateFlow<List<Budget>> = _budgets

    data class BudgetStatus(
        val budget: Budget,
        val spent: Double,
        val remaining: Double,
        val percentage: Float
    )

    private val _budgetStatuses = MutableStateFlow<List<BudgetStatus>>(value = emptyList())
    val budgetStatuses: StateFlow<List<BudgetStatus>> = _budgetStatuses

    fun loadBudgets(userId: Long, month: Int, year: Int) {
        viewModelScope.launch {
            budgetDao.getBudgetsByMonthYear(userId, month, year).collect { budgetList ->
                _budgets.value = budgetList
                updateBudgetStatuses(userId, budgetList, month, year)
            }
        }
    }
    private suspend fun updateBudgetStatuses(userId: Long, budgetList: List<Budget>, month: Int, year: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1, 0, 0, 0)
        val startDate = calendar.timeInMillis
        calendar.add(Calendar.MONTH, 1)
        val endDate = calendar.timeInMillis

        val statusList = mutableListOf<BudgetStatus>()

        for (budget in budgetList) {
            val spent = expenseDao.getTotalExpensesByCategoryAndDateRange(
                userId,
                budget.category,
                startDate,
                endDate
            ).first() ?: 0.0
            statusList.add(
                BudgetStatus(
                    budget = budget,
                    spent = spent,
                    remaining = budget.monthlyLimit - spent,
                    percentage = if (budget.monthlyLimit > 0) (spent / budget.monthlyLimit * 100).toFloat() else 0f
                )
            )
        }
        _budgetStatuses.value = statusList
}
    fun addBudget(userId: Long, category: ExpenseCategory, monthlyLimit: Double, month: Int, year: Int) {
        viewModelScope.launch {
            val budget = Budget(
                userId = userId,
                category = category,
                monthlyLimit = monthlyLimit,
                month = month,
                year = year
            )
            budgetDao.insert(budget)
            loadBudgets(userId, month, year)
        }
    }

    fun updateBudget(budget: Budget) {
        viewModelScope.launch {
            budgetDao.update(budget)

        }
    }
    fun deleteBudget(budget: Budget, userId: Long, month: Int, year: Int) {
        viewModelScope.launch {
            budgetDao.delete(budget)
            loadBudgets(userId, month, year)
        }
    }
    }
