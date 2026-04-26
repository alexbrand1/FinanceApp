package com.example.financeapp.data.model

enum class ExpenseCategory(val displayName: String) {
    FOOD(displayName = "Alimentación"),
    TRANSPORT(displayName = "Transporte"),
    ENTERTAINMENT(displayName = "Entretenimiento"),
    BILLS(displayName = "Servicios"),
    SHOPPING(displayName = "Compras"),
    HEALTH(displayName = "Salud"),
    EDUCATION(displayName = "Educación"),
    TRAVEL(displayName = "Viajes"),
    OTHER(displayName = "Otros")
}