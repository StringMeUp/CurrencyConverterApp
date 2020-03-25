package com.example.currencyconverterapp.model

data class CurrencyResponse(
    val base: String,
    val date: String,
    val rates: Rates
)