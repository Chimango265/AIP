package com.example.aip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {

    TextView displayName;
    EditText soil, location, size, crop;
    Button btn;

    String name, nationalId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        displayName = findViewById(R.id.textView2);
        btn = findViewById(R.id.button3);

        soil = findViewById(R.id.soil);
        location = findViewById(R.id.location);
        size = findViewById(R.id.size);
        crop = findViewById(R.id.crop);

        //getting data from QR code scanner activity
        Intent intent = getIntent();
        name = intent.getStringExtra("beneficiary_name");
        nationalId = intent.getStringExtra("national_id");
        displayName.setText(name);

        // queue for API request and response using Volley library
        RequestQueue queue = Volley.newRequestQueue(this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String soilHealth = soil.getText().toString();
                String locationText = location.getText().toString();
                int farmSize = Integer.parseInt(size.getText().toString());
                String mainCrop = crop.getText().toString();

                postData(soilHealth, locationText, mainCrop, farmSize);// calling method to send API requests
            }
        });



    }

    private void postData(String soilHealth, String location, String mainCrop, int farmSize) {
        String url = "http://192.168.43.205:8080/api/register";

        RequestQueue queue = Volley.newRequestQueue(Registration.this);

        JSONObject farmer = new JSONObject();
        JSONObject farm = new JSONObject();



        try {
            farm.put("farmId", 1);
            farm.put("farmSize", farmSize);
            farm.put("mainCrop", mainCrop);
            farm.put("location", location);
            farmer.put("national_id", nationalId);
            farmer.put("fullName",name);
            farmer.put("bankAccount",667539209);
            farmer.put("farm", farm);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, farmer, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Toast.makeText(Registration.this, "Data sent to PI", Toast.LENGTH_SHORT).show();
                try {
                    btn.setText(response.getString("fullName"));
                } catch(JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(Registration.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> headers = new HashMap<String, String>();

                //headers for the POST request
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Accept", "*/*; charset=utf-8");

                return headers;
            }

        };
        queue.add(request);

    }
}