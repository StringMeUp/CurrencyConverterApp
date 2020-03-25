package com.example.currencyconverterapp.jsoninterface

import com.example.currencyconverterapp.model.CurrencyResponse
import retrofit2.Call
import retrofit2.http.GET

interface JsonApiInterface {
    //https://api.exchangeratesapi.io/latest
    @GET("latest")
    fun getRates() : Call<CurrencyResponse>
}