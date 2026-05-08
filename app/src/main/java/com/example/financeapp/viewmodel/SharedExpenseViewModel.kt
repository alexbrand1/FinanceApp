package com.example.financeapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.financeapp.data.database.AppDatabase
import com.example.financeapp.data.model.ExpenseCategory
import com.example.financeapp.data.model.Participant
import com.example.financeapp.data.model.SharedExpense
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class SharedExpenseViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedExpenseDao = AppDatabase.getDatabase(context = application).sharedExpenseDao()
    private val _sharedExpenses = MutableStateFlow<List<SharedExpense>>(value = emptyList())
    val sharedExpense: StateFlow<List<SharedExpense>> = _sharedExpenses

    private val _unsettledExpenses = MutableStateFlow<List<SharedExpense>>(value = emptyList())
    val unsettledExpenses: StateFlow<List<SharedExpense>> = _unsettledExpenses

    fun loadSharedExpenses(userId: Long) {
        viewModelScope.launch {
            sharedExpenseDao.getSharedExpensesByUser(userId).collect {
                _sharedExpenses.value = it
            }
        }
    }

    fun loadUnsettledExpenses(userId: Long) {
        viewModelScope.launch {
            sharedExpenseDao.getUnsettledSharedExpenses(userId).collect {
                _unsettledExpenses.value = it
            }
        }
    }


    fun addSharedExpense(
        userId: Long,
        totalAmount: Double,
        description: String,
        category: ExpenseCategory,
        participant: List<Participant>
    ) {
        viewModelScope.launch {
            val sharedExpense = SharedExpense(
                creatorUserId = userId,
                totalAmount = totalAmount,
                description = description,
                category = category,
                participants = participantsToJson(participant)
            )
            sharedExpenseDao.insert(sharedExpense)
        }
    }

    fun markAsSettled(sharedExpense: SharedExpense) {
        viewModelScope.launch {
            sharedExpenseDao.update(sharedExpense.copy(settled = true))
        }
    }

    fun deleteSharedExpense(sharedExpense: SharedExpense) {
        viewModelScope.launch {
            sharedExpenseDao.delete(sharedExpense)
        }
    }

    fun parseParticipants(json: String): List<Participant> {
        val participants = mutableListOf<Participant>()
        val jsonArray = JSONArray(json)
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            participants.add(
                Participant(
                    name = obj.getString("name"),
                    amount = obj.getDouble("amount"),
                    paid = obj.getBoolean("paid")
                )
            )
        }
        return participants
    }
    private fun participantsToJson(participants: List<Participant>): String {
        val jsonArray = JSONArray()
        participants.forEach { participant ->
            val obj = JSONObject()
            obj.put("name", participant.name)
            obj.put("amount", participant.amount)
            obj.put("paid", participant.paid)
            jsonArray.put(obj)
        }
        return jsonArray.toString()
    }
}