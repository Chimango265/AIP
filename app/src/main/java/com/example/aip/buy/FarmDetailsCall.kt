package com.example.aip.buy

import android.app.DownloadManager
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class FarmDetailsCall(var nationalId: String, var activity: Context){

    fun getData():JSONArray {

        var getResponse: JSONArray = JSONArray("");
        val queue = Volley.newRequestQueue(activity)
        val url = "http://192.168.43.205:8080/api/get_subsidy?" + nationalId

        val farmRequest: JsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,
                Response.Listener<JSONArray> { response ->
                    getResponse = response
                },
                Response.ErrorListener {  error ->
                    Log.i(TAG, "Error :" + error.toString())}

        )

        queue.add(farmRequest)
        return getResponse
    }

}