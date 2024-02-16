package com.example.devrevassignment.utils

fun String?.orNa(): String {
    return if (isNullOrEmpty()) "N/A" else this
}