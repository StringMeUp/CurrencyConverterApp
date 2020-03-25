package com.example.currencyconverterapp.main

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.currencyconverterapp.R
import com.example.currencyconverterapp.jsoninterface.JsonApiInterface
import com.example.currencyconverterapp.model.CurrencyResponse
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    //views
    private lateinit var fromAmount: EditText
    private lateinit var toAmount: TextView
    private lateinit var fromCurrency: Spinner
    private lateinit var toCurrency: Spinner
    private lateinit var progress: ProgressBar
    private lateinit var date: TextView

    //vars
    var value: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progress = progressBar
        //methods called
        setSpinners()
        //setOnItemSelectedListener
        toCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(
                    this@MainActivity,
                    "Please select desired currency.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {     //get response data
                if (progress.visibility == View.GONE) {
                    progress.visibility = View.VISIBLE
                }
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.exchangeratesapi.io/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val service = retrofit.create(JsonApiInterface::class.java)
                val call = service.getRates()
                call.enqueue(object : Callback<CurrencyResponse> {
                    override fun onFailure(call: Call<CurrencyResponse>, t: Throwable) {
                        t.printStackTrace()
                        Toast.makeText(
                            this@MainActivity,
                            "This was a failed call",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                    override fun onResponse(
                        call: Call<CurrencyResponse>,
                        response: Response<CurrencyResponse>
                    ) {
                        if (progress.visibility == View.VISIBLE) {
                            progress.visibility = View.GONE
                        }
                        Toast.makeText(this@MainActivity, "Ready when you are", Toast.LENGTH_SHORT)
                            .show()
                        val allItems = response.body()
                        if (allItems != null) {
                            val allRates = arrayListOf(allItems.rates)
                            //setDate
                            date = dateTextView
                            var currentDate: String = allItems.date
                            date.text = currentDate

                            //add values to arrayList : Double and get indices
                            val doubleValues = arrayListOf<Double>()
                            doubleValues.add(allItems.rates.AUD)
                            doubleValues.add(allItems.rates.BGN)
                            doubleValues.add(allItems.rates.BRL)
                            doubleValues.add(allItems.rates.CAD)
                            doubleValues.add(allItems.rates.CHF)
                            doubleValues.add(allItems.rates.CNY)
                            doubleValues.add(allItems.rates.CZK)
                            doubleValues.add(allItems.rates.DKK)
                            doubleValues.add(allItems.rates.GBP)
                            doubleValues.add(allItems.rates.HKD)
                            doubleValues.add(allItems.rates.HRK)
                            doubleValues.add(allItems.rates.HUF)
                            doubleValues.add(allItems.rates.IDR)
                            doubleValues.add(allItems.rates.ILS)
                            doubleValues.add(allItems.rates.INR)
                            doubleValues.add(allItems.rates.ISK)
                            doubleValues.add(allItems.rates.JPY)
                            doubleValues.add(allItems.rates.KRW)
                            doubleValues.add(allItems.rates.MXN)
                            doubleValues.add(allItems.rates.MYR)
                            doubleValues.add(allItems.rates.NOK)
                            doubleValues.add(allItems.rates.NZD)
                            doubleValues.add(allItems.rates.PHP)
                            doubleValues.add(allItems.rates.PLN)
                            doubleValues.add(allItems.rates.RON)
                            doubleValues.add(allItems.rates.RUB)
                            doubleValues.add(allItems.rates.SEK)
                            doubleValues.add(allItems.rates.SGD)
                            doubleValues.add(allItems.rates.THB)
                            doubleValues.add(allItems.rates.TRY)
                            doubleValues.add(allItems.rates.USD)
                            doubleValues.add(allItems.rates.ZAR)
                            //iterate through the array
                            for (positionValue in doubleValues.indices) {
                                //if adapterPosition matches position of index
                                if (parent != null) {
                                    if (positionValue == position) {
                                        //assign the value of the position to global value variable
                                        value = doubleValues[positionValue]
                                        //use the value to convert entered amount
                                        convert()
                                    }
                                }
                            }
                        }
                    }
                })

            }
        }
    }

    private fun setSpinners() {
        //set fromCurrency Spinner values
        fromCurrency = fromSpinner
        val currency = resources.getStringArray(R.array.currency)
        val currencyAdapter =
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, currency)
        fromCurrency.adapter = currencyAdapter

        //set toCurrency Spinner values
        toCurrency = toSpinner
        val currencies = resources.getStringArray(R.array.currencies)
        val currenciesAdapter =
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, currencies)
        toCurrency.adapter = currenciesAdapter
    }

    //method converts entered currency to currency of your choosing
    private fun convert() {
        val convertButton = convertButton
        convertButton.setOnClickListener {
            fromAmount = fromAmountEditText
            toAmount = toAmountTextView
            if (value != null && fromAmount.text.isNotEmpty()) {
                val result = fromAmount.text.toString().toDouble() * value!!
                toAmount.text = result.toString()
            } else {
                Toast.makeText(this@MainActivity, "Please enter a valid amount", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}
