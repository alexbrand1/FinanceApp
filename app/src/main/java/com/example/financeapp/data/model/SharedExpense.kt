package com.example.financeapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * SharedExpense - Entidad para gastos compartidos entre varias personas
 */

@Entity(tableName = "shared_expenses")
data class SharedExpense(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val creatorUserId: Long,
    val totalAmount: Double,
    val description: String,
    val category: ExpenseCategory,
    val date: Long = System.currentTimeMillis(),
    val participants: String,
    val settled: Boolean = false
)
/**
 * Participant - Clase de datos para representar un Participante
 */

data class Participant(
    val name: String,
    val amount: Double,
    val paid: Boolean = false
)

