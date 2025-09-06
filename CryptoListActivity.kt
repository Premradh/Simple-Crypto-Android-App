// In CryptoListActivity.kt
package com.example.cryptoapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class CryptoListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    // You'll need a RecyclerView Adapter here

    // Scheduler for periodic updates
    private val scheduler = Executors.newSingleThreadScheduledExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crypto_list)
        recyclerView = findViewById(R.id.crypto_recycler_view)

        // Start fetching data every 1 minute
        scheduler.scheduleAtFixedRate({ fetchPrices() }, 0, 1, TimeUnit.MINUTES)
    }

    private fun fetchPrices() {
        val retrofit = Retrofit.Builder()
            .baseUrl("YOUR_API_BASE_URL_HERE") // Replace with the API's base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(CryptoApiService::class.java)

        service.getPrices().enqueue(object : Callback<List<Cryptocurrency>> {
            override fun onResponse(call: Call<List<Cryptocurrency>>, response: Response<List<Cryptocurrency>>) {
                if (response.isSuccessful) {
                    val cryptoList = response.body()
                    cryptoList?.let {
                        // **Update the RecyclerView Adapter here** to display the new data
                        // You might need to filter this list for "favorites" before displaying
                    }
                }
            }

            override fun onFailure(call: Call<List<Cryptocurrency>>, t: Throwable) {
                Log.e("CryptoApp", "Error fetching data: ${t.message}")
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        scheduler.shutdown() // Important: Stop the scheduler when the activity is destroyed
    }
}
