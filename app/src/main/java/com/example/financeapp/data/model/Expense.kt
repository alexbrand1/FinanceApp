package com.example.financeapp.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Expense - Entidad que representa un Gasto en la base de datos
 *
 * Esta clase almacena todos los gastos que registra un usuario
 * Cada gasto tiene: monto, categoría, descripción, fecha, etc.
 */

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val amount: Double,
    val category: ExpenseCategory,
    val description: String,
    val date: Long = System.currentTimeMillis(),
    val isRecurring: Boolean = false
)
