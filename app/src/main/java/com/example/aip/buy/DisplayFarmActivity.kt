package com.example.aip.buy

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.utils.Oscillator
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.aip.R
import com.example.aip.databinding.ActivityDisplayFarmBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class DisplayFarmActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDisplayFarmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplayFarmBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //setting action bar back button
        supportActionBar!!.title = "farm details"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //getting beneficiary name and national id from ScanBuyActivity
        val intent = intent
        binding.name.text = intent.getStringExtra("beneficiaryName")
        var nationalId: String? = intent.getStringExtra("nationalId")
        Log.i(TAG, nationalId!!.trim())
        getData(nationalId!!.trim()) //api call function to get data from server

    }

    fun getData(id: String){

        val url = "https://aipbackend.herokuapp.com/api/get_subsidy?nationalId=" + id // url with national id as argument
        lateinit var requestResponse: JSONObject

        val getRequestQueue = Volley.newRequestQueue(this)
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                    try {
                        val jo = response.getJSONObject(0)
                        requestResponse = response.getJSONObject(0)
                        Log.i(TAG, jo.getString("farmer"))
                        Log.i(TAG, requestResponse.getString("farmer"))
                        binding.crop.text = jo.getString("interCrop")
                        binding.npk.text = jo.getString("npk")
                        binding.urea.text = jo.getString("urea")

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
            }
        ) { error -> Log.i(Oscillator.TAG, "Error :$error") }
        getRequestQueue.add(jsonArrayRequest)

    }
}